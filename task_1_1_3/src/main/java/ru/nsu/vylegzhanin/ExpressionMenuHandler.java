package ru.nsu.vylegzhanin;

import java.util.Scanner;

/**
 * Класс для обработки меню действий с выражениями.
 */
public class ExpressionMenuHandler {
    
    /**
     * Показывает меню действий с выражением и обрабатывает выбор пользователя.
     */
    public static void handleExpressionMenu(Scanner scanner, Expression expression) {
        while (true) {
            System.out.println("\nЧто сделать с выражением?");
            System.out.println("1: Вывести на экран (print)");
            System.out.println("2: Вычислить (eval)");
            System.out.println("3: Дифференцировать (derivative)");
            System.out.println("4: Упростить (simplify)");
            System.out.println("5: Ввести новое выражение");
            System.out.println("6: Выход");
            System.out.print("Выберите опцию: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("Результат: " + expression.print());
                    break;
                case "2":
                    System.out.print("Введите значения переменных (например: x=5; y=10): ");
                    String vars = scanner.nextLine();
                    try {
                        int result = expression.eval(vars);
                        System.out.println("Результат вычисления: " + result);
                    } catch (Exception e) {
                        System.err.println("Ошибка при вычислении: " + e.getMessage());
                    }
                    break;
                case "3":
                    System.out.print("По какой переменной дифференцировать? ");
                    String var = scanner.nextLine();
                    Expression derivative = expression.derivative(var);
                    System.out.println("Производная: " + derivative.print());
                    break;
                case "4":
                    try {
                        Expression simplified = expression.simplify();
                        System.out.println("Исходное выражение: " + expression.print());
                        System.out.println("Упрощенное выражение: " + simplified.print());
                        
                        // Проверяем, действительно ли выражение упростилось
                        if (expression.print().equals(simplified.print())) {
                            System.out.println("Выражение уже находится в упрощенном виде.");
                        } else {
                            System.out.println("Выражение успешно упрощено!");
                        }
                    } catch (Exception e) {
                        System.err.println("Ошибка при упрощении: " + e.getMessage());
                    }
                    break;
                case "5":
                    return;
                case "6":
                    System.out.println("Программа завершена.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Неверная опция, попробуйте снова.");
            }
        }
    }
}
