package vylegzhanin.task241.service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import vylegzhanin.task241.domain.config.CheckpointSpec;
import vylegzhanin.task241.domain.config.CourseConfig;
import vylegzhanin.task241.domain.RepoRunResult;
import vylegzhanin.task241.domain.config.SettingsSpec;
import vylegzhanin.task241.domain.report.StudentScoreReport;
import vylegzhanin.task241.domain.config.StudentSpec;
import vylegzhanin.task241.domain.config.SubmissionSpec;
import vylegzhanin.task241.domain.report.TaskScoreResult;
import vylegzhanin.task241.domain.config.TaskSpec;
import lombok.extern.slf4j.Slf4j;

/**
 * Основной сервис - координатор оценки учебного курса.
 * Запускает цикл проверок каждого студента и формирует агрегированные отчеты об успеваемости.
 */
@Slf4j
public class CourseEvaluationService {
    private final RepositoryEvaluationService repositoryEvaluationService;
    private final ScoreCalculator scoreCalculator;
    private final GradeService gradeService;

    /**
     * Создает экземпляр сервиса.
     *
     * @param repositoryEvaluationService сервис для проверки отдельных репозиториев
     * @param scoreCalculator             калькулятор баллов
     * @param gradeService                сервис выставления итоговых оценок
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
     * Оценивает работы всех студентов, перечисленных в конфигурации, и формирует итоговые отчеты.
     *
     * @param config          конфигурация курса, загруженная из DSL
     * @param launchDirectory базовая директория запуска
     * @return список отчетов по студентам (по одному на каждого) {@link StudentScoreReport}
     */
    public List<StudentScoreReport> evaluate(CourseConfig config, Path launchDirectory) {
        SettingsSpec settings = config.settings();
        Path workspace = launchDirectory.resolve(settings.workspace()).normalize();
        Map<String, RepoRunResult> repoCache = new HashMap<>();

        Map<String, List<SubmissionSpec>> submissionsByStudent = config.submissions().stream()
            .collect(Collectors.groupingBy(SubmissionSpec::studentGithub));

        List<StudentScoreReport> reports = new ArrayList<>();
        List<StudentSpec> students = config.students().values().stream()
            .sorted(
                Comparator.comparing(StudentSpec::groupName).thenComparing(StudentSpec::fullName))
            .toList();

        for (StudentSpec student : students) {
            List<SubmissionSpec> studentSubmissions =
                submissionsByStudent.getOrDefault(student.github(), List.of());

            log.info("Начало проверки участника: [{}] (Группа: {}, заданий: {})",
                     student.github(), student.groupName(), studentSubmissions.size());

            List<TaskScoreResult> taskResults = new ArrayList<>();
            for (SubmissionSpec submission : studentSubmissions) {
                TaskSpec task = config.tasks().get(submission.taskId());
                if (task == null) {
                    log.warn("Участник [{}]: неизвестное задание [{}], пропуск.",
                             student.github(), submission.taskId());
                    taskResults.add(TaskScoreResult.unknownTask(submission));
                    continue;
                }

                String taskId = submission.taskId();
                String cacheKey = student.github() + ":" + taskId;
                RepoRunResult runResult = repoCache.computeIfAbsent(
                    cacheKey,
                    key -> repositoryEvaluationService.runForStudent(
                        student.github(), student.repositoryUrl(), settings, workspace, taskId
                    )
                );

                log.info("Участник [{}] задание [{}] — git:{} compile:{} tests:{}",
                         student.github(), taskId, runResult.gitOk(), runResult.compileOk(), runResult.testsOk());

                taskResults.add(scoreCalculator.calculate(task, submission, runResult, settings));
            }

            log.debug("Оценено заданий для [{}]: {}", student.github(), taskResults.size());

            double total = taskResults.stream().mapToDouble(TaskScoreResult::points).sum();
            double max = taskResults.stream().mapToDouble(TaskScoreResult::maxPoints).sum();
            Map<String, Double> checkpoints =
                evaluateCheckpoints(config.checkpoints(), config.tasks(), studentSubmissions,
                    taskResults);
            String grade = gradeService.resolve(total, max, settings.gradeBounds());

            reports.add(new StudentScoreReport(
                student.github(),
                student.fullName(),
                student.groupName(),
                List.copyOf(taskResults),
                Numbers.round2(total),
                Numbers.round2(max),
                checkpoints,
                grade
            ));
        }

        return reports;
    }

    /**
     * Оценивает контрольные точки (checkpoints) для студента и вычисляет набранные баллы до определенной даты.
     *
     * @param checkpoints список контрольных точек курса
     * @param tasks       карта всех заданий
     * @param submissions решения, отправленные студентом
     * @param taskResults список вычисленных результатов по заданиям для студента
     * @return карта (название контрольной точки -> сумма баллов на этот момент)
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

}
