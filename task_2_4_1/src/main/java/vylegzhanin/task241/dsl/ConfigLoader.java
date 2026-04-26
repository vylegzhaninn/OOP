package vylegzhanin.task241.dsl;

import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import vylegzhanin.task241.domain.CourseConfig;

/**
 * Загрузчик конфигурации курса. Считывает настройки из DSL-скриптов.
 * Поддерживает подгрузку вложенных конфигураций (импорты) и защиту от зацикливаний.
 */
public final class ConfigLoader {
    private static final String DEFAULT_SCRIPT_NAME = "oop-check.gradle";

    private final Deque<Path> stack = new ArrayDeque<>();
    private final Set<Path> active = new HashSet<>();
    private final DslScriptExecutor scriptExecutor = new DslScriptExecutor();

    /**
     * Загружает конфигурацию из скрипта по умолчанию ("oop-check.gradle") в текущей директории.
     *
     * @param launchDirectory директория запуска
     * @return объект конфигурации {@link CourseConfig}
     */
    public CourseConfig loadDefault(Path launchDirectory) {
        return load(launchDirectory.resolve(DEFAULT_SCRIPT_NAME));
    }

    /**
     * Загружает конфигурацию из указанного файла скрипта.
     *
     * @param configFile файл скрипта с конфигурацией
     * @return объект конфигурации {@link CourseConfig}
     * @throws IllegalStateException если обнаружено циклическое чтение импортов, скрипт содержит ошибки или не вернул CourseConfig
     */
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

    /**
     * Импортирует конфигурацию из другого файла как составную часть текущего скрипта.
     * Должно вызываться только в процессе загрузки основного скрипта.
     *
     * @param relativePath относительный путь к файлу
     * @return объект конфигурации {@link CourseConfig}
     */
    public CourseConfig loadImported(String relativePath) {
        Path current = stack.peek();
        if (current == null) {
            throw new IllegalStateException("importConfig can only be called while loading a config script");
        }
        Path imported = current.getParent().resolve(relativePath).normalize();
        return load(imported);
    }
}
