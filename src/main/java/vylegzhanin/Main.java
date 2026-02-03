package vylegzhanin;

public class Main {
    static void main() throws InterruptedException {
        var arr = new int[] {6, 8, 7, 13, 5, 9, 4};

        System.out.println("1) Последовательное решение");
        long time = System.nanoTime();

        System.out.println("Ответ: " + SequentialSol.sequentialSolution(arr));

        long etime = System.nanoTime();
        long timeSequential = etime - time;
        System.out.println("Время выполнения: " + timeSequential + "\n");

        System.out.println("2) параллельное решение");
        time = System.nanoTime();

        System.out.println("Ответ: " + ParallelSol.parallelSolution(arr, 32));

        etime = System.nanoTime();
        long timeParallel = etime - time;
        System.out.println("Время выполнения: " + timeParallel + "\n");

        System.out.println("3) параллельное решение с parallelStream()");
        time = System.nanoTime();

        System.out.println("Ответ: " + StreamSol.streamSolution(arr));

        etime = System.nanoTime();
        long timeStream = etime - time;
        System.out.println("Время выполнения: " + timeStream + "\n");
    }
}
