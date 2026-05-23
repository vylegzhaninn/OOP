package common;

import java.io.Serializable;

/**
 * Единица работы, передаваемая от сервера воркеру.
 * Содержит уникальный идентификатор и кусок исходного массива.
 * ID позволяет серверу однозначно сопоставить ответ воркера с задачей.
 *
 * @param id    Уникальный идентификатор задачи в рамках текущего запуска сервера.
 * @param chunk Подмассив чисел для проверки на составность.
 */
public record Task(int id, int[] chunk) implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * @param id    уникальный идентификатор задачи
     * @param chunk подмассив чисел для обработки
     */
    public Task {
    }
}
