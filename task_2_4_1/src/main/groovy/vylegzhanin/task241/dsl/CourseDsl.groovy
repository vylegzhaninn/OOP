package vylegzhanin.task241.dsl

import vylegzhanin.task241.domain.config.CheckpointSpec
import vylegzhanin.task241.domain.config.CourseConfig
import vylegzhanin.task241.domain.config.StudentSpec
import vylegzhanin.task241.domain.config.SubmissionSpec
import vylegzhanin.task241.domain.config.TaskSpec

import java.time.LocalDate

class CourseDsl {
    private final Map<String, TaskSpec> tasks = new LinkedHashMap<>()
    private final Map<String, StudentSpec> students = new LinkedHashMap<>()
    private final List<SubmissionSpec> submissions = []
    private final List<CheckpointSpec> checkpoints = []
    private String currentGroup = "ungrouped"
    private final SettingsDsl settingsDsl = new SettingsDsl()

    void merge(CourseConfig imported) {
        tasks.putAll(imported.tasks())
        students.putAll(imported.students())
        submissions.addAll(imported.submissions())
        checkpoints.addAll(imported.checkpoints())
        settingsDsl.applyFrom(imported.settings())
    }

    def task(String id, @DelegatesTo(value = TaskDsl, strategy = Closure.DELEGATE_FIRST) Closure closure) {
        def taskDsl = new TaskDsl(id)
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = taskDsl
        closure.call()
        tasks.put(id, taskDsl.build())
    }

    def group(String name, @DelegatesTo(value = CourseDsl, strategy = Closure.DELEGATE_FIRST) Closure closure) {
        def previous = currentGroup
        currentGroup = name
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = this
        closure.call()
        currentGroup = previous
    }

    def student(String github, @DelegatesTo(value = StudentDsl, strategy = Closure.DELEGATE_FIRST) Closure closure) {
        def studentDsl = new StudentDsl(github)
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = studentDsl
        closure.call()
        students.put(github, studentDsl.build(currentGroup))
    }

    def check(String github, String taskId, @DelegatesTo(value = SubmissionDsl, strategy = Closure.DELEGATE_FIRST) Closure closure = null) {
        def submissionDsl = new SubmissionDsl(github, taskId)
        if (closure != null) {
            closure.resolveStrategy = Closure.DELEGATE_FIRST
            closure.delegate = submissionDsl
            closure.call()
        }
        submissions.add(submissionDsl.build())
    }

    def checkpoint(String name, String dateIso) {
        checkpoints.add(new CheckpointSpec(name, LocalDate.parse(dateIso)))
    }

    def settings(@DelegatesTo(value = SettingsDsl, strategy = Closure.DELEGATE_FIRST) Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = settingsDsl
        closure.call()
    }

    CourseConfig build() {
        new CourseConfig(tasks, students, submissions, checkpoints, settingsDsl.build())
    }
}
