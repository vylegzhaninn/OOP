package ru.nsu.vylegzhanin;

/**
 * Перечисление, представляющее масти игральных карт.
 */
public enum Suit {

    /** Пики. */
    SPADES("Пики"),

    /** Черви. */
    HEARTS("Черви"),

    /** Бубны. */
    DIAMONDS("Бубны"),

    /** Трефы. */
    CLUBS("Трефы");

    /** Название масти на русском языке. */
    private final String name;

    /**
     * Создаёт масть карты.
     *
     * @param name название масти
     */
    Suit(final String name) {
        this.name = name;
    }

    /**
     * Возвращает название масти на русском языке.
     *
     * @return название масти
     */
    public String getName() {
        return name;
    }
}
