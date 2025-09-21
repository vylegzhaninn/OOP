package ru.nsu.vylegzhanin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс, представляющий колоду карт.
 * Поддерживает создание одной или нескольких колод и операции с ними.
 */
public class Deck {

    /** Список карт в колоде. */
    private final List<Card> cards = new ArrayList<>();

    /**
     * Создаёт колоду с заданным количеством стандартных колод.
     * При создании карты перемешиваются.
     *
     * @param decksCount количество стандартных колод (обычно 1)
     */
    public Deck(final int decksCount) {
        for (int d = 0; d < decksCount; d++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    cards.add(new Card(suit, rank));
                }
            }
        }
        Collections.shuffle(cards);
    }

    /**
     * Берёт верхнюю карту из колоды.
     *
     * @return карта с вершины колоды
     */
    public Card drawCard() {
        return cards.remove(0);
    }

    /**
     * Проверяет, пуста ли колода.
     *
     * @return {@code true}, если колода пуста, иначе {@code false}
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
