package vylegzhanin.task241.domain;

import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class SettingsSpec {
    private final Path workspace;
    private final Duration commandTimeout;
    private final String primaryBranch;
    private final String fallbackBranch;
    private final double hardLateMultiplier;
    private final List<GradeBound> gradeBounds;

    public SettingsSpec(
            Path workspace,
            Duration commandTimeout,
            String primaryBranch,
            String fallbackBranch,
            double hardLateMultiplier,
            List<GradeBound> gradeBounds
    ) {
        this.workspace = workspace;
        this.commandTimeout = commandTimeout;
        this.primaryBranch = primaryBranch;
        this.fallbackBranch = fallbackBranch;
        this.hardLateMultiplier = hardLateMultiplier;
        List<GradeBound> sorted = new ArrayList<>(gradeBounds);
        sorted.sort(Comparator.comparingDouble(GradeBound::minPercent).reversed());
        this.gradeBounds = List.copyOf(sorted);
    }

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

    public Path workspace() {
        return workspace;
    }

    public Duration commandTimeout() {
        return commandTimeout;
    }

    public String primaryBranch() {
        return primaryBranch;
    }

    public String fallbackBranch() {
        return fallbackBranch;
    }

    public double hardLateMultiplier() {
        return hardLateMultiplier;
    }

    public List<GradeBound> gradeBounds() {
        return gradeBounds;
    }
}

