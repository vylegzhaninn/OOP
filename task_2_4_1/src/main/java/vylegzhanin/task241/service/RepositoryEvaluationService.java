package vylegzhanin.task241.service;

import java.nio.file.Path;
import vylegzhanin.task241.domain.SettingsSpec;
import vylegzhanin.task241.infra.CommandResult;
import vylegzhanin.task241.infra.GitClient;
import vylegzhanin.task241.infra.GradleRunner;
import vylegzhanin.task241.infra.JUnitXmlParser;

/**
 * Сервис для оценки репозитория студента.
 * Выполняет клонирование, сборку, генерацию документации, проверку стиля кода и запуск тестов.
 */
public final class RepositoryEvaluationService {
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
    public RepositoryEvaluationService(GitClient gitClient, GradleRunner gradleRunner,
                                       JUnitXmlParser xmlParser) {
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
     * @return сырой результат выполнения проверок {@link RepoRunResult}
     */
    public RepoRunResult runForStudent(String github, String repoUrl, SettingsSpec settings,
                                       Path absoluteWorkspace) {
        CommandResult git = gitClient.prepareRepository(
            absoluteWorkspace,
            repoUrl,
            github,
            settings.primaryBranch(),
            settings.fallbackBranch()
        );
        if (!git.isSuccess()) {
            return RepoRunResult.failed(git.output());
        }

        Path repoDir = absoluteWorkspace.resolve(github);
        CommandResult compile = gradleRunner.runTask(repoDir, "compileJava");
        if (!compile.isSuccess()) {
            return new RepoRunResult(true, false, false, false, false, 0, 0, 0, compile.output());
        }

        CommandResult javadoc = gradleRunner.runTask(repoDir, "javadoc");
        if (!javadoc.isSuccess()) {
            return new RepoRunResult(true, true, false, false, false, 0, 0, 0, javadoc.output());
        }

        CommandResult checkstyle = gradleRunner.runTask(repoDir, "checkstyleMain");
        if (!checkstyle.isSuccess()) {
            return new RepoRunResult(true, true, true, false, false, 0, 0, 0, checkstyle.output());
        }

        CommandResult test = gradleRunner.runTask(repoDir, "test");
        JUnitXmlParser.TestStats stats =
            xmlParser.parse(repoDir.resolve("build/test-results/test"));
        return new RepoRunResult(
            true,
            true,
            true,
            true,
            test.isSuccess(),
            stats.passed(),
            stats.failed(),
            stats.skipped(),
            test.output()
        );
    }
}
