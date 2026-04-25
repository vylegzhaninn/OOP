package vylegzhanin.task241.infra;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public final class GradleRunner {
    private final CommandExecutor commandExecutor;
    private final Duration timeout;

    public GradleRunner(CommandExecutor commandExecutor, Duration timeout) {
        this.commandExecutor = commandExecutor;
        this.timeout = timeout;
    }

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

