package vylegzhanin.task241.dsl

import vylegzhanin.task241.domain.config.TaskSpec

import java.time.LocalDate

class TaskDsl {
    private final String id
    private double maxPoints = 0
    private LocalDate softDeadline
    private LocalDate hardDeadline

    TaskDsl(String id) {
        this.id = id
    }

    def maxPoints(Number value) { maxPoints = value.doubleValue() }

    def softDeadline(String value) { softDeadline = LocalDate.parse(value) }

    def hardDeadline(String value) { hardDeadline = LocalDate.parse(value) }

    TaskSpec build() {
        new TaskSpec(id, maxPoints, softDeadline, hardDeadline)
    }
}
