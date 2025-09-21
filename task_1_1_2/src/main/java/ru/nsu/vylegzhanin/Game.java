package ru.nsu.vylegzhanin;

import java.util.Scanner;

/**
 * Основной класс игры «Блэкджек».
 * Содержит точку входа и методы управления игровым процессом.
 */
public final class Game {

    /** Сканер для ввода с консоли. */
    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(final String[] args) {
        System.out.println("Добро пожаловать в Блэкджек!");
        final Deck deck = new Deck(1);
        final Player player = new Player("Вы");
        final Dealer dealer = new Dealer();

        // Раздача стартовых карт
        player.takeCard(deck);
        dealer.takeCard(deck);
        player.takeCard(deck);
        dealer.takeCard(deck);

        System.out.println("Дилер раздал карты");
        printHandsInitial(player, dealer);

        if (player.getHand().isBlackjack()) {
            System.out.println("У вас блэкджек! Вы выиграли.");
            System.out.println("Ваши карты: " + player.getHand() + " => "
                    + player.getHand().getScore());
            return;
        }
        if (dealer.getHand().isBlackjack()) {
            System.out.println("У дилера блэкджек! Вы проиграли.");
            System.out.println("Карты дилера: " + dealer.getHand() + " => "
                    + dealer.getHand().getScore());
            return;
        }

        System.out.println("\nВаш ход\n-------");
        // Ход игрока
        playerTurn(player, dealer, deck);

        if (player.getHand().isBust()) {
            System.out.println("\nВы перебрали. Вы проиграли.");
            return;
        }

        // Ход дилера
        System.out.println("\nХод дилера\n-------");

        dealerTurn(player, dealer, deck);

        determineWinner(player, dealer);
    }

    /**
     * Выполняет ход игрока: запрашивает ввод и даёт карту или завершает ход.
     *
     * @param player игрок
     * @param dealer дилер
     * @param deck   колода карт
     */
    private static void playerTurn(final Player player, final Dealer dealer, final Deck deck) {
        while (true) {
            System.out.println("\nВведите \"1\" чтобы взять карту, или \"0\" чтобы остановиться.");
            final String line = SCANNER.nextLine().trim();
            if ("1".equals(line)) {
                player.takeCard(deck);
                Card lastCard = player.getHand().getCards()
                        .get(player.getHand().getCards().size() - 1);
                System.out.println("Вы открыли карту " + lastCard);
                printHandsInitial(player, dealer);
                if (player.getHand().isBust()) {
                    break;
                }
            } else if ("0".equals(line)) {
                break;
            } else {
                System.out.println("Некорректный ввод, введите 1 или 0.");
            }
        }
    }

    /**
     * Выполняет ход дилера: открывает закрытую карту и берёт карты, пока сумма < 17.
     *
     * @param player игрок
     * @param dealer дилер
     * @param deck   колода карт
     */
    private static void dealerTurn(final Player player, final Dealer dealer, final Deck deck) {
        Card hiddenCard = dealer.getHand().getCards().get(1);
        System.out.println("Дилер открывает закрытую карту " + hiddenCard);
        printFinalHands(player, dealer);

        while (dealer.getHand().getScore() < 17) {
            dealer.takeCard(deck);
            Card lastCard = dealer.getHand().getCards()
                    .get(dealer.getHand().getCards().size() - 1);
            System.out.println("Дилер открывает карту " + lastCard);
            printFinalHands(player, dealer);
            if (dealer.getHand().isBust()) {
                break;
            }
        }
    }

    /**
     * Печатает руки игрока и дилера после начальной раздачи (карта дилера закрыта).
     *
     * @param player игрок
     * @param dealer дилер
     */
    private static void printHandsInitial(final Player player, final Dealer dealer) {
        System.out.println("Ваши карты: " + player.getHand() + " => "
                + player.getHand().getScore());
        System.out.println("Карты дилера: [" + dealer.getHand().getCards().get(0)
                + ", <закрытая карта>]");
    }

    /**
     * Печатает руки игрока и дилера с полным раскрытием карт дилера.
     *
     * @param player игрок
     * @param dealer дилер
     */
    private static void printFinalHands(final Player player, final Dealer dealer) {
        System.out.println("Ваши карты: " + player.getHand() + " => "
                + player.getHand().getScore());
        System.out.println("Карты дилера: " + dealer.getHand() + " => "
                + dealer.getHand().getScore() + "\n");
    }

    /**
     * Определяет победителя и выводит результат.
     *
     * @param player игрок
     * @param dealer дилер
     */
    private static void determineWinner(final Player player, final Dealer dealer) {
        final int p = player.getHand().getScore();
        final int d = dealer.getHand().getScore();
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