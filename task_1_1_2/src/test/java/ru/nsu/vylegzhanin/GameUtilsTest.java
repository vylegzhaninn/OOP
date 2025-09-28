package ru.nsu.vylegzhanin;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/**
 * Юнит-тесты для методов GameUtils: проверяем вывод на консоль
 * при печати рук и определении победителя.
 */
public class GameUtilsTest {
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream out;

    @BeforeEach
    void setUp() {
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testPrintHandsInitial() {
        Player player = new Player("p");
        Dealer dealer = new Dealer();
        // наполняем руки по 2 карты
        player.getHand().getCards().addAll(List.of(
            new Card(Suit.HEARTS, Rank.FIVE),
            new Card(Suit.CLUBS, Rank.TEN)
        ));  // сумма 15
        dealer.getHand().getCards().addAll(List.of(
            new Card(Suit.DIAMONDS, Rank.FOUR),
            new Card(Suit.SPADES, Rank.SEVEN)
        ));

        GameUtils.printHandsInitial(player, dealer);
        String console = out.toString();
        assertTrue(console.contains("=> 15"),
                   "Должна выводиться сумма очков игрока (15)");
        assertTrue(console.contains("<закрытая карта>"),
                   "Вторая карта дилера должна быть скрыта");
    }

    @Test
    void testPrintFinalHands() {
        Player player = new Player("p");
        Dealer dealer = new Dealer();
        player.getHand().getCards().addAll(List.of(
            new Card(Suit.CLUBS, Rank.ACE),
            new Card(Suit.HEARTS, Rank.KING)
        )); // blackjack 21
        dealer.getHand().getCards().addAll(List.of(
            new Card(Suit.SPADES, Rank.TEN),
            new Card(Suit.DIAMONDS, Rank.SEVEN)
        )); // сумма 17

        GameUtils.printFinalHands(player, dealer);
        String console = out.toString();
        assertTrue(console.contains("=> 21"),
                   "Должна выводиться сумма очков игрока (21)");
        assertTrue(console.contains("=> 17"),
                   "Должна выводиться сумма очков дилера (17)");
    }

    @Test
    void testDetermineWinnerDealerBust() {
        Player player = new Player("p");
        Dealer dealer = new Dealer();
        dealer.getHand().getCards().addAll(List.of(
            new Card(Suit.SPADES, Rank.QUEEN),
            new Card(Suit.HEARTS, Rank.KING),
            new Card(Suit.DIAMONDS, Rank.TWO)
        )); // 10+10+2 = 22 → bust
        player.getHand().getCards().addAll(List.of(
            new Card(Suit.CLUBS, Rank.TEN),
            new Card(Suit.HEARTS, Rank.SEVEN)
        )); // 17

        GameUtils.determineWinner(player, dealer);
        String console = out.toString();
        assertTrue(console.contains("Дилер перебрал"),
                   "При bust дилера должно быть соответствующее сообщение");
        assertTrue(console.contains("Вы выиграли"),
                   "При bust дилера игрок должен выигрывать");
    }

    @Test
    void testDetermineWinnerPlayerGreater() {
        Player player = new Player("p");
        Dealer dealer = new Dealer();
        player.getHand().getCards().addAll(List.of(
            new Card(Suit.HEARTS, Rank.NINE),
            new Card(Suit.CLUBS, Rank.EIGHT)
        )); // 17
        dealer.getHand().getCards().addAll(List.of(
            new Card(Suit.SPADES, Rank.TEN),
            new Card(Suit.DIAMONDS, Rank.SIX)
        )); // 16

        GameUtils.determineWinner(player, dealer);
        String console = out.toString();
        assertTrue(console.contains("Вы выиграли"),
                   "При бо́льшем счёте игрока должно быть сообщение об выигрыше");
    }

    @Test
    void testDetermineWinnerPlayerLess() {
        Player player = new Player("p");
        Dealer dealer = new Dealer();
        player.getHand().getCards().addAll(List.of(
            new Card(Suit.HEARTS, Rank.EIGHT),
            new Card(Suit.CLUBS, Rank.SEVEN)
        )); // 15
        dealer.getHand().getCards().addAll(List.of(
            new Card(Suit.SPADES, Rank.NINE),
            new Card(Suit.DIAMONDS, Rank.NINE)
        )); // 18

        GameUtils.determineWinner(player, dealer);
        String console = out.toString();
        assertTrue(console.contains("Вы проиграли"),
                   "При бо́льшем счёте дилера должно быть сообщение о проигрыше");
    }

    @Test
    void testDetermineWinnerTie() {
        Player player = new Player("p");
        Dealer dealer = new Dealer();
        player.getHand().getCards().addAll(List.of(
            new Card(Suit.CLUBS, Rank.TEN),
            new Card(Suit.HEARTS, Rank.SEVEN)
        )); // 17
        dealer.getHand().getCards().addAll(List.of(
            new Card(Suit.SPADES, Rank.NINE),
            new Card(Suit.DIAMONDS, Rank.EIGHT)
        )); // 17

        GameUtils.determineWinner(player, dealer);
        String console = out.toString();
        assertTrue(console.contains("Ничья"),
                   "При равном счёте должно быть сообщение «Ничья.»");
    }
}