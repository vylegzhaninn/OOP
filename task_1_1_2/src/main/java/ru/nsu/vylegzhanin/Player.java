package ru.nsu.vylegzhanin;

/**
 * Игрок.
 */
public class Player {

    private final String name;
    private final Hand hand = new Hand();

    /**
     * Конструктор игрока.
     *
     * @param playerName имя
     */
    public Player(final String playerName) {
        name = playerName;
    }

    /** Возвращает имя игрока. */
    public String getName() {
        return name;
    }

    /** Возвращает руку игрока. */
    public Hand getHand() {
        return hand;
    }

    /** Добавляет карту игроку из колоды. */
    public void takeCard(final Deck deck) {
        hand.addCard(deck.drawCard());
    }
}
