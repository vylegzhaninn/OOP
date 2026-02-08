package vylegzhanin.solutions;

import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Тесты для проверки параллельного алгоритма поиска составных чисел.
 */
class ParallelSolutionTest {

    /**
     * Тест с массивом, содержащим составные числа.
     *
     * @throws InterruptedException если поток был прерван
     */
    @Test
    void testArrayWithPrime() throws InterruptedException {
        ParallelSolution solution = new ParallelSolution(8);
        int[] arr = {4, 6, 8, 11, 12, 14};
        Assertions.assertTrue(solution.containCompound(arr));
    }

    /**
     * Тест с массивом, содержащим только составные числа.
     *
     * @throws InterruptedException если поток был прерван
     */
    @Test
    void testArrayWithoutPrime() throws InterruptedException {
        ParallelSolution solution = new ParallelSolution(8);
        int[] arr = {4, 6, 8, 9, 10, 15};
        Assertions.assertTrue(solution.containCompound(arr));
    }

    /**
     * Тест с пустым массивом.
     *
     * @throws InterruptedException если поток был прерван
     */
    @Test
    void testEmptyArray() throws InterruptedException {
        ParallelSolution solution = new ParallelSolution(8);
        int[] arr = {};
        Assertions.assertFalse(solution.containCompound(arr));
    }

    /**
     * Тест с массивом только из простых чисел.
     *
     * @throws InterruptedException если поток был прерван
     */
    @Test
    void testPrimeAtTheEnd() throws InterruptedException {
        ParallelSolution solution = new ParallelSolution(8);
        int[] arr = {2, 3, 5, 7, 11, 13};
        Assertions.assertFalse(solution.containCompound(arr));
    }

    /**
     * Тест с отрицательными числами и нулем (все составные).
     *
     * @throws InterruptedException если поток был прерван
     */
    @Test
    void testNegativeNumbers() throws InterruptedException {
        ParallelSolution solution = new ParallelSolution(8);
        int[] arr = {-1, -2, -3, 0, 1};
        Assertions.assertTrue(solution.containCompound(arr));
    }

    /**
     * Нагрузочный тест с большим массивом.
     *
     * @throws InterruptedException если поток был прерван
     */
    @Test
    void stressTest() throws InterruptedException {
        ParallelSolution solution = new ParallelSolution(8);
        int[] arr = new int[1_000_000];
        Arrays.fill(arr, 4);
        arr[999_999] = 13;
        Assertions.assertTrue(solution.containCompound(arr));
    }
}