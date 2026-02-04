package vylegzhanin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** Тесты граничных случаев для {@link ParallelSol}. */
public class ParallelSolEdgeCasesTest {

    @Test
    void testKZeroThrows() {
        int[] arr = {2, 3, 5};
        Assertions.assertThrows(ArithmeticException.class, () -> ParallelSol.parallelSolution(arr, 0));
    }

    @Test
    void testKOne() throws InterruptedException {
        int[] arr = {4, 4, 5, 4};
        Assertions.assertTrue(ParallelSol.parallelSolution(arr, 1));
    }

    @Test
    void testKGreaterThanN() throws InterruptedException {
        int[] arr = {4, 7, 4};
        Assertions.assertTrue(ParallelSol.parallelSolution(arr, 100));
    }
}
