package vylegzhanin.task241.domain;

import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Глобальные настройки для оценки и скачивания.
 */
public final class SettingsSpec {
    private final Path workspace;
    private final Duration commandTimeout;
    private final String primaryBranch;
    private final String fallbackBranch;
    private final double hardLateMultiplier;
    private final List<GradeBound> gradeBounds;

    /**
     * Создает настройки курса.
     *
     * @param workspace          путь до локальной директории для клонирования и работы с репозиториями
     * @param commandTimeout     таймаут на выполнение внешних команд (git, gradle и т.д.)
     * @param primaryBranch      основная ветка (main)
     * @param fallbackBranch     резервная ветка (master)
     * @param hardLateMultiplier множитель баллов при жестком опоздании сдачи
     * @param gradeBounds        список границ для выставления оценок
     */
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

    /**
     * Возвращает настройки по умолчанию.
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

    /**
     * @return путь до рабочей директории
     */
    public Path workspace() {
        return workspace;
    }

    /**
     * @return таймаут на выполнение команд
     */
    public Duration commandTimeout() {
        return commandTimeout;
    }

    /**
     * @return основная ветка (например, main)
     */
    public String primaryBranch() {
        return primaryBranch;
    }

    /**
     * @return резервная ветка (например, master)
     */
    public String fallbackBranch() {
        return fallbackBranch;
    }

    /**
     * @return множитель для баллов при сдаче после хард дедлайна
     */
    public double hardLateMultiplier() {
        return hardLateMultiplier;
    }

    /**
     * @return список границ для оценок
     */
    public List<GradeBound> gradeBounds() {
        return gradeBounds;
    }
}
