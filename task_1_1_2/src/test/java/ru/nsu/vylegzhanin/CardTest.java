package ru.nsu.vylegzhanin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardTest {
    @Test
    void testCardValue() {
        Card card = new Card(Suit.SPADES, Rank.ACE);
        assertEquals(11, card.getValue());
    }
}
