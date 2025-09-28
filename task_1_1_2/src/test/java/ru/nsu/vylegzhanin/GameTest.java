package ru.nsu.vylegzhanin;

import org.junit.jupiter.api.Test;
import java.io.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTest {

    @Test
    void testGameMain_printsWelcomeAndDeal() {
        // Подготовка входных данных: 1 колода, сразу пас
        String input = "1\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Перехват вывода в консоль
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outStream));

        // Запускаем main
        Game.main(new String[0]);

        String output = outStream.toString();
        assertTrue(output.contains("Добро пожаловать в Блэкджек!"),
                   "Ожидается приветственное сообщение");
        assertTrue(output.contains("Дилер раздал карты"),
                   "Ожидается сообщение о начальной раздаче карт");
    }
}