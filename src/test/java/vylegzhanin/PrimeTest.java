package vylegzhanin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PrimeTest {
    @Test
    void extremeCases(){
        Assertions.assertFalse(Prime.isPrime(0));
        Assertions.assertFalse(Prime.isPrime(1));
        Assertions.assertTrue(Prime.isPrime(2));
    }
}
