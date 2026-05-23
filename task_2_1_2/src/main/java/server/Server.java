package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    protected static final int PORT = 6767;
    protected static final int countOfWorkers = 3;
    private static final int[] arr1 = new int[]{6, 8, 7, 13, 5, 9, 4};
    private static final int[] arr2 = new int[]{20319251, 6997901, 6997927, 6997937, 17858849, 6997967, 6998009, 6998029, 6998039, 20165149, 6998051, 6998053};
    private static final boolean[] found = new boolean[countOfWorkers];
    private static final boolean[] finish = new boolean[countOfWorkers];


    public static void main(String[] args) {
        try (
            ServerSocket socket = new ServerSocket(PORT);
            ExecutorService workersPool = Executors.newFixedThreadPool(countOfWorkers)
        ) {
            System.out.println("==== Сервер запущен на порту: " + PORT + " ====");

            for (int workerIdx = 0; workerIdx < countOfWorkers; workerIdx++) {
                Socket worker = socket.accept();
                int[] chunk = getChunk(arr1, workerIdx, countOfWorkers);
                System.out.println("Новый работник #" + workerIdx + ": " + worker.getInetAddress());
                workersPool.execute(new WorkerHandler(worker, chunk, found, workerIdx, finish));
            }

            while (true) {
                int finishedCount = 0;
                boolean composite = false;
                for (int i = 0; i < countOfWorkers; i++) {
                    if (finish[i]) finishedCount++;
                    if (found[i]) { composite = true; break; }
                }
                if (composite) {
                    System.out.println("Результат: true (составное число найдено)");
                    break;
                }
                if (finishedCount == countOfWorkers) {
                    System.out.println("Результат: false (составное число не найдено)");
                    break;
                }
                Thread.sleep(50);
            }
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


