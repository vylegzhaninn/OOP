package vylegzhanin.task241.dsl

import vylegzhanin.task241.domain.*

import java.nio.file.Path
import java.time.Duration
import java.time.LocalDate

class CourseDsl {
    private final Map<String, TaskSpec> tasks = new LinkedHashMap<>()
    private final Map<String, StudentSpec> students = new LinkedHashMap<>()
    private final List<SubmissionSpec> submissions = []
    private final List<CheckpointSpec> checkpoints = []

    private String currentGroup = "ungrouped"
    private Path workspace = SettingsSpec.defaults().workspace()
    private Duration timeout = SettingsSpec.defaults().commandTimeout()
    private String mainBranch = SettingsSpec.defaults().primaryBranch()
    private String fallbackBranch = SettingsSpec.defaults().fallbackBranch()
    private double hardLateMultiplier = SettingsSpec.defaults().hardLateMultiplier()
    private final List<GradeBound> grades = new ArrayList<>(SettingsSpec.defaults().gradeBounds())

    void merge(CourseConfig imported) {
        tasks.putAll(imported.tasks())
        students.putAll(imported.students())
        submissions.addAll(imported.submissions())
        checkpoints.addAll(imported.checkpoints())
        workspace = imported.settings().workspace()
        timeout = imported.settings().commandTimeout()
        mainBranch = imported.settings().primaryBranch()
        fallbackBranch = imported.settings().fallbackBranch()
        hardLateMultiplier = imported.settings().hardLateMultiplier()
        grades.clear()
        grades.addAll(imported.settings().gradeBounds())
    }

    def task(String id, @DelegatesTo(value = TaskDsl, strategy = 1) Closure closure) {
        def taskDsl = new TaskDsl(id)
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = taskDsl
        closure.call()
        tasks.put(id, taskDsl.build())
    }

    def group(String name, @DelegatesTo(value = CourseDsl, strategy = 1) Closure closure) {
        def previous = currentGroup
        currentGroup = name
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = this
        closure.call()
        currentGroup = previous
    }

    def student(String github, @DelegatesTo(value = StudentDsl, strategy = 1) Closure closure) {
        def studentDsl = new StudentDsl(github)
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = studentDsl
        closure.call()
        students.put(github, studentDsl.build(currentGroup))
    }

    def check(String github, String taskId, @DelegatesTo(value = SubmissionDsl, strategy = 1) Closure closure = null) {
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

    def settings(@DelegatesTo(value = SettingsDsl, strategy = 1) Closure closure) {
        def settingsDsl = new SettingsDsl(workspace, timeout, mainBranch, fallbackBranch, hardLateMultiplier, grades)
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = settingsDsl
        closure.call()
        workspace = settingsDsl.workspace
        timeout = settingsDsl.timeout
        mainBranch = settingsDsl.mainBranch
        fallbackBranch = settingsDsl.fallbackBranch
        hardLateMultiplier = settingsDsl.hardLateMultiplier
        grades.clear()
        grades.addAll(settingsDsl.grades)
    }

    CourseConfig build() {
        def settings = new SettingsSpec(workspace, timeout, mainBranch, fallbackBranch, hardLateMultiplier, grades)
        return new CourseConfig(tasks, students, submissions, checkpoints, settings)
    }

    static class TaskDsl {
        private final String id
        private String title = ""
        private double maxPoints = 0
        private LocalDate softDeadline
        private LocalDate hardDeadline

        TaskDsl(String id) {
            this.id = id
        }

        def title(String value) { title = value }

        def maxPoints(Number value) { maxPoints = value.doubleValue() }

        def softDeadline(String value) { softDeadline = LocalDate.parse(value) }

        def hardDeadline(String value) { hardDeadline = LocalDate.parse(value) }

        TaskSpec build() {
            new TaskSpec(id, title, maxPoints, softDeadline, hardDeadline)
        }
    }

    static class StudentDsl {
        private final String github
        private String fullName = ""
        private String repo = ""

        StudentDsl(String github) {
            this.github = github
        }

        def fullName(String value) { fullName = value }

        def repo(String value) { repo = value }

        StudentSpec build(String groupName) {
            new StudentSpec(github, fullName, repo, groupName)
        }
    }

    static class SubmissionDsl {
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

    static class SettingsDsl {
        Path workspace
        Duration timeout
        String mainBranch
        String fallbackBranch
        double hardLateMultiplier
        final List<GradeBound> grades

        SettingsDsl(Path workspace, Duration timeout, String mainBranch, String fallbackBranch, double hardLateMultiplier, List<GradeBound> grades) {
            this.workspace = workspace
            this.timeout = timeout
            this.mainBranch = mainBranch
            this.fallbackBranch = fallbackBranch
            this.hardLateMultiplier = hardLateMultiplier
            this.grades = new ArrayList<>(grades)
        }

        def workspace(String path) { workspace = Path.of(path) }

        def timeoutSeconds(Number seconds) { timeout = Duration.ofSeconds(seconds.longValue()) }

        def mainBranch(String value) { mainBranch = value }

        def fallbackBranch(String value) { fallbackBranch = value }

        def hardLateMultiplier(Number value) { hardLateMultiplier = value.doubleValue() }

        def grade(String name, Number minPercent) {
            grades.add(new GradeBound(name, minPercent.doubleValue()))
        }

        def clearGrades() {
            grades.clear()
        }
    }
}


