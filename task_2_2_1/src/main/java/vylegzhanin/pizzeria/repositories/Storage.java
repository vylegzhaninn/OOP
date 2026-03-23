package vylegzhanin.pizzeria.repositories;

import java.util.ArrayList;
import java.util.List;
import vylegzhanin.pizzeria.model.Order;

/**
 * Потокобезопасное промежуточное хранилище готовых заказов.
 *
 * <p>Реализует стек (LIFO) поверх массива фиксированной ёмкости.
 * Все публичные методы синхронизированы для безопасного доступа
 * из нескольких потоков (пекарей и курьеров).</p>
 */
public class Storage {
    /**
     * Массив для хранения заказов; ёмкость задаётся при создании.
     */
    private final Order[] storage;

    /**
     * Индекс следующей свободной ячейки (одновременно — текущий размер).
     */
    private int top;

    /**
     * Создаёт хранилище с заданной максимальной ёмкостью.
     *
     * @param capacity максимальное количество заказов, которые можно хранить одновременно
     */
    public Storage(int capacity) {
        storage = new Order[capacity];
        top = 0;
    }

    /**
     * Добавляет заказ в хранилище, блокируясь, если оно заполнено.
     *
     * @param order заказ, который необходимо положить на хранение; не должен быть {@code null}
     * @throws InterruptedException если поток был прерван во время ожидания
     */
    public synchronized void add(Order order) throws InterruptedException {
        while (isFull()) {
            wait();
        }
        storage[top++] = order;
        notifyAll();
    }

    /**
     * Извлекает заказы из хранилища, заполняя доступную ёмкость.
     * Блокируется, если хранилище пусто.
     *
     * @param maxCapacity максимальная суммарная масса заказов
     * @return список извлечённых заказов
     * @throws InterruptedException если поток был прерван во время ожидания
     */
    public synchronized List<Order> take(int maxCapacity) throws InterruptedException {
        while (isEmpty()) {
            wait();
        }

        List<Order> orders = new ArrayList<>();
        int currentCapacity = maxCapacity;

        while (!isEmpty() && getOrderSize() <= currentCapacity) {
            Order order = get();
            orders.add(order);
            currentCapacity -= order.size();
        }
        
        notifyAll();
        return orders;
    }

    /**
     * Возвращает объём (массу) последнего добавленного заказа
     * без его удаления из хранилища.
     *
     * <p><b>Предусловие:</b> хранилище не должно быть пустым
     * ({@link #isEmpty()} == {@code false}).</p>
     *
     * @return размер (масса) верхнего заказа в стеке
     * @throws NullPointerException если хранилище пусто
     */
    public synchronized int getOrderSize() {
        return storage[top - 1].size();
    }

    /**
     * Извлекает верхний заказ из хранилища (операция pop).
     *
     * <p>После вызова заказ удаляется из хранилища и его ячейка освобождается.</p>
     *
     * @return последний добавленный заказ или {@code null}, если хранилище пусто
     */
    public synchronized Order get() {
        if (isEmpty()) {
            return null;
        }
        return storage[--top];
    }

    /**
     * Проверяет, пусто ли хранилище.
     *
     * @return {@code true}, если заказов нет; {@code false} — если есть хотя бы один
     */
    public synchronized boolean isEmpty() {
        return top < 1;
    }

    /**
     * Проверяет, заполнено ли хранилище до максимальной ёмкости.
     *
     * @return {@code true}, если новый заказ добавить невозможно;
     * {@code false} — если место есть
     */
    public synchronized boolean isFull() {
        return storage.length <= top;
    }
}
