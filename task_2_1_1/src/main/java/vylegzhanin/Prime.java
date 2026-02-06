package vylegzhanin;

/**
 * Класс для проверки числа на простоту.
 */
public class Prime {

    /**
     * Метод, реализующий проверку на простоту.
     *
     * @param el число для проверки
     * @return true, если простое
     */
    public static boolean isPrime(int el) {
        if (el < 2) {
            return false;
        }
        for (int i = 2; i * i <= el; i++) {
            if (el % i == 0) {
                return false;
            }
        }
        return true;
    }
}
