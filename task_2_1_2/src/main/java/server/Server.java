package server;

import common.Constants;
import common.Task;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private static final int[] arr1 = new int[]{6, 8, 7, 13, 5, 9, 4};
    private static final int[] arr2 = new int[]{20319251, 6997901, 6997927, 6997937, 17858849, 6997967, 6998009, 6998029, 6998039, 20165149, 6998051, 6998053};

    public static void main(String[] args) {
        int[] input = arr1;
        BlockingQueue<Task> pending = new LinkedBlockingQueue<>();
        AtomicBoolean compositeFound = new AtomicBoolean(false);
        AtomicInteger remainingTasks = new AtomicInteger(Constants.COUNT_OF_WORKERS);

        for (int i = 0; i < Constants.COUNT_OF_WORKERS; i++) {
            pending.offer(new Task(i, getChunk(input, i, Constants.COUNT_OF_WORKERS)));
        }

        try (
            ServerSocket serverSocket = new ServerSocket(Constants.PORT);
            ExecutorService pool = Executors.newCachedThreadPool()
        ) {
            serverSocket.setSoTimeout(Constants.ACCEPT_TIMEOUT_MS);
            System.out.println("==== Сервер запущен на порту: " + Constants.PORT + " ====");

            while (!compositeFound.get() && remainingTasks.get() > 0) {
                try {
                    Socket worker = serverSocket.accept();
                    System.out.println("Новый работник: " + worker.getInetAddress());
                    pool.execute(new WorkerHandler(worker, pending, compositeFound, remainingTasks));
                } catch (SocketTimeoutException e) {
                    System.out.println("Ожидание воркеров... осталось задач: " + remainingTasks.get());
                }
            }

            System.out.println(compositeFound.get()
                ? "Результат: true (составное число найдено)"
                : "Результат: false (составных чисел нет)");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    static int[] getChunk(int[] arr, int workerIdx, int total) {
        int base = arr.length / total;
        int remainder = arr.length % total;
        int from = workerIdx * base + Math.min(workerIdx, remainder);
        int to = from + base + (workerIdx < remainder ? 1 : 0);
        return Arrays.copyOfRange(arr, from, to);
    }
}
