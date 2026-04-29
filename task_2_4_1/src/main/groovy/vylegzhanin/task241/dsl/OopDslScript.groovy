package vylegzhanin.task241.dsl

import vylegzhanin.task241.domain.CourseConfig

import java.nio.file.Path

abstract class OopDslScript extends Script {
    final CourseDsl dsl = new CourseDsl()

    CourseConfig resultConfig = CourseConfig.empty()

    def course(@DelegatesTo(value = CourseDsl, strategy = Closure.DELEGATE_FIRST) Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = dsl
        closure.call()
        resultConfig = dsl.build()
    }

    def importConfig(String relativePath) {
        Path currentFile = (Path) binding.getVariable("__configFile")
        ConfigLoader loader = (ConfigLoader) binding.getVariable("__loader")
        CourseConfig imported = loader.load(currentFile.parent.resolve(relativePath))
        dsl.merge(imported)
    }
}

