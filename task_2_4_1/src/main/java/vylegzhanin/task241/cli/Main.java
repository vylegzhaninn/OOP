package vylegzhanin.task241.cli;

import java.nio.file.Path;
import java.util.List;
import vylegzhanin.task241.domain.CourseConfig;
import vylegzhanin.task241.dsl.ConfigLoader;
import vylegzhanin.task241.infra.CommandExecutor;
import vylegzhanin.task241.infra.GitClient;
import vylegzhanin.task241.infra.GradleRunner;
import vylegzhanin.task241.infra.JUnitXmlParser;
import vylegzhanin.task241.report.HtmlReportRenderer;
import vylegzhanin.task241.service.CourseEvaluationService;
import vylegzhanin.task241.service.GradeService;
import vylegzhanin.task241.service.RepositoryEvaluationService;
import vylegzhanin.task241.service.ScoreCalculator;
import vylegzhanin.task241.service.StudentScoreReport;

/**
 * Главный класс приложения. Является точкой входа.
 */
public final class Main {
    /**
     * Закрытый конструктор, предотвращающий создание экземпляров утилитного класса.
     */
    private Main() {
    }

    /**
     * Точка входа в приложение.
     * Загружает конфигурацию, анализирует репозитории студентов и выводит HTML-отчет в стандартный вывод.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        try {
            Path launchDir = Path.of("").toAbsolutePath();
            Path configPath = resolveConfig(args, launchDir);

            ConfigLoader configLoader = new ConfigLoader();
            CourseConfig config = configLoader.load(configPath);

            CourseEvaluationService evaluationService =
                getCourseEvaluationService(config);
            List<StudentScoreReport> reports = evaluationService.evaluate(config, launchDir);

            String html = new HtmlReportRenderer().render(reports);
            System.out.println(html);
        } catch (Exception e) {
            System.err.println("Task_2_4_1 failed: " + e.getMessage());
        }
    }

    /**
     * Инициализирует и возвращает сервис для оценки курса.
     *
     * @param config конфигурация курса
     * @return готовый к использованию {@link CourseEvaluationService}
     */
    private static CourseEvaluationService getCourseEvaluationService(CourseConfig config) {
        CommandExecutor commandExecutor = new CommandExecutor();
        GitClient gitClient = new GitClient(commandExecutor, config.settings().commandTimeout());
        GradleRunner gradleRunner =
            new GradleRunner(commandExecutor, config.settings().commandTimeout());
        RepositoryEvaluationService repositoryEvaluationService = new RepositoryEvaluationService(
            gitClient,
            gradleRunner,
            new JUnitXmlParser()
        );

        CourseEvaluationService evaluationService = new CourseEvaluationService(
            repositoryEvaluationService,
            new ScoreCalculator(),
            new GradeService()
        );
        return evaluationService;
    }

    /**
     * Определяет путь к файлу конфигурации курса на основе аргументов командной строки.
     * Если параметр --config не указан то берется по дефолту oop-check.gradle
     *
     * @param args      аргументы командной строки
     * @param launchDir директория запуска приложения
     * @return путь к файлу конфигурации
     */
    private static Path resolveConfig(String[] args, Path launchDir) {
        if (args.length >= 2 && "--config".equals(args[0])) {
            return launchDir.resolve(args[1]).normalize();
        }
        return launchDir.resolve("oop-check.gradle");
    }
}
