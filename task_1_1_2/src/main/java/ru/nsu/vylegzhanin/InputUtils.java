package ru.nsu.vylegzhanin;

import java.util.Scanner;

/**
 * Утилитный класс для обработки ввода пользователя.
 */
public class InputUtils {

    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Запрашивает у пользователя количество колод от 1 до 8.
     *
     * @return количество колод
     */
    public static int askCountDecks() {
        while (true) {
            System.out.print("Введите количество колод (1-8): ");
            String line = SCANNER.nextLine().trim();
            if (line.matches("[1-8]")) {
                return Integer.parseInt(line);
            } else {
                System.out.println("Некорректный ввод, введите число от 1 до 8.");
            }
        }
    }

    /**
     * Обрабатывает ход игрока: взять карту или остановиться.
     *
     * @param player игрок
     * @param dealer дилер
     * @param deck   колода
     */
    public static void playerTurn(Player player, Dealer dealer, Deck deck) {
        while (true) {
            System.out.print("Введите \"1\" чтобы взять карту, или \"0\" чтобы остановиться: ");
            String cmd = SCANNER.nextLine().trim();
            if (cmd.equals("1")) {
                player.takeCard(deck);
                System.out.println("Вы открыли карту "
                        + player.getHand().getCards().get(player.getHand().getCards().size() - 1));
                GameUtils.printHandsInitial(player, dealer);
                if (player.getHand().isBust()) {
                    return;
                }
            } else if (cmd.equals("0")) {
                return;
            } else {
                System.out.println("Некорректный ввод, введите 1 или 0.");
            }
        }
    }

    /**
     * Ход дилера: открывает карту и берёт новые, пока &lt;17 очков.
     *
     * @param player игрок
     * @param dealer дилер
     * @param deck   колода
     */
    public static void dealerTurn(Player player, Dealer dealer, Deck deck) {
        System.out.println("Дилер открывает закрытую карту "
                + dealer.getHand().getCards().get(1));
        GameUtils.printFinalHands(player, dealer);
        while (dealer.getHand().getScore() < 17) {
            dealer.takeCard(deck);
            Card last = dealer.getHand().getCards()
                    .get(dealer.getHand().getCards().size() - 1);
            System.out.println("Дилер открывает карту " + last);
            GameUtils.printFinalHands(player, dealer);
            if (dealer.getHand().isBust()) {
                break;
            }
        }
    }
}
