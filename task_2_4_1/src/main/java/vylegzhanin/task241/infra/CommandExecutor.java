package vylegzhanin.task241.infra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Исполнитель команд операционной системы.
 * Отвечает за запуск внешних процессов с заданными параметрами, окружением и таймаутом.
 */
public final class CommandExecutor {
    /**
     * Выполняет указанную команду.
     *
     * @param workingDirectory рабочая директория (откуда будет запущена команда)
     * @param timeout          максимальное время выполнения (таймаут)
     * @param command          список аргументов команды (например, ["git", "status"])
     * @param extraEnv         дополнительные переменные окружения, которые будут добавлены к процессу
     * @return результат выполнения команды (включает код возврата и текст вывода)
     */
    public CommandResult run(Path workingDirectory, Duration timeout, List<String> command,
                             Map<String, String> extraEnv) {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(workingDirectory.toFile());
        processBuilder.redirectErrorStream(true);
        processBuilder.environment().putAll(extraEnv);

        try {
            Process process = processBuilder.start();
            boolean finished = process.waitFor(timeout.toSeconds(), TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                return new CommandResult(124, "Command timed out: " + String.join(" ", command));
            }

            String output;
            try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append(System.lineSeparator());
                }
                output = sb.toString();
            }
            return new CommandResult(process.exitValue(), output);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new CommandResult(1, "Command failed: " + e.getMessage());
        } catch (IOException e) {
            return new CommandResult(1, "Command failed: " + e.getMessage());
        }
    }
}
