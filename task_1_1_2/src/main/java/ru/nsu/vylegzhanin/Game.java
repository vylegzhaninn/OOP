package ru.nsu.vylegzhanin;

import java.util.Scanner;

/**
 * Основной класс игры. Запускает консольный раунд блэкджека.
 */
public final class Game {

    private static final Scanner SCANNER = new Scanner(System.in);

    private Game() {
        // утилитный класс
    }

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
            return;
        }
        if (dealer.getHand().isBlackjack()) {
            System.out.println("У дилера блэкджек! Вы проиграли.");
            return;
        }

        // Ход игрока
        playerTurn(player, dealer, deck);

        if (player.getHand().isBust()) {
            System.out.println("Вы перебрали. Вы проиграли.");
            return;
        }

        // Ход дилера
        System.out.println("Ход дилера");
        dealer.play(deck);
        printFinalHands(player, dealer);

        determineWinner(player, dealer);
    }

    private static void playerTurn(final Player player, final Dealer dealer, final Deck deck) {
        while (true) {
            System.out.println("Ваши карты: " + player.getHand() + " > " + player.getHand().getScore());
            System.out.println("Карты дилера: [" + dealer.getHand().getCards().get(0) + ", <закрытая карта>]");
            System.out.println("Введите \"1\" чтобы взять карту, или \"0\" чтобы остановиться.");
            final String line = SCANNER.nextLine().trim();
            if ("1".equals(line)) {
                player.takeCard(deck);
                System.out.println("Вы открыли карту " + player.getHand().getCards()
                        .get(player.getHand().getCards().size() - 1));
                if (player.getHand().isBust()) {
                    System.out.println("Ваши карты: " + player.getHand() + " > " + player.getHand().getScore());
                    break;
                }
            } else if ("0".equals(line)) {
                break;
            } else {
                System.out.println("Некорректный ввод, введите 1 или 0.");
            }
        }
    }

    private static void printHandsInitial(final Player player, final Dealer dealer) {
        System.out.println("Ваши карты: " + player.getHand() + " > " + player.getHand().getScore());
        System.out.println("Карты дилера: [" + dealer.getHand().getCards().get(0) + ", <закрытая карта>]");
    }

    private static void printFinalHands(final Player player, final Dealer dealer) {
        System.out.println("Ваши карты: " + player.getHand() + " > " + player.getHand().getScore());
        System.out.println("Карты дилера: " + dealer.getHand() + " > " + dealer.getHand().getScore());
    }

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

