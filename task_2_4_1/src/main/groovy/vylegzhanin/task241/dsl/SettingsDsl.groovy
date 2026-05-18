package vylegzhanin.task241.dsl

import vylegzhanin.task241.domain.config.GradeBound
import vylegzhanin.task241.domain.config.SettingsSpec

import java.nio.file.Path
import java.time.Duration

class SettingsDsl {
    Path workspace = SettingsSpec.defaults().workspace()
    Duration timeout = SettingsSpec.defaults().commandTimeout()
    String mainBranch = SettingsSpec.defaults().primaryBranch()
    String fallbackBranch = SettingsSpec.defaults().fallbackBranch()
    double hardLateMultiplier = SettingsSpec.defaults().hardLateMultiplier()
    final List<GradeBound> grades = new ArrayList<>(SettingsSpec.defaults().gradeBounds())

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

    void applyFrom(SettingsSpec spec) {
        workspace = spec.workspace()
        timeout = spec.commandTimeout()
        mainBranch = spec.primaryBranch()
        fallbackBranch = spec.fallbackBranch()
        hardLateMultiplier = spec.hardLateMultiplier()
        grades.clear()
        grades.addAll(spec.gradeBounds())
    }

    SettingsSpec build() {
        new SettingsSpec(workspace, timeout, mainBranch, fallbackBranch, hardLateMultiplier, grades)
    }
}
