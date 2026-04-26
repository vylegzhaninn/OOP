package vylegzhanin.task241.infra;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import vylegzhanin.task241.domain.TestStats;

/**
 * Парсер отчетов о тестировании в формате JUnit XML.
 */
public final class JUnitXmlParser {
    /**
     * Парсит директорию с результатами тестов и возвращает агрегированную статистику.
     *
     * @param testResultDir директория, в которой лежат .xml файлы отчетов (например, build/test-results/test)
     * @return объект {@link TestStats} с результатами парсинга
     */
    public TestStats parse(Path testResultDir) {
        AtomicInteger passed = new AtomicInteger();
        AtomicInteger failed = new AtomicInteger();
        AtomicInteger skipped = new AtomicInteger();

        if (!Files.exists(testResultDir)) {
            return new TestStats(0, 0, 0);
        }

        try {
            Files.walk(testResultDir)
                .filter(path -> path.getFileName().toString().endsWith(".xml"))
                .forEach(path -> parseFile(path, passed, failed, skipped));
        } catch (Exception ignored) {
            return new TestStats(0, 0, 0);
        }

        return new TestStats(passed.get(), failed.get(), skipped.get());
    }

    /**
     * Анализирует конкретный XML-файл отчета и обновляет счетчики пройденных, упавших и пропущенных тестов.
     *
     * @param xmlFile путь к XML-файлу
     * @param passed  счетчик успешно пройденных тестов
     * @param failed  счетчик упавших тестов
     * @param skipped счетчик пропущенных тестов
     */
    private static void parseFile(Path xmlFile, AtomicInteger passed, AtomicInteger failed,
                                  AtomicInteger skipped) {
        try {
            Document doc =
                DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile.toFile());
            NodeList tests = doc.getElementsByTagName("testcase");
            for (int i = 0; i < tests.getLength(); i++) {
                org.w3c.dom.Node test = tests.item(i);
                NodeList children = test.getChildNodes();
                boolean isFailed = false;
                boolean isSkipped = false;
                for (int j = 0; j < children.getLength(); j++) {
                    String name = children.item(j).getNodeName();
                    if ("failure".equals(name) || "error".equals(name)) {
                        isFailed = true;
                        break;
                    }
                    if ("skipped".equals(name)) {
                        isSkipped = true;
                    }
                }
                if (isFailed) {
                    failed.incrementAndGet();
                } else if (isSkipped) {
                    skipped.incrementAndGet();
                } else {
                    passed.incrementAndGet();
                }
            }
        } catch (Exception ignored) {
            // Skip malformed report file and continue parsing others.
        }
    }
}
