package ru.nsu.vylegzhanin;

import java.util.ArrayList;
import java.util.List;

/**
 * Рука — набор карт игрока или дилера. Содержит логику подсчёта очков.
 */
public final class Hand {

    private final List<Card> cards = new ArrayList<>();

    /**
     * Добавляет карту в руку.
     *
     * @param card карта для добавления
     */
    public void addCard(final Card card) {
        cards.add(card);
    }

    /**
     * Возвращает список карт в руке.
     *
     * @return список карт
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Подсчёт очков с учётом тузов (туз = 11 или 1).
     *
     * @return сумма очков
     */
    public int getScore() {
        int sum = 0;
        int aces = 0;
        for (Card c : cards) {
            sum += c.getValue();
            if (c.getValue() == 11) {
                aces++;
            }
        }
        while (sum > 21 && aces > 0) {
            sum -= 10;
            aces--;
        }
        return sum;
    }

    /**
     * Проверка, является ли комбинация блекджеком (2 карты, 21 очко).
     *
     * @return {@code true}, если блекджек, иначе {@code false}
     */
    public boolean isBlackjack() {
        return cards.size() == 2 && getScore() == 21;
    }

    /**
     * Проверка перебора.
     *
     * @return {@code true}, если перебор, иначе {@code false}
     */
    public boolean isBust() {
        return getScore() > 21;
    }

    @Override
    public String toString() {
        return cards.toString();
    }
}