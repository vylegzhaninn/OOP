package ru.nsu.vylegzhanin;

/**
 * Утилитный класс для вывода состояния игры и определения победителя.
 */
public class GameUtils {

    /**
     * Печатает начальные руки игрока и дилера, скрывая одну карту дилера.
     *
     * @param player игрок
     * @param dealer дилер
     */
    public static void printHandsInitial(Player player, Dealer dealer) {
        System.out.println("Ваши карты: "
                + player.getHand()
                + " => "
                + player.getHand().getScore());
        System.out.println("Карты дилера: ["
                + dealer.getHand().getCards().get(0)
                + ", <закрытая карта>]\n");
    }

    /**
     * Печатает финальные руки игрока и дилера с раскрытыми картами и подсчитанными очками.
     *
     * @param player игрок
     * @param dealer дилер
     */
    public static void printFinalHands(Player player, Dealer dealer) {
        System.out.println("Ваши карты: "
                + player.getHand()
                + " => "
                + player.getHand().getScore());
        System.out.println("Карты дилера: "
                + dealer.getHand()
                + " => "
                + dealer.getHand().getScore()
                + "\n");
    }

    /**
     * Определяет победителя, сравнивая очки игрока и дилера, и выводит результат.
     *
     * @param player игрок
     * @param dealer дилер
     */
    public static void determineWinner(Player player, Dealer dealer) {
        int p = player.getHand().getScore();
        int d = dealer.getHand().getScore();

        if (dealer.getHand().isBust()) {
            System.out.println("Дилер перебрал. Вы выиграли.");
        } else if (p > d) {
            System.out.println("Вы выиграли.");
        } else if (p < d) {
            System.out.println("Вы проиграли.");
        } else {
            System.out.println("Ничья.");
        }
    }
}
