package ru.nsu.vylegzhanin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/*Player тест */
class PlayerTest {

    @Test
    void testGetName() {
        Player p = new Player("Alice");
        assertEquals("Alice", p.getName());
    }

    @Test
    void testInitialHandIsEmpty() {
        Player p = new Player("Bob");
        assertTrue(p.getHand().getCards().isEmpty());
        assertEquals(0, p.getHand().getScore());
    }

    @Test
    void testTakeCardMovesFromDeckToHand() {
        Deck deck = new Deck(1);
        Player p = new Player("Test");
        // не можем проверить размер колоды напрямую, но рука пуста
        assertTrue(p.getHand().getCards().isEmpty());
        p.takeCard(deck);
        assertEquals(1, p.getHand().getCards().size());
        // и при этом deck уже уменьшается, т.е. не пуст, но выдаёт меньше
        assertFalse(deck.isEmpty());
    }
}