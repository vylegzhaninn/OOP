package vylegzhanin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** Дополнительные тесты для класса {@link Prime}. */
public class PrimeAdditionalTest {

    @Test
    void negativeAndEven() {
        Assertions.assertFalse(Prime.isPrime(-17));
        Assertions.assertFalse(Prime.isPrime(100));
    }

    @Test
    void bigPrimeAndComposite() {
        Assertions.assertTrue(Prime.isPrime(7919));
        Assertions.assertFalse(Prime.isPrime(20014));
    }

    @Test
    void squareOfPrime() {
        Assertions.assertFalse(Prime.isPrime(49));
    }
}
