package vylegzhanin.task241.service;

import java.nio.file.Path;
import vylegzhanin.task241.domain.SettingsSpec;
import vylegzhanin.task241.domain.TestStats;
import vylegzhanin.task241.infra.CommandResult;
import vylegzhanin.task241.infra.GitClient;
import vylegzhanin.task241.infra.GradleRunner;
import vylegzhanin.task241.infra.JUnitXmlParser;
import lombok.extern.slf4j.Slf4j;

/**
 * Сервис для оценки репозитория студента.
 * Выполняет клонирование, сборку, генерацию документации, проверку стиля кода и запуск тестов.
 */
@Slf4j
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
        log.info("Инициализация рабочей директории и клонирование репозитория учетной записи: [{}] (URL: [{}])", github, repoUrl);
        CommandResult git = gitClient.prepareRepository(
            absoluteWorkspace,
            repoUrl,
            github,
            settings.primaryBranch(),
            settings.fallbackBranch()
        );
        if (!git.isSuccess()) {
            log.warn("Операция Git завершилась с ошибкой для участника [{}]. Детали выполнения: {}", github, git.output());
            return RepoRunResult.failed(git.output());
        }

        log.debug("Этап получения исходного кода (Git) успешно завершен для [{}].", github);

        Path repoDir = absoluteWorkspace.resolve(github);

        log.info("Запуск задачи компиляции (compileJava) для участника [{}]...", github);
        CommandResult compile = gradleRunner.runTask(repoDir, "compileJava");
        if (!compile.isSuccess()) {
            log.warn("Этап компиляции завершился с ошибкой для участника [{}]. Вывод консоли: {}", github, compile.output());
            return new RepoRunResult(true, false, false, false, false, 0, 0, 0, compile.output());
        }

        log.info("Запуск задачи генерации документации (javadoc) для участника [{}]...", github);
        CommandResult javadoc = gradleRunner.runTask(repoDir, "javadoc");
        if (!javadoc.isSuccess()) {
            log.warn("Этап генерации Javadoc завершился с ошибкой для участника [{}]. Вывод консоли: {}", github, javadoc.output());
            return new RepoRunResult(true, true, false, false, false, 0, 0, 0, javadoc.output());
        }

        log.info("Запуск задачи анализа стиля кода (checkstyleMain) для участника [{}]...", github);
        CommandResult checkstyle = gradleRunner.runTask(repoDir, "checkstyleMain");
        if (!checkstyle.isSuccess()) {
            log.warn("Этап проверки Checkstyle завершился с ошибкой для участника [{}]. Вывод консоли: {}", github, checkstyle.output());
            return new RepoRunResult(true, true, true, false, false, 0, 0, 0, checkstyle.output());
        }

        log.info("Запуск набора автоматических тестов (test) для участника [{}]...", github);
        CommandResult test = gradleRunner.runTask(repoDir, "test");
        if (!test.isSuccess()) {
            log.warn("Тесты завершились с ошибками для участника [{}]. Результат выполнения: {}", github, test.output());
        }
        TestStats stats =
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
