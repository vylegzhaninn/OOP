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

/**
 * Обработчик одного подключённого воркера на стороне сервера.
 * <p>
 * Поддерживает постоянное соединение и в цикле раздаёт воркеру задачи из
 * общей очереди: отправляет задачу, ждёт подтверждение получения (ACK),
 * затем результат. При любом таймауте или ошибке связи задача возвращается
 * в очередь для повторной обработки другим воркером, а соединение
 * закрывается.
 * <p>
 * Когда работа завершена (найдено составное число либо нет больше
 * незавершённых задач), сервер отправляет воркеру сериализованный
 * {@code null} как сигнал «задач больше нет» и закрывает соединение.
 */
public class WorkerHandler implements Runnable {
    private final Socket socket;
    private final BlockingQueue<Task> pending;
    private final AtomicBoolean compositeFound;
    private final AtomicInteger remainingTasks;

    /**
     * @param socket          сокет подключённого воркера
     * @param pending         общая очередь незавершённых задач
     * @param compositeFound  флаг, выставляемый при нахождении составного числа
     * @param remainingTasks  счётчик задач, ожидающих успешного завершения
     */
    public WorkerHandler(Socket socket, BlockingQueue<Task> pending,
                         AtomicBoolean compositeFound, AtomicInteger remainingTasks) {
        this.socket = socket;
        this.pending = pending;
        this.compositeFound = compositeFound;
        this.remainingTasks = remainingTasks;
    }

    /**
     * В цикле выбирает задачи из очереди и обрабатывает их через одно
     * постоянное соединение с воркером. Выходит, когда работа всей системы
     * завершена либо когда при общении с этим воркером возникла ошибка
     * (в последнем случае незавершённая задача возвращается в очередь).
     */
    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())
        ) {
            while (!compositeFound.get() && remainingTasks.get() > 0) {
                Task task = pending.poll(Constants.POLL_TIMEOUT_MS, TimeUnit.MILLISECONDS);
                if (task == null) {
                    continue;
                }
                if (!processOne(task, in, out)) {
                    return;
                }
            }
            sendShutdown(out);
        } catch (IOException e) {
            System.out.println("Соединение с воркером разорвано: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Прогон одной задачи: отправка → ожидание ACK → ожидание результата.
     * При успехе обновляет общие счётчики/флаги и возвращает {@code true},
     * чтобы вызывающий цикл взял следующую задачу.
     * <p>
     * При любом сбое возвращает задачу в очередь и возвращает {@code false} —
     * соединение с этим воркером больше не используется.
     *
     * @param task текущая задача
     * @param in   входной поток сокета (ответы воркера, построчно)
     * @param out  выходной поток сокета (сериализованные задачи)
     * @return {@code true}, если задача обработана и соединение пригодно для следующей итерации
     */
    private boolean processOne(Task task, BufferedReader in, ObjectOutputStream out) {
        try {
            out.writeObject(task);
            out.reset();
            out.flush();

            socket.setSoTimeout(Constants.ACK_TIMEOUT_MS);
            String ack = in.readLine();
            if (ack == null || !ack.equals("ack " + task.id())) {
                requeue(task, "не получен ACK");
                return false;
            }

            socket.setSoTimeout(Constants.RESPONSE_TIMEOUT_MS);
            String response = in.readLine();
            if (response == null) {
                requeue(task, "разрыв до ответа");
                return false;
            }

            if (response.equals("beep " + task.id())) {
                System.out.println("Работник нашёл составное в задаче #" + task.id());
                compositeFound.set(true);
                remainingTasks.decrementAndGet();
                return true;
            }
            if (response.equals("ok " + task.id())) {
                System.out.println("Задача #" + task.id() + " завершена без находки");
                remainingTasks.decrementAndGet();
                return true;
            }
            requeue(task, "неожиданный ответ: " + response);
            return false;
        } catch (SocketTimeoutException e) {
            requeue(task, "таймаут");
            return false;
        } catch (IOException e) {
            requeue(task, "ошибка связи: " + e.getMessage());
            return false;
        }
    }

    /**
     * Уведомляет воркера, что задач больше не будет: посылает сериализованный
     * {@code null}. Ошибки записи игнорируются — это финальный сигнал перед
     * закрытием сокета.
     *
     * @param out выходной поток сокета
     */
    private void sendShutdown(ObjectOutputStream out) {
        try {
            out.writeObject(null);
            out.flush();
        } catch (IOException ignored) {
        }
    }

    /**
     * Возвращает задачу в очередь и логирует причину.
     *
     * @param t      задача для повторной обработки
     * @param reason описание причины возврата (для лога)
     */
    private void requeue(Task t, String reason) {
        System.out.println("Задача #" + t.id() + " возвращена в очередь (" + reason + ")");
        pending.offer(t);
    }
}
