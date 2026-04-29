package vylegzhanin.task241.domain.config;

import java.time.LocalDate;

/**
 * Описание задания в курсе.
 *
 * @param id           уникальный идентификатор задания
 * @param title        заголовок/название задания
 * @param maxPoints    максимальное количество баллов за задание
 * @param softDeadline дата мягкого дедлайна (после которого баллы могут снижаться)
 * @param hardDeadline дата жесткого дедлайна (после которого возможны дополнительные штрафы или нули)
 */
public record TaskSpec(
    String id,
    String title,
    double maxPoints,
    LocalDate softDeadline,
    LocalDate hardDeadline
) {
}
