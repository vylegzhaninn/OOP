package vylegzhanin.task241.dsl

import vylegzhanin.task241.domain.config.SubmissionSpec

import java.time.LocalDate

class SubmissionDsl {
    private final String github
    private final String taskId
    private LocalDate submittedAt
    private double bonus = 0

    SubmissionDsl(String github, String taskId) {
        this.github = github
        this.taskId = taskId
    }

    def submittedAt(String value) { submittedAt = LocalDate.parse(value) }

    def bonus(Number value) { bonus = value.doubleValue() }

    SubmissionSpec build() {
        new SubmissionSpec(github, taskId, submittedAt, bonus)
    }
}
