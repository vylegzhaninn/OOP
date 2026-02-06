package vylegzhanin.Solutions;

import vylegzhanin.ContainCompound;
import vylegzhanin.Prime;

/**
 * Класс для паралельного решения с заданым кол-вом потоков.
 */
public class ParallelSolution implements ContainCompound {
    volatile boolean foundCompound;
    private final int k;

    public ParallelSolution(int nThreads) {
        this.k = nThreads;
        this.foundCompound = false;
    }

    /**
     * Метод, реализующий паралельное решение.
     *
     * @param arr список чисел
     * @return true если список не содержит простое число
     */
    public boolean containCompound(int[] arr) throws InterruptedException {
        int n = arr.length;

        if (n == 0) {
            return false;
        }

        int numThreads = Math.min(k, n);
        int chunkSize = n / numThreads;

        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            int start = i * chunkSize;
            int end = (i == numThreads - 1) ? n : start + chunkSize;

            threads[i] = new Thread(() -> {
                for (int j = start; j < end && !foundCompound; j++) {
                    if (!Prime.isPrime(arr[j])) {
                        foundCompound = true;
                        break;
                    }
                }
            });
            threads[i].start();
        }

        for (Thread t: threads){
            t.join();
        }

        return foundCompound;
    }
}
