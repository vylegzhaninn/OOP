package ru.nsu.vylegzhanin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    void testNewDeckNotEmpty() {
        Deck deck = new Deck(1);
        assertFalse(deck.isEmpty());
    }

    @Test
    void testDrawCardReturnsNonNull() {
        Deck deck = new Deck(1);
        Card c = deck.drawCard();
        assertNotNull(c);
        assertFalse(deck.isEmpty());
    }

    @Test
    void testDeckBecomesEmptyAfter52Draws() {
        Deck deck = new Deck(1);
        for (int i = 0; i < 52; i++) {
            deck.drawCard();
        }
        assertTrue(deck.isEmpty());
    }

    @Test
    void testDrawBeyondEmptyThrows() {
        Deck deck = new Deck(1);
        for (int i = 0; i < 52; i++) {
            deck.drawCard();
        }
        assertTrue(deck.isEmpty());
        assertThrows(IndexOutOfBoundsException.class, deck::drawCard);
    }

    @Test
    void testMultipleDecksCountDraws() {
        int decksCount = 3;
        Deck deck = new Deck(decksCount);
        int count = 0;
        while (!deck.isEmpty()) {
            deck.drawCard();
            count++;
        }
        assertEquals(decksCount * 52, count);
    }
}