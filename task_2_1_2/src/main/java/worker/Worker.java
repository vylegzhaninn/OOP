package worker;

import common.Constants;
import common.Task;

import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class Worker {
    public static void main(String[] args) {
        System.out.println("Работник запущен");

        try (
            Socket socket = new Socket("localhost", Constants.PORT);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            Task task = (Task) in.readObject();
            System.out.println("Получена задача #" + task.id + ": " + Arrays.toString(task.chunk));

            out.println("ack " + task.id);

            boolean found = false;
            for (int el : task.chunk) {
                if (isComposite(el)) {
                    found = true;
                    break;
                }
            }

            if (found) {
                out.println("beep " + task.id);
                System.out.println("Составное число найдено");
            } else {
                out.println("ok " + task.id);
                System.out.println("Составных чисел не найдено");
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    static boolean isComposite(int n) {
        if (n < 4) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return true;
        }
        return false;
    }
}
