package vylegzhanin.task241.domain;

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
                        new GradeBound("A", 85),
                        new GradeBound("B", 70),
                        new GradeBound("C", 55),
                        new GradeBound("D", 40),
                        new GradeBound("F", 0)
                )
        );
    }
}
