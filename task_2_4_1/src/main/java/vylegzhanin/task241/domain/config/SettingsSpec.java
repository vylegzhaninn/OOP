package vylegzhanin.task241.domain.config;

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

/**
 * Глобальные настройки для оценки и скачивания.
 */
public record SettingsSpec(
    Path workspace,
    Duration commandTimeout,
    String primaryBranch,
    String fallbackBranch,
    double hardLateMultiplier,
    List<GradeBound> gradeBounds
) {
    /**
     * Создает настройки курса по умолчанию.
     *
     * @return стандартные настройки {@link SettingsSpec}
     */
    public static SettingsSpec defaults() {
        return new SettingsSpec(
            Path.of(".oop-checker-work"),
            Duration.ofMinutes(10),
            "main",
            "master",
            0.0,
            List.of(
                new GradeBound("5", 85),
                new GradeBound("4", 70),
                new GradeBound("3", 55),
                new GradeBound("2", 0)
            )
        );
    }
}
