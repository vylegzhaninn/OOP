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
) {
    /** Пустая статистика (все счётчики равны нулю). Используется как нейтральный элемент в {@link #add}. */
    public static final TestStats EMPTY = new TestStats(0, 0, 0);

    /**
     * Возвращает новую статистику, в которой счетчики поэлементно сложены с переданной.
     *
     * @param other другая статистика
     * @return сумма двух статистик
     */
    public TestStats add(TestStats other) {
        return new TestStats(
            passed + other.passed,
            failed + other.failed,
            skipped + other.skipped
        );
    }
}