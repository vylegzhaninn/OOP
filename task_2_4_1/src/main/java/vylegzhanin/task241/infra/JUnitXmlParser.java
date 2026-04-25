package vylegzhanin.task241.infra;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

public final class JUnitXmlParser {
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

    private static void parseFile(Path xmlFile, AtomicInteger passed, AtomicInteger failed, AtomicInteger skipped) {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile.toFile());
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

    public record TestStats(int passed, int failed, int skipped) {
    }
}

