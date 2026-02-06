package vylegzhanin;

import vylegzhanin.Solutions.ParallelSolution;
import vylegzhanin.Solutions.SequentialSolution;
import vylegzhanin.Solutions.StreamSolution;

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
        var arr = new int[] {3, 3, 3, 3, 3, 3, 3, 20};

        ContainCompound solution;

        int counter = 1;
        while (counter <= 3) {
            System.out.println("Решение - " + counter);
            solution = switch (counter) {
                case 1 -> new SequentialSolution();
                case 2 -> new ParallelSolution(10);
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