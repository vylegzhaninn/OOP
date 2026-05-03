package vylegzhanin.task241.infra;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import vylegzhanin.task241.domain.TestStats;

/**
 * Парсер отчетов о тестировании в формате JUnit XML.
 */
public class JUnitXmlParser {
    private enum Status { PASSED, FAILED, SKIPPED }

    /**
     * Парсит директорию с результатами тестов и возвращает агрегированную статистику.
     *
     * @param testResultDir директория с .xml файлами отчетов (например, build/test-results/test)
     * @return объект {@link TestStats} с результатами парсинга
     */
    public TestStats parse(Path testResultDir) {
        if (!Files.exists(testResultDir)) {
            return TestStats.EMPTY;
        }

        try (Stream<Path> walk = Files.walk(testResultDir)) {
            return walk
                .filter(path -> path.getFileName().toString().endsWith(".xml"))
                .map(JUnitXmlParser::parseFile)
                .reduce(TestStats.EMPTY, TestStats::add);
        } catch (Exception ignored) {
            return TestStats.EMPTY;
        }
    }

    private static TestStats parseFile(Path xmlFile) {
        try {
            Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().parse(xmlFile.toFile());
            NodeList tests = doc.getElementsByTagName("testcase");
            int passed = 0;
            int failed = 0;
            int skipped = 0;
            for (int i = 0; i < tests.getLength(); i++) {
                switch (statusOf(tests.item(i))) {
                    case FAILED -> failed++;
                    case SKIPPED -> skipped++;
                    case PASSED -> passed++;
                }
            }
            return new TestStats(passed, failed, skipped);
        } catch (Exception ignored) {
            return TestStats.EMPTY;
        }
    }

    private static Status statusOf(Node test) {
        NodeList children = test.getChildNodes();
        boolean skipped = false;
        for (int i = 0; i < children.getLength(); i++) {
            String name = children.item(i).getNodeName();
            if ("failure".equals(name) || "error".equals(name)) {
                return Status.FAILED;
            }
            if ("skipped".equals(name)) {
                skipped = true;
            }
        }
        return skipped ? Status.SKIPPED : Status.PASSED;
    }
}
