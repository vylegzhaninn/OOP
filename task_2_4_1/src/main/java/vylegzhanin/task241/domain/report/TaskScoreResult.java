package vylegzhanin.task241.domain.report;

import vylegzhanin.task241.domain.RepoRunResult;
import vylegzhanin.task241.domain.config.SubmissionSpec;
import vylegzhanin.task241.domain.config.TaskSpec;

/**
 * Детальные результаты проверки одного конкретного задания (таски).
 *
 * @param taskId      идентификатор задания (например, "task_1_1_1")
 * @param points      заработанные баллы за эту задачу
 * @param maxPoints   максимальный балл за неё
 * @param bonusPoints количество добавленных бонусных баллов
 * @param buildOk     успешна ли компиляция/сборка
 * @param docsOk      пройдена ли проверка javadoc
 * @param styleOk     пройдена ли проверка Checkstyle (style guide)
 * @param passed      количество успешно пройденных тестов
 * @param failed      количество упавших тестов
 * @param skipped     количество пропущенных тестов
 * @param status      текстовый статус (например, "OK", "FAILED", "LATE")
 * @param note        дополнительные примечания/заметки
 */
public record TaskScoreResult(
    String taskId,
    double points,
    double maxPoints,
    double bonusPoints,
    boolean buildOk,
    boolean docsOk,
    boolean styleOk,
    int passed,
    int failed,
    int skipped,
    String status,
    String note
) {
    /**
     * Создаёт результат с нулевыми баллами для задания, завершившегося критической ошибкой.
     *
     * @param task     описание задания
     * @param sub      сданное решение
     * @param buildOk  прошла ли компиляция
     * @param docsOk   прошла ли документация
     * @param styleOk  прошёл ли checkstyle
     * @param status   код ошибки (например, "GIT_FAILED", "COMPILE_FAILED")
     * @param note     детали ошибки из вывода консоли
     * @return результат с points=0 и тестами 0/0/0
     */
    public static TaskScoreResult failed(TaskSpec task, SubmissionSpec sub,
                                         boolean buildOk, boolean docsOk, boolean styleOk,
                                         String status, String note) {
        return new TaskScoreResult(
            task.id(), 0, task.maxPoints(), sub.bonusPoints(),
            buildOk, docsOk, styleOk, 0, 0, 0, status, note
        );
    }

    /**
     * Создаёт результат для задания, которое не описано в DSL-конфигурации.
     *
     * @param sub сданное решение с неизвестным taskId
     * @return результат со статусом UNKNOWN_TASK и нулевым максимальным баллом
     */
    public static TaskScoreResult unknownTask(SubmissionSpec sub) {
        return new TaskScoreResult(
            sub.taskId(), 0, 0, sub.bonusPoints(),
            false, false, false, 0, 0, 0, "UNKNOWN_TASK", "Task is not defined in DSL"
        );
    }

    /**
     * Создаёт результат для успешно (или частично успешно) пройденного задания.
     *
     * @param task   описание задания
     * @param sub    сданное решение
     * @param points итоговые баллы с учётом всех штрафов и бонусов
     * @param run    результаты прогона репозитория (флаги и счётчики тестов)
     * @param status текстовый статус (например, "OK", "CHECKSTYLE_FAILED", "TESTS_FAILED")
     * @param note   дополнительные примечания
     * @return результат с реальными флагами и счётчиками тестов из {@link RepoRunResult}
     */
    public static TaskScoreResult success(TaskSpec task, SubmissionSpec sub,
                                          double points, RepoRunResult run,
                                          String status, String note) {
        return new TaskScoreResult(
            task.id(), points, task.maxPoints(), sub.bonusPoints(),
            run.compileOk(), run.javadocOk(), run.checkstyleOk(),
            run.passed(), run.failed(), run.skipped(), status, note
        );
    }
}
