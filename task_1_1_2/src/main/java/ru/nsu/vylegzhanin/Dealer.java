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

}