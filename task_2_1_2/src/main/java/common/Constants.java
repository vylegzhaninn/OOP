package common;

/**
 * Общие константы протокола взаимодействия сервера и воркеров.
 * Вынесены в отдельный класс, чтобы избежать дублирования между модулями.
 */
public final class Constants {
    /** TCP-порт, на котором сервер ожидает подключений. */
    public static final int PORT = 6767;

    /** Количество задач (кусков), на которые делится исходный массив. */
    public static final int COUNT_OF_WORKERS = 3;

    /** Максимальное время ожидания ACK от воркера после отправки задачи (мс). */
    public static final int ACK_TIMEOUT_MS = 2000;

    /** Максимальное время ожидания результата обработки задачи (мс). */
    public static final int RESPONSE_TIMEOUT_MS = 10000;

    /** Таймаут на ServerSocket.accept(): нужен для периодической проверки условий выхода. */
    public static final int ACCEPT_TIMEOUT_MS = 5000;

    /** Таймаут на ожидание задачи из очереди в WorkerHandler (мс). */
    public static final int POLL_TIMEOUT_MS = 1000;

    private Constants() {}
}
