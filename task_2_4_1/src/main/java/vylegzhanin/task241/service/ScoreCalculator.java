package vylegzhanin.task241.service;

import java.time.temporal.ChronoUnit;
import vylegzhanin.task241.domain.RepoRunResult;
import vylegzhanin.task241.domain.config.SettingsSpec;
import vylegzhanin.task241.domain.config.SubmissionSpec;
import vylegzhanin.task241.domain.report.TaskScoreResult;
import vylegzhanin.task241.domain.config.TaskSpec;

/**
 * Калькулятор итоговых баллов. Содержит бизнес-логику подсчета баллов,
 * применения штрафов за дедлайны и бонусных баллов.
 */
public class ScoreCalculator {
    private static final int MAX_DETAIL_LENGTH = 220;
    /**
     * Вычисляет баллы за задачу на основе результатов выполнения.
     *
     * @param task       описание задания
     * @param submission информация о сданном решении
     * @param runResult  сырой результат запуска репозитория студента
     * @param settings   настройки оценки курса (в том числе штрафной множитель)
     * @return готовый детальный результат {@link TaskScoreResult} для отчета
     */
    public TaskScoreResult calculate(TaskSpec task, SubmissionSpec submission,
                                     RepoRunResult runResult, SettingsSpec settings) {
        String details = compact(runResult.details());
        if (!runResult.gitOk())        return TaskScoreResult.failed(task, submission, false, false, false, "GIT_FAILED",        details);
        if (!runResult.compileOk())    return TaskScoreResult.failed(task, submission, false, false, false, "COMPILE_FAILED",    details);
        if (!runResult.javadocOk())    return TaskScoreResult.failed(task, submission, true,  false, false, "JAVADOC_FAILED",    details);
        if (!runResult.checkstyleOk()) return TaskScoreResult.failed(task, submission, true,  true,  false, "CHECKSTYLE_FAILED", details);

        double testRatio = runResult.successRatio();
        double latenessFactor = latenessFactor(task, submission, settings.hardLateMultiplier());
        double points = Numbers.round2(Math.max(0, task.maxPoints() * testRatio * latenessFactor + submission.bonusPoints()));
        String status = runResult.testsOk() ? "OK" : "TESTS_FAILED";
        return TaskScoreResult.success(task, submission, points, runResult, status);
    }

    /**
     * Вычисляет множитель за просроченные дедлайны.
     *
     * @param task               описание задания с мягким и жестким дедлайнами
     * @param submission         информация о дате сдачи
     * @param hardLateMultiplier множитель баллов при жестком опоздании
     * @return коэффициент от hardLateMultiplier до 1.0
     */
    private static double latenessFactor(TaskSpec task, SubmissionSpec submission,
                                         double hardLateMultiplier) {
        if (submission.submittedAt() == null || task.softDeadline() == null ||
            task.hardDeadline() == null) {
            return 1.0;
        }
        if (!submission.submittedAt().isAfter(task.softDeadline())) {
            return 1.0;
        }
        if (submission.submittedAt().isAfter(task.hardDeadline())) {
            return hardLateMultiplier;
        }

        long allDays =
            Math.max(1, ChronoUnit.DAYS.between(task.softDeadline(), task.hardDeadline()));
        long lateDays =
            Math.max(0, ChronoUnit.DAYS.between(task.softDeadline(), submission.submittedAt()));
        double progress = (double) lateDays / allDays;
        return 1.0 - progress * (1.0 - hardLateMultiplier);
    }

    /**
     * Компактифицирует длинный текст ошибок (удаляет переносы строк, ограничивает длину).
     *
     * @param text исходный текст ошибки
     * @return однострочная компактная строка
     */
    private static String compact(String text) {
        if (text == null) {
            return "";
        }
        String singleLine = text.replace('\n', ' ').replace('\r', ' ').trim();
        return singleLine.length() > MAX_DETAIL_LENGTH ? singleLine.substring(0, MAX_DETAIL_LENGTH) + "..." : singleLine;
    }
}
