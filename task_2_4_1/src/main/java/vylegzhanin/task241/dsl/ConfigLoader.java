package vylegzhanin.task241.dsl;

import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import vylegzhanin.task241.domain.CourseConfig;

public final class ConfigLoader {
    private static final String DEFAULT_SCRIPT_NAME = "oop-check.gradle";

    private final Deque<Path> stack = new ArrayDeque<>();
    private final Set<Path> active = new HashSet<>();
    private final DslScriptExecutor scriptExecutor = new DslScriptExecutor();

    public CourseConfig loadDefault(Path launchDirectory) {
        return load(launchDirectory.resolve(DEFAULT_SCRIPT_NAME));
    }

    public CourseConfig load(Path configFile) {
        Path absolute = configFile.toAbsolutePath().normalize();
        if (!active.add(absolute)) {
            throw new IllegalStateException("Circular DSL import detected: " + absolute);
        }
        stack.push(absolute);
        try {
            Object value = scriptExecutor.execute(absolute, this);
            if (!(value instanceof CourseConfig config)) {
                throw new IllegalStateException("DSL did not produce CourseConfig: " + absolute);
            }
            return config;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load DSL config " + absolute + ": " + e.getMessage(), e);
        } finally {
            stack.pop();
            active.remove(absolute);
        }
    }

    public CourseConfig loadImported(String relativePath) {
        Path current = stack.peek();
        if (current == null) {
            throw new IllegalStateException("importConfig can only be called while loading a config script");
        }
        Path imported = current.getParent().resolve(relativePath).normalize();
        return load(imported);
    }
}

