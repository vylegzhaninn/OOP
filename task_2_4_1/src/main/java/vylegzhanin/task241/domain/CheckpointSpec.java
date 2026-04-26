package vylegzhanin.task241.domain;

import java.time.LocalDate;

/**
 * Описание контрольной точки курса.
 *
 * @param name название контрольной точки
 * @param date дата, когда проходит или заканчивается контрольная точка
 */
public record CheckpointSpec(
    String name,
    LocalDate date
) {
}
