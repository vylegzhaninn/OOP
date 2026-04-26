package vylegzhanin.task241.domain;

/**
 * Структура для хранения статистики прохождения тестов.
 *
 * @param passed  количество пройденных тестов
 * @param failed  количество упавших тестов
 * @param skipped количество пропущенных тестов
 */
public record TestStats(
    int passed,
    int failed,
    int skipped
) {}