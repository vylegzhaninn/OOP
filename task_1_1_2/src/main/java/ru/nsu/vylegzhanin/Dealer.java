package ru.nsu.vylegzhanin;

/**
 * Дилер. Имеет стратегию: тянет карты, пока сумма меньше 17.
 */
public final class Dealer extends Player {

    /**
     * Создаёт дилера с фиксированным именем.
     */
    public Dealer() {
        super("Дилер");
    }

    /**
     * Ход дилера: берет карты, пока счёт < 17.
     *
     * @param deck колода
     */
    public void play(final Deck deck) {
        while (getHand().getScore() < 17 && !getHand().isBust()) {
            takeCard(deck);
        }
    }
}