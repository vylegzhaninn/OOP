package vylegzhanin.Solutions;

import vylegzhanin.ContainCompound;
import vylegzhanin.Prime;

/**
 * Класс для последовательного решения.
 */
public class SequentialSolution implements ContainCompound {

    /**
     * Метод, реализующий последовательное решение.
     *
     * @param arr список чисел
     * @return true если список не содержит простое число
     */
    public boolean containCompound(int[] arr) {
        for (int el : arr) {
            if (!Prime.isPrime(el)) {
                return true;
            }
        }
        return false;
    }
}
