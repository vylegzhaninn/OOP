package ru.nsu.vylegzhanin;

/**
 * Перечисление, представляющее номиналы игральных карт.
 * Содержит символ для отображения и числовое значение для подсчёта очков.
 */
public enum Rank {

    /** Двойка. */
    TWO("2", 2),

    /** Тройка. */
    THREE("3", 3),

    /** Четвёрка. */
    FOUR("4", 4),

    /** Пятёрка. */
    FIVE("5", 5),

    /** Шестёрка. */
    SIX("6", 6),

    /** Семёрка. */
    SEVEN("7", 7),

    /** Восьмёрка. */
    EIGHT("8", 8),

    /** Девятка. */
    NINE("9", 9),

    /** Десятка. */
    TEN("10", 10),

    /** Валет. */
    JACK("J", 10),

    /** Дама. */
    QUEEN("Q", 10),

    /** Король. */
    KING("K", 10),

    /** Туз. */
    ACE("A", 11);

    /** Символ номинала карты. */
    private final String symbol;

    /** Числовое значение номинала карты. */
    private final int value;

    /**
     * Создаёт номинал карты.
     *
     * @param symbol символ для отображения
     * @param value  числовое значение карты
     */
    Rank(final String symbol, final int value) {
        this.symbol = symbol;
        this.value = value;
    }

    /**
     * Возвращает символ номинала карты.
     *
     * @return символ номинала
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Возвращает числовое значение номинала карты.
     *
     * @return числовое значение
     */
    public int getValue() {
        return value;
    }
}
