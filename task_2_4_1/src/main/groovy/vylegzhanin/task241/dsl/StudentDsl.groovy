package vylegzhanin.task241.dsl

import vylegzhanin.task241.domain.config.StudentSpec

class StudentDsl {
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
