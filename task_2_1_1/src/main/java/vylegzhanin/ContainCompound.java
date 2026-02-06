package vylegzhanin;

/**
 * Интерфейс для решения задачи о наличии составного числа в массиве.
 */
public interface ContainCompound {

    /**
     * Метод для проверки наличия составного числа в массиве.
     *
     * @param arr массив чисел для проверки
     * @return true, если массив содержит хотя бы одно составное число, иначе false
     */
    boolean containCompound(int[] arr) throws InterruptedException;
}
