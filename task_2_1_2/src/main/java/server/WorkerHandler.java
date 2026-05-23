package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class WorkerHandler implements Runnable{
    private final Socket socket;
    private final int[] chunk;
    private final boolean[] found;
    private final int workerIdx;
    private final boolean[] finish;

    public WorkerHandler(Socket client, int[] chunk, boolean[] found, int workerIdx, boolean[] finish) {
        this.socket = client;
        this.chunk = chunk;
        this.found = found;
        this.workerIdx = workerIdx;
        this.finish = finish;
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())
        ) {
            out.writeObject(chunk);
            out.flush();
            System.out.println("Работник" + workerIdx + "начал работу");

            while (true) {
                String message = in.readLine();
                if (message != null && message.equals("beep")) {
                    System.out.println("Работник" + workerIdx + "нашел составное число");
                    found[workerIdx] = true;
                    finish[workerIdx] = true;
                    break;
                }else if(message != null && message.equals("finish")) {
                    System.out.println("Работник" + workerIdx + "не нашел составное число");
                    finish[workerIdx] = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Связь с работником " + workerIdx + " прервана.");
        }
    }
}

