package common;

import java.io.Serializable;

/**
 * Единица работы, передаваемая от сервера воркеру.
 * Содержит уникальный идентификатор и кусок исходного массива.
 * ID позволяет серверу однозначно сопоставить ответ воркера с задачей.
 */
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Уникальный идентификатор задачи в рамках текущего запуска сервера. */
    public final int id;

    /** Подмассив чисел для проверки на составность. */
    public final int[] chunk;

    /**
     * @param id    уникальный идентификатор задачи
     * @param chunk подмассив чисел для обработки
     */
    public Task(int id, int[] chunk) {
        this.id = id;
        this.chunk = chunk;
    }
}
