package ru.nsu.vylegzhanin;

public class Card {
    private final String suit;
    private final String rank; 

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public String showCard() {
        return rank + " " + suit;
    }
}