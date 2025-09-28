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

    /**
     * Возвращает имя игрока.
     *
     * @return имя игрока
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает руку игрока.
     *
     * @return рука игрока
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Добавляет карту игроку из колоды.
     *
     * @param deck колода, из которой берётся карта
     */
    public void takeCard(final Deck deck) {
        hand.addCard(deck.drawCard());
    }
}