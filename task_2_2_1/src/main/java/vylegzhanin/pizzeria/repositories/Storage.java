package vylegzhanin.pizzeria.repositories;

import java.util.ArrayList;
import java.util.List;
import vylegzhanin.pizzeria.model.Order;

/**
 * Потокобезопасное промежуточное хранилище готовых заказов.
 *
 * <p>Использует {@link OrderQueue} (FIFO) для хранения заказов.</p>
 */
public class Storage {
    private final OrderQueue queue;

    /**
     * Создаёт хранилище с заданной максимальной ёмкостью.
     *
     * @param capacity максимальное количество заказов, которые можно хранить одновременно
     */
    public Storage(int capacity) {
        queue = new OrderQueue(capacity);
    }

    /**
     * Добавляет заказ в хранилище, блокируясь, если оно заполнено.
     *
     * @param order заказ, который необходимо положить на хранение; не должен быть {@code null}
     * @throws InterruptedException если поток был прерван во время ожидания
     */
    public void add(Order order) throws InterruptedException {
        queue.offer(order);
    }

    /**
     * Извлекает заказы из хранилища, заполняя доступную ёмкость.
     * Блокируется, если хранилище пусто.
     *
     * @param maxCapacity максимальная суммарная масса заказов
     * @return список извлечённых заказов
     * @throws InterruptedException если поток был прерван во время ожидания
     */
    public List<Order> take(int maxCapacity) throws InterruptedException {
        synchronized (queue) {
            while (isEmpty()) {
                queue.wait();
            }

            List<Order> orders = new ArrayList<>();
            int currentCapacity = maxCapacity;

            while (!isEmpty()) {
                Order next = queue.peek();
                if (next.size() <= currentCapacity) {
                    orders.add(queue.poll());
                    currentCapacity -= next.size();
                } else {
                    break;
                }
            }
            return orders;
        }
    }

    /**
     * Возвращает объём (массу) следующего доступного заказа
     * без его удаления из хранилища.
     *
     * <p><b>Предусловие:</b> хранилище не должно быть пустым
     * ({@link #isEmpty()} == {@code false}).</p>
     *
     * @return размер (масса) следующего заказа
     * @throws NullPointerException если хранилище пусто
     */
    public int getOrderSize() {
        Order order = queue.peek();
        if (order == null) {
            throw new NullPointerException();
        }
        return order.size();
    }

    /**
     * Извлекает один заказ из хранилища.
     *
     * @return следующий заказ или {@code null}, если хранилище пусто
     */
    public Order get() {
        return queue.poll();
    }

    /**
     * Проверяет, пусто ли хранилище.
     *
     * @return {@code true}, если заказов нет; {@code false} — если есть хотя бы один
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Проверяет, заполнено ли хранилище до максимальной ёмкости.
     *
     * @return {@code true}, если новый заказ добавить невозможно;
     * {@code false} — если место есть
     */
    public boolean isFull() {
        return queue.isFull();
    }
}
