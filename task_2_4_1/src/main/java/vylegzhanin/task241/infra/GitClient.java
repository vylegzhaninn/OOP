package vylegzhanin.task241.infra;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import vylegzhanin.task241.domain.CommandResult;

/**
 * Клиент для работы с системой контроля версий (Git).
 * Обеспечивает клонирование репозиториев, переключение веток и обновление кода.
 */
public class GitClient {
    private final CommandExecutor commandExecutor;
    private final Duration timeout;

    /**
     * Создает новый клиент Git.
     *
     * @param commandExecutor инструмент для запуска консольных команд
     * @param timeout         таймаут на выполнение команд
     */
    public GitClient(CommandExecutor commandExecutor, Duration timeout) {
        this.commandExecutor = commandExecutor;
        this.timeout = timeout;
    }

    /**
     * Подготавливает репозиторий в нужной директории: клонирует или обновляет его и переключает на нужную ветку.
     *
     * @param workspace      базовая рабочая директория
     * @param repoUrl        URL репозитория для клонирования
     * @param dirName        имя папки, в которой будет находиться репозиторий
     * @param mainBranch     основная ветка (например, "main")
     * @param fallbackBranch резервная ветка, если основной нет (например, "master")
     * @return результат выполнения операций (успех/ошибка с выводом)
     */
    public CommandResult prepareRepository(
        Path workspace,
        String repoUrl,
        String dirName,
        String mainBranch,
        String fallbackBranch
    ) {
        Path repoDir = workspace.resolve(dirName);
        Map<String, String> env = Map.of(
            "GIT_TERMINAL_PROMPT",
            "0", "GCM_INTERACTIVE",
            "Never"
        );

        try {
            Files.createDirectories(workspace);
        } catch (Exception e) {
            return new CommandResult(
                1,
                "Cannot create workspace directory: " + workspace
                    +
                    " (" + e.getMessage() + ")");
        }

        CommandResult accessCheck = commandExecutor.run(
            workspace,
            timeout,
            List.of("git", "ls-remote", repoUrl, "HEAD"),
            env
        );

        if (!accessCheck.isSuccess()) {
            return new CommandResult(
                accessCheck.exitCode(),
                "Cannot access repository without prompt: " + repoUrl + "\n"
                    +
                    accessCheck.output());
        }

        CommandResult cloneOrFetch;
        if (Files.exists(repoDir.resolve(".git"))) {
            cloneOrFetch =
                commandExecutor.run(repoDir, timeout, List.of("git", "fetch", "origin"), env);
        } else {
            cloneOrFetch =
                commandExecutor.run(workspace, timeout, List.of("git", "clone", repoUrl, dirName),
                    env);
        }
        if (!cloneOrFetch.isSuccess()) {
            return cloneOrFetch;
        }

        CommandResult checkoutPrimary =
            commandExecutor.run(repoDir, timeout,
                List.of("git", "reset", "--hard", "origin/" + mainBranch), env);
        if (checkoutPrimary.isSuccess()) {
            return checkoutPrimary;
        }
        return commandExecutor.run(repoDir, timeout,
            List.of("git", "reset", "--hard", "origin/" + fallbackBranch),
            env);
    }
}
