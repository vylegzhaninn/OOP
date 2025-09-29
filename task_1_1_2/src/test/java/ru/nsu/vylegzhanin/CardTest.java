package ru.nsu.vylegzhanin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Юнит-тесты для класса Card.
 */
class CardTest {
    @Test
    void testCardValue() {
        Card card = new Card(Suit.SPADES, Rank.ACE);
        assertEquals(11, card.getValue());
    }
}
