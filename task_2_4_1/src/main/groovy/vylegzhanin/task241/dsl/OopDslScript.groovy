package vylegzhanin.task241.dsl

import groovy.transform.PackageScope
import vylegzhanin.task241.domain.CourseConfig

abstract class OopDslScript extends Script {
    final CourseDsl dsl = new CourseDsl()

    CourseConfig resultConfig = CourseConfig.empty()

    def course(@DelegatesTo(value = CourseDsl, strategy = Closure.DELEGATE_FIRST) Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = dsl
        closure.call()
        resultConfig = dsl.build()
    }
}

