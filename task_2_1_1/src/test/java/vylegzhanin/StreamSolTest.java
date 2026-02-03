package vylegzhanin;

import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Тесты для проверки решения с использованием Stream API.
 */
public class StreamSolTest {

    /**
     * Тест с массивом, содержащим простое число.
     *
     * @throws InterruptedException если поток был прерван
     */
    @Test
    void testArrayWithPrime() throws InterruptedException {
        int[] arr = {4, 6, 8, 11, 12, 14};
        Assertions.assertTrue(StreamSol.streamSolution(arr));
    }

    /**
     * Тест с массивом без простых чисел.
     *
     * @throws InterruptedException если поток был прерван
     */
    @Test
    void testArrayWithoutPrime() throws InterruptedException {
        int[] arr = {4, 6, 8, 9, 10, 15};
        Assertions.assertFalse(StreamSol.streamSolution(arr));
    }

    /**
     * Тест с пустым массивом.
     *
     * @throws InterruptedException если поток был прерван
     */
    @Test
    void testEmptyArray() throws InterruptedException {
        int[] arr = {};
        Assertions.assertFalse(StreamSol.streamSolution(arr));
    }

    /**
     * Тест, где простое число находится в конце массива.
     *
     * @throws InterruptedException если поток был прерван
     */
    @Test
    void testPrimeAtTheEnd() throws InterruptedException {
        int[] arr = {4, 4, 4, 4, 4, 7};
        Assertions.assertTrue(StreamSol.streamSolution(arr));
    }

    /**
     * Тест с отрицательными числами и нулем.
     *
     * @throws InterruptedException если поток был прерван
     */
    @Test
    void testNegativeNumbers() throws InterruptedException {
        int[] arr = {-1, -2, -3, 0, 1};
        Assertions.assertFalse(StreamSol.streamSolution(arr));
    }

    /**
     * Нагрузочный тест с большим массивом.
     *
     * @throws InterruptedException если поток был прерван
     */
    @Test
    void stressTest() throws InterruptedException {
        int[] arr = new int[1_000_000];
        Arrays.fill(arr, 4);
        arr[999_999] = 13;
        Assertions.assertTrue(StreamSol.streamSolution(arr));
    }
}