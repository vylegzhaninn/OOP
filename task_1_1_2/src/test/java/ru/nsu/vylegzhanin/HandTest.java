package ru.nsu.vylegzhanin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/*Hand тест */
class HandTest {

    @Test
    void testGetScoreWithoutAces() {
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.CLUBS,   Rank.TWO));   // 2
        hand.addCard(new Card(Suit.HEARTS,  Rank.FIVE));  // 5
        hand.addCard(new Card(Suit.SPADES,  Rank.NINE));  // 9
        assertEquals(16, hand.getScore());
        assertFalse(hand.isBlackjack());
        assertFalse(hand.isBust());
    }

    @Test
    void testGetScoreWithAces() {
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.CLUBS,   Rank.ACE));   // 11 or 1
        hand.addCard(new Card(Suit.HEARTS,  Rank.ACE));   // 11 or 1
        hand.addCard(new Card(Suit.DIAMONDS,Rank.NINE));  // 9
        // один туз = 11 + 1 + 9 = 21
        assertEquals(21, hand.getScore());
        assertFalse(hand.isBust());
    }

    @Test
    void testBlackjackDetection() {
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.SPADES,  Rank.ACE));   // 11
        hand.addCard(new Card(Suit.HEARTS, Rank.KING));   // 10
        assertTrue(hand.isBlackjack());
        assertEquals(21, hand.getScore());
    }

    @Test
    void testBust() {
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.CLUBS,   Rank.TEN));
        hand.addCard(new Card(Suit.DIAMONDS,Rank.NINE));
        hand.addCard(new Card(Suit.SPADES,  Rank.THREE));
        assertTrue(hand.isBust());
        assertTrue(hand.getScore() > 21);
    }

    @Test
    void testToStringShowsCards() {
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.SPADES, Rank.TWO));
        hand.addCard(new Card(Suit.CLUBS,  Rank.THREE));
        String s = hand.toString();
        assertTrue(s.contains("Пики") || s.contains("Трефы"));  // любая карта
        assertTrue(s.contains("("));
        assertTrue(s.contains(")"));
    }
}