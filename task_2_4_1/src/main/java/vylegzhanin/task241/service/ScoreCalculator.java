package vylegzhanin.task241.service;

import vylegzhanin.task241.domain.SettingsSpec;
import vylegzhanin.task241.domain.SubmissionSpec;
import vylegzhanin.task241.domain.TaskSpec;

import java.time.temporal.ChronoUnit;

public final class ScoreCalculator {
    public TaskScoreResult calculate(TaskSpec task, SubmissionSpec submission, RepoRunResult runResult, SettingsSpec settings) {
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

    private static double latenessFactor(TaskSpec task, SubmissionSpec submission, double hardLateMultiplier) {
        if (submission.submittedAt() == null || task.softDeadline() == null || task.hardDeadline() == null) {
            return 1.0;
        }
        if (!submission.submittedAt().isAfter(task.softDeadline())) {
            return 1.0;
        }
        if (submission.submittedAt().isAfter(task.hardDeadline())) {
            return hardLateMultiplier;
        }

        long allDays = Math.max(1, ChronoUnit.DAYS.between(task.softDeadline(), task.hardDeadline()));
        long lateDays = Math.max(0, ChronoUnit.DAYS.between(task.softDeadline(), submission.submittedAt()));
        double progress = (double) lateDays / allDays;
        return 1.0 - progress * (1.0 - hardLateMultiplier);
    }

    private static String compact(String text) {
        if (text == null) {
            return "";
        }
        String singleLine = text.replace('\n', ' ').replace('\r', ' ').trim();
        return singleLine.length() > 220 ? singleLine.substring(0, 220) + "..." : singleLine;
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
