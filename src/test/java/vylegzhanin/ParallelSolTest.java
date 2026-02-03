package vylegzhanin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class ParallelSolTest {
    @Test
    void testArrayWithPrime() throws InterruptedException {
        int[] arr = {4, 6, 8, 11, 12, 14};
        Assertions.assertTrue(ParallelSol.parallelSolution(arr, 3));
    }

    @Test
    void testArrayWithoutPrime() throws InterruptedException {
        int[] arr = {4, 6, 8, 9, 10, 15};
        Assertions.assertFalse(ParallelSol.parallelSolution(arr, 4));
    }

    @Test
    void testEmptyArray() throws InterruptedException {
        int[] arr = {};
        Assertions.assertFalse(ParallelSol.parallelSolution(arr, 2));
    }

    @Test
    void testPrimeAtTheEnd() throws InterruptedException {
        int[] arr = {4, 4, 4, 4, 4, 7};
        Assertions.assertTrue(ParallelSol.parallelSolution(arr, 2));
    }

    @Test
    void testNegativeNumbers() throws InterruptedException {
        int[] arr = {-1, -2, -3, 0, 1};
        Assertions.assertFalse(ParallelSol.parallelSolution(arr, 2));
    }

    @Test
    void stressTest() throws InterruptedException {
        int[] arr = new int[1_000_000];
        Arrays.fill(arr, 4);
        arr[999_999] = 13;
        Assertions.assertTrue(ParallelSol.parallelSolution(arr, 8));
    }
}