package vylegzhanin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Тесты для проверки метода определения простоты числа.
 */
public class PrimeTest {

    /**
     * Проверка граничных значений (0, 1, 2).
     */
    @Test
    void extremeCases() {
        Assertions.assertFalse(Prime.isPrime(0));
        Assertions.assertFalse(Prime.isPrime(1));
        Assertions.assertTrue(Prime.isPrime(2));
    }
}