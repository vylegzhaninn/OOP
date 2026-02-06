package vylegzhanin.Solutions;

import vylegzhanin.ContainCompound;
import vylegzhanin.Prime;
import java.util.Arrays;

/**
 * Класс для парального решения при помощи Stream.
 */
public class StreamSolution implements ContainCompound {

    /**
     * Метод,  возможно реализующий паралельное решение.
     *
     * @param arr список чисел
     * @return true если не содержит простое число
     */
    public boolean containCompound(int[] arr) {
        return Arrays.stream(arr).parallel().anyMatch(x -> !Prime.isPrime(x));
    }
}
