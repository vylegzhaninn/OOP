package vylegzhanin.task241.dsl

import groovy.transform.PackageScope
import vylegzhanin.task241.domain.CourseConfig

abstract class OopDslScript extends Script {
    @PackageScope
    final CourseDsl dsl = new CourseDsl()

    CourseConfig resultConfig = CourseConfig.empty()

    def course(@DelegatesTo(value = CourseDsl, strategy = Closure.DELEGATE_FIRST) Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = dsl
        closure.call()
        resultConfig = dsl.build()
    }

    def importConfig(String path) {
        def loader = binding.getVariable("__loader") as ConfigLoader
        def imported = loader.loadImported(path)
        dsl.merge(imported)
        resultConfig = dsl.build()
    }
}

