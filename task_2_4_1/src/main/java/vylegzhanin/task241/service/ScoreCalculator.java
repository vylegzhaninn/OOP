package vylegzhanin.task241.service;

import java.time.temporal.ChronoUnit;
import vylegzhanin.task241.domain.SettingsSpec;
import vylegzhanin.task241.domain.SubmissionSpec;
import vylegzhanin.task241.domain.TaskSpec;

/**
 * Калькулятор итоговых баллов. Содержит бизнес-логику подсчета баллов,
 * применения штрафов за дедлайны и бонусных баллов.
 */
public final class ScoreCalculator {
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
        if (!runResult.gitOk()) {
            return new TaskScoreResult(
                task.id(),
                task.title(),
                0,
                task.maxPoints(),
                submission.bonusPoints(),
                false,
                false,
                false,
                0,
                0,
                0,
                "GIT_FAILED",
                compact(runResult.details())
            );
        }
        if (!runResult.compileOk()) {
            return new TaskScoreResult(
                task.id(),
                task.title(),
                0,
                task.maxPoints(),
                submission.bonusPoints(),
                false,
                false,
                false,
                0,
                0,
                0,
                "COMPILE_FAILED",
                compact(runResult.details())
            );
        }
        if (!runResult.javadocOk()) {
            return new TaskScoreResult(
                task.id(),
                task.title(),
                0,
                task.maxPoints(),
                submission.bonusPoints(),
                true,
                false,
                false,
                0,
                0,
                0,
                "JAVADOC_FAILED",
                compact(runResult.details())
            );
        }
        if (!runResult.checkstyleOk()) {
            return new TaskScoreResult(
                task.id(),
                task.title(),
                0,
                task.maxPoints(),
                submission.bonusPoints(),
                true,
                true,
                false,
                0,
                0,
                0,
                "CHECKSTYLE_FAILED",
                compact(runResult.details())
            );
        }

        double testRatio = runResult.successRatio();
        double latenessFactor = latenessFactor(task, submission, settings.hardLateMultiplier());
        double rawPoints = task.maxPoints() * testRatio * latenessFactor + submission.bonusPoints();
        double points = round(Math.max(0, rawPoints));
        return new TaskScoreResult(
            task.id(),
            task.title(),
            points,
            task.maxPoints(),
            submission.bonusPoints(),
            true,
            true,
            true,
            runResult.passed(),
            runResult.failed(),
            runResult.skipped(),
            runResult.testsOk() ? "OK" : "TESTS_FAILED",
            compact(runResult.details())
        );
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
        return singleLine.length() > 220 ? singleLine.substring(0, 220) + "..." : singleLine;
    }

    /**
     * Округляет значение до двух знаков после запятой.
     *
     * @param value исходное значение
     * @return округленное значение
     */
    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
