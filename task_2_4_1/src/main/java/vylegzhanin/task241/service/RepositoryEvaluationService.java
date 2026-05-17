package vylegzhanin.task241.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import vylegzhanin.task241.domain.CommandResult;
import vylegzhanin.task241.domain.RepoRunResult;
import vylegzhanin.task241.domain.TestStats;
import vylegzhanin.task241.domain.config.SettingsSpec;
import vylegzhanin.task241.infra.GitClient;
import vylegzhanin.task241.infra.GradleRunner;
import vylegzhanin.task241.infra.GradleTasks;
import vylegzhanin.task241.infra.JUnitXmlParser;

/**
 * Сервис для оценки репозитория студента.
 * Выполняет клонирование, сборку, генерацию документации, проверку стиля кода и запуск тестов.
 */
@Slf4j
public class RepositoryEvaluationService {
    private static final String TEST_RESULTS_DIR = "build/test-results/test";

    private final GitClient gitClient;
    private final GradleRunner gradleRunner;
    private final JUnitXmlParser xmlParser;

    /**
     * Создает экземпляр сервиса.
     *
     * @param gitClient    клиент для работы с Git
     * @param gradleRunner компонент для запуска задач Gradle
     * @param xmlParser    парсер XML-отчетов о тестировании
     */
    public RepositoryEvaluationService(
        GitClient gitClient,
        GradleRunner gradleRunner,
        JUnitXmlParser xmlParser
    ) {
        this.gitClient = gitClient;
        this.gradleRunner = gradleRunner;
        this.xmlParser = xmlParser;
    }

    /**
     * Запускает полный цикл проверки (подготовка, компиляция, javadoc, checkstyle, тесты) для репозитория.
     *
     * @param github            логин студента (название директории для клонирования)
     * @param repoUrl           URL репозитория
     * @param settings          настройки проверки курса
     * @param absoluteWorkspace абсолютный путь к рабочей папке
     * @param branch            ветка для проверки (обычно совпадает с taskId)
     * @return сырой результат выполнения проверок {@link RepoRunResult}
     */
    public RepoRunResult runForStudent(
        String github,
        String repoUrl,
        SettingsSpec settings,
        Path absoluteWorkspace,
        String branch
    ) {
        log.info("Инициализация репозитория [{}] задание [{}] (URL: [{}])", github, branch, repoUrl);
        CommandResult git = gitClient.prepareRepository(
            absoluteWorkspace,
            repoUrl,
            github,
            settings.primaryBranch(),
            settings.fallbackBranch()
        );

        if (!git.isSuccess()) {
            log.warn("Операция Git завершилась с ошибкой для участника [{}]. Детали: {}",
                github, git.output());
            return RepoRunResult.failed(git.output());
        }

        log.debug("Git успешно завершен для [{}].", github);

        Path repoDir = absoluteWorkspace.resolve(github);

        Path gradleWorkDir = findTaskDirectory(repoDir, branch);
        if (gradleWorkDir == null) {
            log.warn("[{}][{}] Директория задания не найдена в {}", github, branch, repoDir);
            return RepoRunResult.failed("Task directory not found: " + branch);
        }

        log.info("[{}][{}] compileJava...", github, branch);
        CommandResult compile = gradleRunner.runTask(gradleWorkDir, GradleTasks.COMPILE);
        if (!compile.isSuccess()) {
            log.warn("[{}][{}] Компиляция упала: {}", github, branch, compile.output());
            return new RepoRunResult(true, false, false, false, false, 0, 0, 0, compile.output());
        }

        log.info("[{}][{}] javadoc...", github, branch);
        CommandResult javadoc = gradleRunner.runTask(gradleWorkDir, GradleTasks.JAVADOC);
        boolean docsOk = javadoc.isSuccess();
        if (!docsOk) {
            log.warn("[{}][{}] Javadoc упал: {}", github, branch, javadoc.output());
        }

        log.info("[{}][{}] checkstyleMain...", github, branch);
        CommandResult checkstyle = gradleRunner.runTask(gradleWorkDir, GradleTasks.CHECKSTYLE);
        boolean styleOk = checkstyle.isSuccess();
        if (!styleOk) {
            log.warn("[{}][{}] Checkstyle упал: {}", github, branch, checkstyle.output());
        }

        log.info("[{}][{}] test...", github, branch);
        CommandResult test = gradleRunner.runTask(gradleWorkDir, GradleTasks.TEST);
        if (!test.isSuccess()) {
            log.warn("[{}][{}] Тесты упали: {}", github, branch, test.output());
        }
        TestStats stats = xmlParser.parse(gradleWorkDir.resolve(TEST_RESULTS_DIR));
        return new RepoRunResult(
            true,
            true,
            docsOk,
            styleOk,
            test.isSuccess(),
            stats.passed(),
            stats.failed(),
            stats.skipped(),
            test.output()
        );
    }

    private Path findTaskDirectory(Path repoDir, String taskId) {
        try (var entries = Files.list(repoDir)) {
            return entries
                .filter(Files::isDirectory)
                .filter(p -> p.getFileName().toString().equalsIgnoreCase(taskId))
                .findFirst()
                .orElse(null);
        } catch (IOException e) {
            log.warn("Ошибка при поиске директории задания {}: {}", taskId, e.getMessage());
            return null;
        }
    }
}
