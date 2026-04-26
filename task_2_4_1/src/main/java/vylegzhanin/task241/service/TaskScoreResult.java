package vylegzhanin.task241.service;

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
}
