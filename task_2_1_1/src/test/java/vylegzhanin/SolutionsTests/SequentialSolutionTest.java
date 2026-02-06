package vylegzhanin.solutionstests;

import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import vylegzhanin.solutions.SequentialSolution;

/**
 * Тесты для проверки последовательного алгоритма поиска составных чисел.
 */
public class SequentialSolutionTest {
    SequentialSolution solution = new SequentialSolution();

    /**
     * Тест с массивом, содержащим составные числа.
     */
    @Test
    void testArrayWithPrime() {
        int[] arr = {4, 6, 8, 11, 12, 14};
        Assertions.assertTrue(solution.containCompound(arr));
    }

    /**
     * Тест с массивом, содержащим только составные числа.
     */
    @Test
    void testArrayWithoutPrime() {
        int[] arr = {4, 6, 8, 9, 10, 15};
        Assertions.assertTrue(solution.containCompound(arr));
    }

    /**
     * Тест с пустым массивом.
     */
    @Test
    void testEmptyArray() {
        int[] arr = {};
        Assertions.assertFalse(solution.containCompound(arr));
    }

    /**
     * Тест с массивом только из простых чисел.
     */
    @Test
    void testPrimeAtTheEnd() {
        int[] arr = {2, 3, 5, 7, 11, 13};
        Assertions.assertFalse(solution.containCompound(arr));
    }

    /**
     * Тест с отрицательными числами и нулем (все составные).
     */
    @Test
    void testNegativeNumbers() {
        int[] arr = {-1, -2, -3, 0, 1};
        Assertions.assertTrue(solution.containCompound(arr));
    }

    /**
     * Нагрузочный тест с большим массивом.
     */
    @Test
    void stressTest() {
        int[] arr = new int[1_000_000];
        Arrays.fill(arr, 4);
        arr[999_999] = 13;
        Assertions.assertTrue(solution.containCompound(arr));
    }
}