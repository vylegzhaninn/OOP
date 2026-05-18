package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private final Socket socket;

    public ClientHandler(Socket client) {
        this.socket = client;
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            while (true) {
                String message = in.readLine();
                System.out.println("Получено: " + message);
            }
        } catch (IOException e) {
            System.out.println("Связь с клиентом " + socket.getInetAddress() + " прервана.");
        }
    }
}

