package ru.nsu.vylegzhanin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Колода карт. Поддерживает несколько стандартных 52-картных колод.
 */
public final class Deck {

    private final List<Card> cards = new ArrayList<>();
    private int topIndex = 0;

    /**
     * Создаёт колоду из указанного числа 52-картных колод и перемешивает её.
     *
     * @param decksCount число колод
     */
    public Deck(final int decksCount) {
        for (int d = 0; d < decksCount; d++) {
            for (Card.Suit suit : Card.Suit.values()) {
                for (Card.Rank rank : Card.Rank.values()) {
                    cards.add(new Card(suit, rank));
                }
            }
        }
        shuffle();
    }

    /** Перемешать оставшиеся карты. */
    public void shuffle() {
        Collections.shuffle(cards);
        topIndex = 0;
    }

    /**
     * Взять карту с верха колоды.
     *
     * @return карта
     * @throws IllegalStateException если колода пуста
     */
    public Card drawCard() {
        if (topIndex >= cards.size()) {
            throw new IllegalStateException("Deck is empty");
        }
        return cards.get(topIndex++);
    }

    /**
     * Количество оставшихся карт в колоде.
     *
     * @return число карт
     */
    public int remaining() {
        return cards.size() - topIndex;
    }
}