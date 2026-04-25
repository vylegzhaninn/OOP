package vylegzhanin.task241.infra;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GitClient {
    private final CommandExecutor commandExecutor;
    private final Duration timeout;

    public GitClient(CommandExecutor commandExecutor, Duration timeout) {
        this.commandExecutor = commandExecutor;
        this.timeout = timeout;
    }

    public CommandResult prepareRepository(Path workspace, String repoUrl, String dirName, String mainBranch, String fallbackBranch) {
        Path repoDir = workspace.resolve(dirName);
        Map<String, String> env = gitNoPromptEnv();
        try {
            Files.createDirectories(workspace);
        } catch (Exception e) {
            return new CommandResult(1, "Cannot create workspace directory: " + workspace + " (" + e.getMessage() + ")");
        }

        CommandResult accessCheck = commandExecutor.run(workspace, timeout, List.of("git", "ls-remote", repoUrl, "HEAD"), env);
        if (!accessCheck.isSuccess()) {
            return new CommandResult(accessCheck.exitCode(), "Cannot access repository without prompt: " + repoUrl + "\n" + accessCheck.output());
        }

        CommandResult cloneOrPull;
        if (Files.exists(repoDir.resolve(".git"))) {
            cloneOrPull = commandExecutor.run(repoDir, timeout, List.of("git", "pull", "--ff-only"), env);
        } else {
            cloneOrPull = commandExecutor.run(workspace, timeout, List.of("git", "clone", repoUrl, dirName), env);
        }
        if (!cloneOrPull.isSuccess()) {
            return cloneOrPull;
        }

        CommandResult checkoutPrimary = commandExecutor.run(repoDir, timeout, List.of("git", "checkout", mainBranch), env);
        if (checkoutPrimary.isSuccess()) {
            return checkoutPrimary;
        }
        return commandExecutor.run(repoDir, timeout, List.of("git", "checkout", fallbackBranch), env);
    }

    private static Map<String, String> gitNoPromptEnv() {
        Map<String, String> env = new HashMap<>();
        env.put("GIT_TERMINAL_PROMPT", "0");
        env.put("GCM_INTERACTIVE", "Never");
        return env;
    }
}


