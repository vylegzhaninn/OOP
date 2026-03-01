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

    /**
     * Создаёт пустую очередь заказов.
     */
    public OrderQueue() {
        orderQueue = new LinkedList<>();
    }

    /**
     * Добавляет заказ в конец очереди.
     *
     * @param order заказ для добавления; не должен быть {@code null}
     */
    public synchronized void offer(Order order) {
        orderQueue.offer(order);
    }

    /**
     * Извлекает и удаляет первый заказ из очереди.
     *
     * @return первый заказ в очереди, или {@code null}, если очередь пуста
     */
    public synchronized Order poll() {
        return orderQueue.poll();
    }

    /**
     * Проверяет, пуста ли очередь.
     *
     * @return {@code true}, если заказов нет; {@code false} — если есть хотя бы один
     */
    public synchronized boolean isEmpty() {
        return orderQueue.isEmpty();
    }
}
