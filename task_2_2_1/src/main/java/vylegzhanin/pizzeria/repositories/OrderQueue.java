package vylegzhanin.pizzeria.repositories;

import java.util.LinkedList;
import vylegzhanin.pizzeria.model.Order;

/**
 * Потокобезопасная очередь входящих заказов (FIFO).
 *
 * <p>Используется как буфер между генератором заказов и пекарями:
 * генератор кладёт заказы через {@link #offer}, а пекари
 * разбирают их через {@link #poll}.</p>
 *
 * <p>Все методы синхронизированы; пекари ожидают появления заказов
 * через {@code wait()} на мониторе данного объекта, а генератор
 * будит их через {@code notifyAll()}.</p>
 */
public class OrderQueue {
    private final LinkedList<Order> orderQueue;
    private final int capacity;

    /**
     * Создаёт пустую очередь заказов с неограниченной вместимостью.
     */
    public OrderQueue() {
        this(Integer.MAX_VALUE);
    }

    /**
     * Создаёт пустую очередь заказов с заданной вместимостью.
     *
     * @param capacity максимальное количество заказов в очереди
     */
    public OrderQueue(int capacity) {
        this.orderQueue = new LinkedList<>();
        this.capacity = capacity;
    }

    /**
     * Добавляет заказ в конец очереди и уведомляет всех ожидающих.
     * Если очередь заполнена, поток блокируется до появления свободного о места.
     *
     * @param order заказ для добавления; не должен быть {@code null}
     * @throws InterruptedException если поток был прерван во время ожидания
     */
    public synchronized void offer(Order order) throws InterruptedException {
        while (orderQueue.size() >= capacity) {
            wait();
        }
        orderQueue.offer(order);
        notifyAll();
    }

    /**
     * Ожидает появления заказа в очереди и забирает его.
     *
     * @return первый заказ в очереди
     * @throws InterruptedException если поток был прерван во время ожидания
     */
    public synchronized Order take() throws InterruptedException {
        while (orderQueue.isEmpty()) {
            wait();
        }
        Order order = orderQueue.poll();
        notifyAll();
        return order;
    }

    /**
     * Извлекает и удаляет первый заказ из очереди без ожидания.
     *
     * @return первый заказ в очереди, или {@code null}, если очередь пуста
     */
    public synchronized Order poll() {
        Order order = orderQueue.poll();
        if (order != null) {
            notifyAll();
        }
        return order;
    }

    /**
     * Возвращает первый заказ в очереди без удаления.
     *
     * @return первый заказ или {@code null}, если очередь пуста
     */
    public synchronized Order peek() {
        return orderQueue.peek();
    }

    /**
     * Проверяет, пуста ли очередь.
     *
     * @return {@code true}, если заказов нет; {@code false} — если есть хотя бы один
     */
    public synchronized boolean isEmpty() {
        return orderQueue.isEmpty();
    }

    /**
     * Проверяет, заполнена ли очередь.
     *
     * @return {@code true} если очередь полна, {@code false} иначе
     */
    public synchronized boolean isFull() {
        return orderQueue.size() >= capacity;
    }
}
