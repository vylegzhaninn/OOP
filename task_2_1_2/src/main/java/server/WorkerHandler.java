package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class WorkerHandler implements Runnable{
    private final Socket socket;
    private final int[] chunk;
    private final AtomicBoolean found;
    private final ServerSocket serverSocket;

    public WorkerHandler(Socket client, int[] chunk, AtomicBoolean found, ServerSocket serverSocket) {
        this.socket = client;
        this.chunk = chunk;
        this.found = found;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())
        ) {
            out.writeObject(chunk);
            out.flush();

            while (true) {
                String message = in.readLine();
                if (message != null && message.equals("beep")) {
                    found.set(true);
                    serverSocket.close();
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Связь с клиентом " + socket.getInetAddress() + " прервана.");
        }
    }
}

