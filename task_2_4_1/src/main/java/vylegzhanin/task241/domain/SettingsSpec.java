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
}
