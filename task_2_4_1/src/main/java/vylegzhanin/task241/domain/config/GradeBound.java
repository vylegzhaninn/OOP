package vylegzhanin.task241.domain.config;

/**
 * Ограничение (граница) для конвертации процентов в оценку.
 *
 * @param grade      текстовое представление оценки (например, "A", "B", "C")
 * @param minPercent минимальный процент баллов для получения этой оценки
 */
public record GradeBound(
    String grade,
    double minPercent
) {
}
