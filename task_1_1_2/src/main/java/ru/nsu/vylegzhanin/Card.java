package ru.nsu.vylegzhanin;

/**
 * Класс, представляющий одну игральную карту.
 * Содержит масть и номинал.
 */
public class Card {

    /** Масть карты. */
    private final Suit suit;

    /** Номинал карты. */
    private final Rank rank;

    /**
     * Создаёт карту с указанной мастью и номиналом.
     *
     * @param suit масть карты
     * @param rank номинал карты
     */
    public Card(final Suit suit, final Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    /**
     * Возвращает масть карты.
     *
     * @return масть карты
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Возвращает номинал карты.
     *
     * @return номинал карты
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Возвращает числовое значение карты,
     * которое используется для подсчёта очков.
     *
     * @return числовое значение карты
     */
    public int getValue() {
        return rank.getValue();
    }

    /**
     * Возвращает строковое представление карты.
     *
     * @return строковое представление карты в формате  
     *         {@code "<масть> (<значение>)"}
     */
    @Override
    public String toString() {
        return suit.getName() + " (" + rank.getValue() + ")";
    }
}
