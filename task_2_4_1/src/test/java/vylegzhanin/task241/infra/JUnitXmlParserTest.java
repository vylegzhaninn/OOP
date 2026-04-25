package vylegzhanin.task241.infra;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JUnitXmlParserTest {
    @Test
    void parsesPassedFailedSkipped(@TempDir Path tempDir) throws Exception {
        Path xml = tempDir.resolve("TEST-a.xml");
        Files.writeString(xml, """
                <testsuite tests=\"3\" failures=\"1\" skipped=\"1\">
                    <testcase classname=\"A\" name=\"ok\"/>
                    <testcase classname=\"A\" name=\"fail\"><failure/></testcase>
                    <testcase classname=\"A\" name=\"skip\"><skipped/></testcase>
                </testsuite>
                """);

        JUnitXmlParser.TestStats stats = new JUnitXmlParser().parse(tempDir);
        assertEquals(1, stats.passed());
        assertEquals(1, stats.failed());
        assertEquals(1, stats.skipped());
    }
}

