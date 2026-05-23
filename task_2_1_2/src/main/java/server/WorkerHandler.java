package server;

import common.Constants;
import common.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkerHandler implements Runnable {
    private final Socket socket;
    private final BlockingQueue<Task> pending;
    private final AtomicBoolean compositeFound;
    private final AtomicInteger remainingTasks;

    public WorkerHandler(Socket socket, BlockingQueue<Task> pending,
                         AtomicBoolean compositeFound, AtomicInteger remainingTasks) {
        this.socket = socket;
        this.pending = pending;
        this.compositeFound = compositeFound;
        this.remainingTasks = remainingTasks;
    }

    @Override
    public void run() {
        Task task = null;
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())
        ) {
            task = pending.poll(Constants.POLL_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            if (task == null) {
                return;
            }

            out.writeObject(task);
            out.flush();

            socket.setSoTimeout(Constants.ACK_TIMEOUT_MS);
            String ack = in.readLine();
            if (ack == null || !ack.equals("ack " + task.id)) {
                requeue(task, "не получен ACK");
                task = null;
                return;
            }

            socket.setSoTimeout(Constants.RESPONSE_TIMEOUT_MS);
            String response = in.readLine();
            if (response == null) {
                requeue(task, "разрыв до ответа");
                task = null;
                return;
            }

            if (response.equals("beep " + task.id)) {
                System.out.println("Работник нашёл составное в задаче #" + task.id);
                compositeFound.set(true);
                remainingTasks.decrementAndGet();
            } else if (response.equals("ok " + task.id)) {
                System.out.println("Задача #" + task.id + " завершена без находки");
                remainingTasks.decrementAndGet();
            } else {
                requeue(task, "неожиданный ответ: " + response);
                task = null;
            }
        } catch (SocketTimeoutException e) {
            if (task != null) {
                requeue(task, "таймаут");
            }
        } catch (IOException | InterruptedException e) {
            if (task != null) {
                requeue(task, "ошибка связи: " + e.getMessage());
            }
        }
    }

    private void requeue(Task t, String reason) {
        System.out.println("Задача #" + t.id + " возвращена в очередь (" + reason + ")");
        pending.offer(t);
    }
}
