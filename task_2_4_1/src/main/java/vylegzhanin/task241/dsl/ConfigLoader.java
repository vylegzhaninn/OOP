package vylegzhanin.task241.dsl;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import vylegzhanin.task241.domain.CourseConfig;

/**
 * Загрузчик конфигурации курса. Считывает настройки из DSL-скриптов.
 * Поддерживает подгрузку вложенных конфигураций (импорты) и защиту от зацикливаний.
 */
public class ConfigLoader {
    private final Set<Path> active = new HashSet<>();
    private final DslScriptExecutor scriptExecutor = new DslScriptExecutor();

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
        try {
            Object value = scriptExecutor.execute(absolute, this);
            if (!(value instanceof CourseConfig config)) {
                throw new IllegalStateException("DSL did not produce CourseConfig: " + absolute);
            }
            return config;
        } catch (Exception e) {
            throw new IllegalStateException(
                "Failed to load DSL config " + absolute + ": " + e.getMessage(), e);
        } finally {
            active.remove(absolute);
        }
    }
}
