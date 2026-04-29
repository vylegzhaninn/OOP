package vylegzhanin.task241.infra;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import vylegzhanin.task241.domain.CommandResult;

/**
 * Раннер (runner) для запуска задач билдера Gradle.
 */
public final class GradleRunner {
    private final CommandExecutor commandExecutor;
    private final Duration timeout;

    /**
     * Конструктор GradleRunner.
     *
     * @param commandExecutor сервис запуска консольных команд
     * @param timeout         максимальное время выполнения задачи
     */
    public GradleRunner(CommandExecutor commandExecutor, Duration timeout) {
        this.commandExecutor = commandExecutor;
        this.timeout = timeout;
    }

    /**
     * Запускает указанную Gradle задачу (task).
     * Автоматически определяет, использовать ли локальный gradlew или глобальный gradle.
     *
     * @param projectDir директория проекта
     * @param taskName   имя выполняемой задачи (например, "build" или "test")
     * @return результат запуска команды
     */
    public CommandResult runTask(Path projectDir, String taskName) {
        List<String> command;
        if (Files.exists(projectDir.resolve("gradlew"))) {
            command = List.of("./gradlew", "--no-daemon", taskName);
        } else {
            command = List.of("gradle", "--no-daemon", taskName);
        }
        return commandExecutor.run(projectDir, timeout, command, Map.of());
    }
}
