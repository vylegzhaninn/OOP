package vylegzhanin.task241.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import vylegzhanin.task241.domain.RepoRunResult;
import vylegzhanin.task241.domain.config.SettingsSpec;
import vylegzhanin.task241.domain.config.SubmissionSpec;
import vylegzhanin.task241.domain.config.TaskSpec;
import vylegzhanin.task241.domain.report.TaskScoreResult;

class ScoreCalculatorTest {
    @Test
    void appliesLatePenaltyAndBonus() {
        TaskSpec task =
            new TaskSpec("t1", 10, LocalDate.of(2026, 4, 10), LocalDate.of(2026, 4, 20));
        SubmissionSpec submission =
            new SubmissionSpec("octo", "t1", LocalDate.of(2026, 4, 15), 1.0);

        RepoRunResult run = new RepoRunResult(true, true, true, true, true, 8, 2, 0, "");
        TaskScoreResult result =
            new ScoreCalculator().calculate(task, submission, run, SettingsSpec.defaults());

        assertEquals(5.0, result.points());
    }
}


