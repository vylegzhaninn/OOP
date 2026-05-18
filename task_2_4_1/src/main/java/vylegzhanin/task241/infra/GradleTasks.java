package vylegzhanin.task241.infra;

/**
 * Имена задач Gradle, используемых при проверке студенческих репозиториев.
 */
public enum GradleTasks {
    /** Задача компиляции исходного кода. */
    COMPILE("compileJava"),
    /** Задача генерации Javadoc-документации. */
    JAVADOC("javadoc"),
    /** Задача проверки стиля кода через Checkstyle. */
    CHECKSTYLE("checkstyleMain"),
    /** Задача запуска автоматических тестов. */
    TEST("test");

    private final String taskName;

    GradleTasks(String taskName) {
        this.taskName = taskName;
    }

    /**
     * @return строковое представление задачи Gradle
     */
    public String getTaskName() {
        return taskName;
    }
}
