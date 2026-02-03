package vylegzhanin;

import java.util.Arrays;

/**
 * Класс для парального решения при помощи Stream
 */
public class StreamSol {

    /**
     * Метод реализующий паралельное решение
     * @param arr список чисел
     * @return true если содержит простое число
     */
    public static boolean streamSolution(int[] arr){
        return Arrays.stream(arr).anyMatch(Prime::isPrime);
    }
}
