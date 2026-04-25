package vylegzhanin.task241.service;

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
