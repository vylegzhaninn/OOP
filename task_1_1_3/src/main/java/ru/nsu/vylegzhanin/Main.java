package ru.nsu.vylegzhanin;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                try {
                    String expressionString = InputHandler.readExpressionSource(scanner);
                    if (expressionString == null) {
                        break;
                    }

                    Expression expression = Parse.parse(expressionString);
                    System.out.println("Выражение успешно распознано: " + expression.print());

                    ExpressionMenuHandler.handleExpressionMenu(scanner, expression);

                } catch (IOException e) {
                    System.err.println("Ошибка чтения файла: " + e.getMessage());
                } catch (IllegalArgumentException | ArithmeticException e) {
                    System.err.println("Ошибка обработки выражения: " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
                }

                System.out.println("\n-----------------------------------\n");
            }
        }
        System.out.println("Программа завершена.");
    }
}