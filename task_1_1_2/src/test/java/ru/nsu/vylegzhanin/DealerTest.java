package ru.nsu.vylegzhanin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DealerTest {
    @Test
    void testDealerInheritsPlayerFunctionality() {
        Deck deck = new Deck(1);
        Dealer d = new Dealer();
        assertTrue(d.getHand().getCards().isEmpty());
        d.takeCard(deck);
        assertEquals(1, d.getHand().getCards().size());
    }
}