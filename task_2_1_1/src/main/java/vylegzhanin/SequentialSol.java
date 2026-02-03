package vylegzhanin;

/**
 * Класс для последовательного решения.
 */
public class SequentialSol {

    /**
     * Метод, реализующий последовательное решение.
     *
     * @param arr список чисел
     * @return true если список содержит простое число
     */
    public static boolean sequentialSolution(int[] arr) {
        for (int el : arr) {
            if (Prime.isPrime(el)) {
                return true;
            }
        }
        return false;
    }
}
