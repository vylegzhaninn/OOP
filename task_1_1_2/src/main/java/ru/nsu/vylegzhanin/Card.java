package ru.nsu.vylegzhanin;

/**
 * Модель карты: масть и ранк. Метод получения значения для блэкджека.
 */
public final class Card {

    /** Масть карты. */
    public enum Suit {
        PIKI("Пики"), CHERVI("Черви"), BUBNY("Бубны"), TREFY("Трефы");

        private final String name;

        Suit(final String nameValue) {
            name = nameValue;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /** Ранг (номинал) карты. */
    public enum Rank {
        TWO("2", 2), THREE("3", 3), FOUR("4", 4), FIVE("5", 5), SIX("6", 6), SEVEN("7", 7),
        EIGHT("8", 8), NINE("9", 9), TEN("10", 10), JACK("J", 10), QUEEN("Q", 10), KING("K", 10),
        ACE("A", 11);

        private final String label;
        private final int value;

        Rank(final String labelValue, final int valueValue) {
            label = labelValue;
            value = valueValue;
        }

        @Override
        public String toString() {
            return label;
        }

        /**
         * Базовое числовое значение карты (туз = 11 по умолчанию).
         *
         * @return значение карты
         */
        public int getValue() {
            return value;
        }
    }

    private final Suit suit;
    private final Rank rank;

    /**
     * Конструктор карты.
     *
     * @param cardSuit масть
     * @param cardRank ранк
     */
    public Card(final Suit cardSuit, final Rank cardRank) {
        suit = cardSuit;
        rank = cardRank;
    }

    /**
     * Возвращает базовое значение карты для подсчёта очков (туз = 11).
     *
     * @return значение карты
     */
    public int getValue() {
        return rank.getValue();
    }

    @Override
    public String toString() {
        return rank + " " + suit + " (" + getValue() + ")";
    }
}