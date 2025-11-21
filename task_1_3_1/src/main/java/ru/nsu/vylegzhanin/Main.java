package ru.nsu.vylegzhanin;

import java.io.IOException;

/**
 * Точка входа приложения, запускающая поиск подстроки и печать её позиций.
 */
public final class Main {
    /**
     * Запускает поиск подстроки в файле {@code input.txt} и выводит позиции в стандартный вывод.
     *
     * @param args аргументы командной строки (игнорируются)
     * @throws IOException при ошибке чтения входного файла
     */
    public static void main(String[] args) throws IOException {
        FindStr.find("input.txt", "бра");
    }
}
