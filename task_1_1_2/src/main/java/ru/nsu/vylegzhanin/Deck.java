package ru.nsu.vylegzhanin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards = new ArrayList<>();

    public Deck(int decksCount) {
        String[] suits = {"Пики","Черви","Бубны","Трефы"};
        String[] ranks = {"2","3","4","5","6","7","8","9","10","J","Q","K","A"};
        for (int d = 0; d < decksCount; d++) {
            for (String suit : suits)
                for (String rank : ranks)
                    cards.add(new Card(suit, rank));
        }
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        return cards.remove(0);
    }
}
