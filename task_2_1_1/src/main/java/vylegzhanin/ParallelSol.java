package vylegzhanin;

public class ParallelSol {
    public static boolean parallelSolution(int[] arr, int k) throws InterruptedException {
        int n = arr.length;

        if (n == 0) return false;

        int numThreads = Math.min(k, n);
        int chunkSize = n / numThreads;

        Thread[] threads = new Thread[numThreads];
        boolean[] threadsBool = new boolean[numThreads];

        for (int i = 0; i < numThreads; i++) {
            int start = i * chunkSize;
            int end = (i == numThreads - 1) ? n : start + chunkSize;

            int finalI = i;
            threads[i] = new Thread(() -> {
                for (int j = start; j < end; j++) {
                    if (Prime.isPrime(arr[j])){
                        threadsBool[finalI] = true;
                        break;
                    }
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        for(boolean el : threadsBool){
            if(el) return true;
        }

        return false;
    }
}
