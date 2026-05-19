package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {
    protected static final int PORT = 6767;
    protected static final int countOfWorkers = 3;
    private static final int[] arr = new int[]{3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,10,3,3,3,3};

    public static void main(String[] args) {
        AtomicBoolean found = new AtomicBoolean(false);
        try (
            ServerSocket socket = new ServerSocket(PORT);
            ExecutorService workersPool = Executors.newFixedThreadPool(countOfWorkers)
        ) {
            System.out.println("==== Сервер запущен на порту: " + PORT + " ====");
            int workerIdx = 0;
            while (true) {
                Socket worker = socket.accept();
                int[] chunk = getChunk(arr, workerIdx, countOfWorkers);
                System.out.println("Новый работник #" + workerIdx + ": " + worker.getInetAddress());
                workersPool.execute(new WorkerHandler(worker, chunk, found, socket));
                workerIdx++;
            }
        } catch (Exception e) {
            if (found.get()) {
                System.out.println("==== Составное число найдено. Сервер остановлен. ====");
            } else {
                System.out.println("Ошибка: " + e.getMessage());
            }
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


