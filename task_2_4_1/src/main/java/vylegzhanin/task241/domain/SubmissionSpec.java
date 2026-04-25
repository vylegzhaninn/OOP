package vylegzhanin.task241.domain;

import java.time.LocalDate;

public record SubmissionSpec(
        String studentGithub,
        String taskId,
        LocalDate submittedAt,
        double bonusPoints
) {
}

