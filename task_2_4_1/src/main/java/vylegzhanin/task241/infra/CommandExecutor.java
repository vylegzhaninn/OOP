package vylegzhanin.task241.infra;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import vylegzhanin.task241.domain.CommandResult;

/**
 * Исполнитель команд операционной системы.
 * Отвечает за запуск внешних процессов с заданными параметрами, окружением и таймаутом.
 */
@Slf4j
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

        ExecutorService drainer = Executors.newSingleThreadExecutor();
        try {
            log.info("Процеес {} начал работу", command);
            Process process = processBuilder.start();
            Future<String> outputFuture = drainer.submit(() -> drainStream(process.getInputStream()));

            boolean finished = process.waitFor(timeout.toSeconds(), TimeUnit.SECONDS);
            if (!finished) {
                process.descendants().forEach(ProcessHandle::destroyForcibly);
                process.destroyForcibly();
                log.warn("Процесс {} завершился неудачно", command);
                return new CommandResult(124, "Command timed out: " + String.join(" ", command));
            }

            String output = outputFuture.get();
            return new CommandResult(process.exitValue(), output);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new CommandResult(1, "Command failed: " + e.getMessage());
        } catch (IOException | ExecutionException e) {
            return new CommandResult(1, "Command failed: " + e.getMessage());
        } finally {
            drainer.shutdownNow();
        }
    }

    private static String drainStream(InputStream stream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[8192];
        int read;
        while ((read = stream.read(chunk)) != -1) {
            buffer.write(chunk, 0, read);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }
}
