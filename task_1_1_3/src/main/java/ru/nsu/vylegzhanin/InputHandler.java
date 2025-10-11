package ru.nsu.vylegzhanin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Класс для обработки ввода выражений.
 */
public class InputHandler {
    
    /**
     * Спрашивает у пользователя, откуда читать выражение, и возвращает его.
     */
    static String readExpressionSource(Scanner scanner) throws IOException {
        while (true) {
            System.out.println("Откуда считать выражение?");
            System.out.println("1: С консоли");
            System.out.println("2: Из файла");
            System.out.println("3: Выход");
            System.out.print("Выберите опцию: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.print("Введите выражение: ");
                    return scanner.nextLine();
                case "2":
                    System.out.print("Введите путь к файлу: ");
                    String path = scanner.nextLine();
                    return Files.lines(Paths.get(path))
                            .filter(s -> !s.trim().isEmpty())
                            .findFirst()
                            .orElseThrow(() -> new IOException(
                                    "Файл пуст или не содержит выражений."));
                case "3":
                    return null;
                default:
                    System.out.println("Неверная опция, попробуйте снова.");
            }
        }
    }
}
