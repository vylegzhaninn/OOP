package worker;

import common.Constants;
import common.Task;

import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

/**
 * Вычислительный узел распределённой системы.
 * <p>
 * Подключается к серверу и в цикле принимает задачи: на каждой итерации
 * получает {@link Task}, подтверждает её ответом {@code "ack <id>"},
 * выполняет поиск составного числа в своём куске и возвращает результат:
 * {@code "beep <id>"} если составное найдено, иначе {@code "ok <id>"}.
 * <p>
 * Цикл завершается, когда сервер вместо очередной задачи присылает
 * сериализованный {@code null} — это означает, что задач больше нет
 * (все обработаны или одна из них уже нашла составное число).
 */
public class Worker {
    /**
     * Точка входа воркера. Поддерживает одно постоянное соединение с сервером
     * и обрабатывает все назначенные задачи в цикле, пока сервер не пришлёт
     * сигнал завершения.
     */
    public static void main(String[] args) {
        System.out.println("Работник запущен");
        try (
            Socket socket = new Socket("localhost", Constants.PORT);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            while (true) {
                Task task = (Task) in.readObject();
                if (task == null) {
                    System.out.println("Сервер сообщил: задач больше нет");
                    break;
                }
                System.out.println("Получена задача #" + task.id() + ": " + Arrays.toString(task.chunk()));
                out.println("ack " + task.id());

                boolean found = false;
                for (int el : task.chunk()) {
                    if (isComposite(el)) { found = true; break; }
                }
                out.println((found ? "beep " : "ok ") + task.id());
                System.out.println(found ? "Составное число найдено" : "Составных чисел не найдено");
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Проверяет, является ли число составным (имеет делитель кроме 1 и самого себя).
     * Числа 0, 1, 2, 3 не считаются составными.
     *
     * @param n проверяемое число
     * @return true, если найден делитель в диапазоне [2, sqrt(n)]
     */
    static boolean isComposite(int n) {
        if (n < 4) return false;
        for (long i = 2; i * i <= (long) n; i++) {
            if (n % i == 0) return true;
        }
        return false;
    }
}
