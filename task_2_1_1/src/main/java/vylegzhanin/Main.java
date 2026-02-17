package vylegzhanin;

import vylegzhanin.solutions.ParallelSolution;
import vylegzhanin.solutions.SequentialSolution;
import vylegzhanin.solutions.StreamSolution;

import java.util.Arrays;

/**
 * Демонстрационный файл.
 *
 * @author Данил Колбасенко
 */
public class Main {

    /**
     * Демонстрационный метод: запускает три варианта поиска простых чисел в массиве
     * и печатает ответ и время выполнения каждого варианта.
     */
    static void main() throws InterruptedException {
        int[] arr = new int[1_000_000];
        Arrays.fill(arr, 13);
        arr[999_999] = 8;

        ContainCompound solution;

        for (int i = 0; i < 10; i++) {
            int counter = 1;
            while (counter <= 3) {
                solution = switch (counter) {
                    case 1 -> new SequentialSolution();
                    case 2 -> new ParallelSolution(4);
                    case 3 -> new StreamSolution();
                    default -> throw new IllegalStateException("Unexpected value: " + counter);
                };
                solution.containCompound(arr);
                counter++;
            }
        }


        int counter = 1;
        while (counter <= 3) {
            System.out.println("Решение - " + counter);
            solution = switch (counter) {
                case 1 -> new SequentialSolution();
                case 2 -> new ParallelSolution(15);
                case 3 -> new StreamSolution();
                default -> throw new IllegalStateException("Unexpected value: " + counter);
            };

            long startTime = System.nanoTime();
            System.out.println("Ответ: " + solution.containCompound(arr));
            long endTime = System.nanoTime();

            long resultTime = endTime - startTime;
            System.out.println("Время выполнения: " + resultTime + "\n");
            counter++;
        }
    }
}