package vylegzhanin.task241.service;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import vylegzhanin.task241.domain.RepoRunResult;
import vylegzhanin.task241.domain.config.CheckpointSpec;
import vylegzhanin.task241.domain.config.CourseConfig;
import vylegzhanin.task241.domain.config.SettingsSpec;
import vylegzhanin.task241.domain.config.StudentSpec;
import vylegzhanin.task241.domain.config.SubmissionSpec;
import vylegzhanin.task241.domain.config.TaskSpec;
import vylegzhanin.task241.domain.report.StudentScoreReport;
import vylegzhanin.task241.domain.report.TaskScoreResult;

/**
 * Координатор оценки учебного курса.
 *
 * <p>Оркестрирует полный цикл проверки: параллельно запускает оценку каждого студента
 * в пуле потоков ({@code N = availableProcessors}), агрегирует результаты по заданиям
 * и контрольным точкам, выставляет итоговую оценку через {@link GradeService}.
 *
 * <p>Потокобезопасность: экземпляр класса неизменяем после создания и может
 * использоваться из нескольких потоков одновременно.
 */
@Slf4j
public class CourseEvaluationService {

    /**
     * Компаратор для детерминированного порядка студентов в отчёте:
     * сначала по группе, затем по полному имени.
     */
    private static final Comparator<StudentSpec> STUDENT_ORDER =
        Comparator.comparing(StudentSpec::groupName).thenComparing(StudentSpec::fullName);

    /** Максимальное время ожидания завершения пула потоков перед принудительной остановкой. */
    private static final long SHUTDOWN_TIMEOUT_MINUTES = 60;

    private final RepositoryEvaluationService repositoryEvaluationService;
    private final ScoreCalculator scoreCalculator;
    private final GradeService gradeService;

    /**
     * Создаёт сервис оценки курса.
     *
     * @param repositoryEvaluationService сервис клонирования/сборки/тестирования репозитория студента
     * @param scoreCalculator             калькулятор баллов за одно задание
     * @param gradeService                сервис перевода набранных баллов в оценку
     */
    public CourseEvaluationService(
        RepositoryEvaluationService repositoryEvaluationService,
        ScoreCalculator scoreCalculator,
        GradeService gradeService
    ) {
        this.repositoryEvaluationService = repositoryEvaluationService;
        this.scoreCalculator = scoreCalculator;
        this.gradeService = gradeService;
    }

    /**
     * Параллельно оценивает работы всех студентов и формирует итоговые отчёты.
     *
     * <p>Каждый студент обрабатывается независимо в отдельном потоке пула.
     * Submissions внутри одного студента остаются последовательными, поскольку
     * они разделяют один git working tree.
     *
     * <p>Порядок элементов в возвращаемом списке соответствует {@link #STUDENT_ORDER}:
     * по группе, затем по имени.
     *
     * @param config          конфигурация курса (студенты, задания, контрольные точки, настройки)
     * @param launchDirectory директория запуска; относительный {@code workspace} из настроек
     *                        разрешается относительно неё
     * @return список отчётов по каждому студенту в порядке {@link #STUDENT_ORDER}
     */
    public List<StudentScoreReport> evaluate(CourseConfig config, Path launchDirectory) {
        SettingsSpec settings = config.settings();
        Path workspace = launchDirectory.resolve(settings.workspace()).normalize();

        Map<String, List<SubmissionSpec>> submissionsByStudent = config.submissions().stream()
            .collect(Collectors.groupingBy(SubmissionSpec::studentGithub));

        List<StudentSpec> students = config.students().values().stream()
            .sorted(STUDENT_ORDER)
            .toList();

        ExecutorService executor = newWorkerPool();
        try {
            log.info("Параллельная проверка: студентов={}, потоков={}",
                students.size(), Runtime.getRuntime().availableProcessors());

            EvaluationContext ctx = new EvaluationContext(
                config, settings, workspace, submissionsByStudent, new ConcurrentHashMap<>());

            return students.stream()
                .map(student -> CompletableFuture.supplyAsync(
                    () -> evaluateStudent(student, ctx), executor))
                .toList()
                .stream()
                .map(CompletableFuture::join)
                .toList();
        } finally {
            shutdown(executor);
        }
    }

    /**
     * Оценивает все задания одного студента и формирует его итоговый отчёт.
     *
     * <p>Вызывается из потока пула; результат передаётся в {@link CompletableFuture}.
     *
     * @param student студент для оценки
     * @param ctx     неизменяемый контекст текущей сессии оценки
     * @return отчёт с баллами по заданиям, контрольными точками и итоговой оценкой
     */
    private StudentScoreReport evaluateStudent(StudentSpec student, EvaluationContext ctx) {
        List<SubmissionSpec> submissions =
            ctx.submissionsByStudent.getOrDefault(student.github(), List.of());

        log.info("Начало проверки участника: [{}] (Группа: {}, заданий: {})",
            student.github(), student.groupName(), submissions.size());

        List<TaskScoreResult> taskResults = submissions.stream()
            .map(submission -> evaluateSubmission(student, submission, ctx))
            .toList();

        log.debug("Оценено заданий для [{}]: {}", student.github(), taskResults.size());

        double total = taskResults.stream().mapToDouble(TaskScoreResult::points).sum();
        double max = taskResults.stream().mapToDouble(TaskScoreResult::maxPoints).sum();
        Map<String, Double> checkpoints = evaluateCheckpoints(
            ctx.config.checkpoints(), ctx.config.tasks(), submissions, taskResults);
        String grade = gradeService.resolve(total, max, ctx.settings.gradeBounds());

        return new StudentScoreReport(
            student.github(),
            student.fullName(),
            student.groupName(),
            List.copyOf(taskResults),
            Numbers.round2(total),
            Numbers.round2(max),
            checkpoints,
            grade
        );
    }

    /**
     * Оценивает одну сдачу (submission) студента по конкретному заданию.
     *
     * <p>Результат запуска репозитория берётся из кэша {@link EvaluationContext#repoCache()}
     * по ключу {@code github:taskId}, чтобы не клонировать/собирать репозиторий повторно
     * при наличии нескольких submissions на одно задание.
     *
     * @param student    студент-владелец сдачи
     * @param submission описание сдачи (ветка, дата, задание)
     * @param ctx        контекст сессии оценки
     * @return результат оценки задания с набранными баллами;
     *         если задание не найдено в конфигурации — возвращает {@link TaskScoreResult#unknownTask}
     */
    private TaskScoreResult evaluateSubmission(
        StudentSpec student, SubmissionSpec submission, EvaluationContext ctx
    ) {
        TaskSpec task = ctx.config.tasks().get(submission.taskId());
        if (task == null) {
            log.warn("Участник [{}]: неизвестное задание [{}], пропуск.",
                student.github(), submission.taskId());
            return TaskScoreResult.unknownTask(submission);
        }

        String taskId = submission.taskId();
        String cacheKey = student.github() + ":" + taskId;
        RepoRunResult runResult = ctx.repoCache.computeIfAbsent(
            cacheKey,
            key -> repositoryEvaluationService.runForStudent(
                student.github(), student.repositoryUrl(), ctx.settings, ctx.workspace, taskId
            )
        );

        log.info("Участник [{}] задание [{}] — git:{} compile:{} tests:{}",
            student.github(), taskId, runResult.gitOk(), runResult.compileOk(), runResult.testsOk());

        return scoreCalculator.calculate(task, submission, runResult, ctx.settings);
    }

    /**
     * Рассчитывает накопленные баллы студента на каждую контрольную точку.
     *
     * <p>В зачёт контрольной точки {@code cp} входят баллы за задания,
     * у которых {@code softDeadline <= cp.date()}. Задания без {@code softDeadline}
     * в расчёт не включаются.
     *
     * @param checkpoints список контрольных точек курса
     * @param tasks       справочник заданий курса (taskId → TaskSpec)
     * @param submissions список сдач студента
     * @param taskResults результаты оценки сдач студента
     * @return {@link LinkedHashMap} «имя контрольной точки → сумма баллов»
     *         в порядке следования контрольных точек в конфигурации
     */
    private Map<String, Double> evaluateCheckpoints(
        List<CheckpointSpec> checkpoints,
        Map<String, TaskSpec> tasks,
        List<SubmissionSpec> submissions,
        List<TaskScoreResult> taskResults
    ) {
        Map<String, TaskScoreResult> byTask = taskResults.stream()
            .collect(Collectors.toMap(TaskScoreResult::taskId, t -> t, (a, b) -> b));

        Map<String, Double> points = new LinkedHashMap<>();
        for (CheckpointSpec checkpoint : checkpoints) {
            double cpTotal = 0;
            for (SubmissionSpec submission : submissions) {
                TaskSpec task = tasks.get(submission.taskId());
                TaskScoreResult result = byTask.get(submission.taskId());
                if (task == null || result == null || task.softDeadline() == null) {
                    continue;
                }
                if (!task.softDeadline().isAfter(checkpoint.date())) {
                    cpTotal += result.points();
                }
            }
            points.put(checkpoint.name(), Numbers.round2(cpTotal));
        }
        return points;
    }

    /**
     * Создаёт пул потоков с именованными воркерами для параллельной оценки студентов.
     *
     * <p>Размер пула равен {@link Runtime#availableProcessors()}.
     * Потоки не демонизированы, чтобы JVM не завершалась раньше окончания оценки.
     *
     * @return настроенный {@link ExecutorService} с фиксированным числом потоков
     */
    private static ExecutorService newWorkerPool() {
        int parallelism = Runtime.getRuntime().availableProcessors();
        AtomicInteger counter = new AtomicInteger(1);
        ThreadFactory factory = runnable -> {
            Thread thread = new Thread(runnable, "course-eval-" + counter.getAndIncrement());
            thread.setDaemon(false);
            return thread;
        };
        return Executors.newFixedThreadPool(parallelism, factory);
    }

    /**
     * Корректно завершает работу пула потоков.
     *
     * <p>Сначала посылает сигнал штатного завершения и ждёт до {@link #SHUTDOWN_TIMEOUT_MINUTES} минут.
     * Если пул не укладывается в лимит — вызывает {@code shutdownNow()}.
     * При прерывании текущего потока также вызывает {@code shutdownNow()} и восстанавливает флаг прерывания.
     *
     * @param executor пул потоков для остановки
     */
    private static void shutdown(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(SHUTDOWN_TIMEOUT_MINUTES, TimeUnit.MINUTES)) {
                log.warn("Пул не завершился за {} мин — принудительная остановка.",
                    SHUTDOWN_TIMEOUT_MINUTES);
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Неизменяемый контекст одной сессии оценки, разделяемый между воркерами.
     *
     * @param config                конфигурация курса
     * @param settings              настройки окружения (workspace, таймауты, границы оценок)
     * @param workspace             абсолютный путь к рабочей директории для клонирования репозиториев
     * @param submissionsByStudent  submissions, сгруппированные по GitHub-логину студента
     * @param repoCache             кэш результатов запуска репозитория; ключ — {@code github:taskId}
     */
    private record EvaluationContext(
        CourseConfig config,
        SettingsSpec settings,
        Path workspace,
        Map<String, List<SubmissionSpec>> submissionsByStudent,
        Map<String, RepoRunResult> repoCache
    ) {}
}
