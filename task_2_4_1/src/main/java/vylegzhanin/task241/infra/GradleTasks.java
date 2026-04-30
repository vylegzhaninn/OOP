package vylegzhanin.task241.infra;

/**
 * Имена задач Gradle, используемых при проверке студенческих репозиториев.
 */
public class GradleTasks {
    /** Задача компиляции исходного кода. */
    public static final String COMPILE = "compileJava";
    /** Задача генерации Javadoc-документации. */
    public static final String JAVADOC = "javadoc";
    /** Задача проверки стиля кода через Checkstyle. */
    public static final String CHECKSTYLE = "checkstyleMain";
    /** Задача запуска автоматических тестов. */
    public static final String TEST = "test";

    private GradleTasks() {
    }
}
