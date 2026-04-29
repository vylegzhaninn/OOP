package vylegzhanin.task241.domain;

/**
 * Детальные результаты проверки одного конкретного задания (таски).
 *
 * @param taskId      иденификатор задания (например, "Task_1_1_1")
 * @param taskTitle   название задания
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
 * @param note        дополнительные примечания/заметки (например, штраф за опоздание)
 */
public record TaskScoreResult(
    String taskId,
    String taskTitle,
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
    public static TaskScoreResult failed(TaskSpec task, SubmissionSpec sub,
                                         boolean buildOk, boolean docsOk, boolean styleOk,
                                         String status, String note) {
        return new TaskScoreResult(
            task.id(), task.title(), 0, task.maxPoints(), sub.bonusPoints(),
            buildOk, docsOk, styleOk, 0, 0, 0, status, note
        );
    }

    public static TaskScoreResult unknownTask(SubmissionSpec sub) {
        return new TaskScoreResult(
            sub.taskId(), "", 0, 0, sub.bonusPoints(),
            false, false, false, 0, 0, 0, "UNKNOWN_TASK", "Task is not defined in DSL"
        );
    }

    public static TaskScoreResult success(TaskSpec task, SubmissionSpec sub,
                                          double points, RepoRunResult run, String status) {
        return new TaskScoreResult(
            task.id(), task.title(), points, task.maxPoints(), sub.bonusPoints(),
            true, true, true, run.passed(), run.failed(), run.skipped(), status, ""
        );
    }
}
