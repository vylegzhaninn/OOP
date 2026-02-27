package vylegzhanin.pizzeria.repositories;

import vylegzhanin.pizzeria.model.Order;

/**
 * Потокобезопасное промежуточное хранилище готовых заказов.
 *
 * <p>Реализует стек (LIFO) поверх массива фиксированной ёмкости.
 * Все публичные методы синхронизированы для безопасного доступа
 * из нескольких потоков (пекарей и курьеров).</p>
 */
public class Storage {
    private final Order[] storage;
    private int i;

    /**
     * Создаёт хранилище с заданной максимальной ёмкостью.
     *
     * @param Capacity максимальное количество заказов, которые можно хранить одновременно
     */
    public Storage(int Capacity) {
        storage = new Order[Capacity];
        i = 0;
    }

    /**
     * Добавляет заказ в хранилище.
     *
     * <p><b>Предусловие:</b> хранилище не должно быть заполнено ({@link #isFull()} == {@code false}).
     * Вызывающий код обязан проверить это перед вызовом, иначе возможен
     * {@link ArrayIndexOutOfBoundsException}.</p>
     *
     * @param order заказ, который необходимо положить на хранение; не должен быть {@code null}
     */
    public synchronized void add(Order order) {
        storage[i++] = order;
    }

    /**
     * Возвращает объём (массу) последнего добавленного заказа без его удаления из хранилища.
     *
     * <p><b>Предусловие:</b> хранилище не должно быть пустым ({@link #isEmpty()} == {@code false}).</p>
     *
     * @return размер (масса) верхнего заказа в стеке
     * @throws NullPointerException если хранилище пусто и {@code storage[i - 1]} равен {@code null}
     */
    public synchronized int getOrderSize() {
        return storage[i - 1].size();
    }

    /**
     * Извлекает верхний заказ из хранилища (операция pop).
     *
     * <p>После вызова заказ удаляется из хранилища и его ячейка освобождается.</p>
     *
     * <p><b>Предусловие:</b> хранилище не должно быть пустым ({@link #isEmpty()} == {@code false}).</p>
     *
     * @return последний добавленный заказ
     */
    public synchronized Order get() {
        return storage[--i];
    }

    /**
     * Проверяет, пусто ли хранилище.
     *
     * @return {@code true}, если заказов нет; {@code false} — если есть хотя бы один
     */
    public synchronized boolean isEmpty() {
        return i < 1;
    }

    /**
     * Проверяет, заполнено ли хранилище до максимальной ёмкости.
     *
     * @return {@code true}, если новый заказ добавить невозможно; {@code false} — если место есть
     */
    public synchronized boolean isFull() {
        return storage.length <= i;
    }
}
