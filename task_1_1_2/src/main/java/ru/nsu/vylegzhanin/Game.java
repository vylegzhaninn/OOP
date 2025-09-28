package ru.nsu.vylegzhanin;

/**
 * Главный класс приложения — игра «Блэкджек».
 */
public final class Game {
    /**
     * Точка входа: раздаёт карты, обрабатывает ходы игрока и дилера и выводит результат.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        System.out.println("Добро пожаловать в Блэкджек!");
        Deck deck = new Deck(InputUtils.askCountDecks());
        Player player = new Player("Вы");
        Dealer dealer = new Dealer();

        // начальная раздача
        player.takeCard(deck);
        dealer.takeCard(deck);
        player.takeCard(deck);
        dealer.takeCard(deck);

        System.out.println("Дилер раздал карты");
        GameUtils.printHandsInitial(player, dealer);

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
        InputUtils.playerTurn(player, dealer, deck);
        if (player.getHand().isBust()) {
            System.out.println("\nВы перебрали. Вы проиграли.");
            return;
        }

        System.out.println("\nХод дилера\n-------");
        InputUtils.dealerTurn(player, dealer, deck);
        GameUtils.determineWinner(player, dealer);
    }
}