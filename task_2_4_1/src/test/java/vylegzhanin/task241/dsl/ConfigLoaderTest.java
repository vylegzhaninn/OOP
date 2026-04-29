package vylegzhanin.task241.dsl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import vylegzhanin.task241.domain.config.CourseConfig;

class ConfigLoaderTest {
    @Test
    void supportsImportingConfigs(@TempDir Path tempDir) throws Exception {
        Path base = tempDir.resolve("base.gradle");
        Path root = tempDir.resolve("oop-check.gradle");

        Files.writeString(base, """
            course {
                task('t1') {
                    title 'Task 1'
                    maxPoints 10
                    softDeadline '2026-04-10'
                    hardDeadline '2026-04-20'
                }
            }
            """);

        Files.writeString(root, """
            importConfig 'base.gradle'
            course {
                group('G1') {
                    student('octo') {
                        fullName 'Octo Cat'
                        repo 'https://example.com/repo.git'
                    }
                }
                check('octo', 't1')
            }
            """);

        CourseConfig config = new ConfigLoader().load(root);
        assertEquals(1, config.tasks().size());
        assertEquals(1, config.students().size());
        assertEquals(1, config.submissions().size());
    }
}

