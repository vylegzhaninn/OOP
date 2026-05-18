package server;

import config.Config;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Config {
    private static int[] arr = new int[]{1,2,3,4,5};
    public static void main(String[] args) {
        try (
            ServerSocket socket = new ServerSocket(PORT);
            ExecutorService workersPool =  Executors.newFixedThreadPool(countOfWorkers)
        ) {
            System.out.println("==== Сервер запущен на порту: " + PORT + " ====");

            while (true) {
                Socket client = socket.accept();
                System.out.println("Новый клиент: " + client.getInetAddress());
                workersPool.execute(new ClientHandler(client));
            }
        }catch (Exception e) {
            System.out.println("Ошибка создания ресурсов в Server.main() " + e.getMessage());
        }
    }
}


