package vylegzhanin.pizzeria.model.workers;

import vylegzhanin.pizzeria.model.Order;
import vylegzhanin.pizzeria.repositories.OrderQueue;
import vylegzhanin.pizzeria.repositories.Storage;

/**
 * Пекарь — работник, который берёт заказы из очереди и готовит их.
 *
 * <p>Пекарь блокируется на мониторе {@link OrderQueue}, пока в очереди
 * нет заказов, после чего забирает один заказ и кладёт его в
 * {@link Storage} по истечении {@code operatingTime} мс.</p>
 */
public class Baker extends Worker {
    private final OrderQueue orderQueue;

    /**
     * Создаёт пекаря с заданными параметрами.
     *
     * @param orderQueue    очередь входящих заказов
     * @param operatingTime время приготовления одного заказа, мс
     * @param storage       общее хранилище для готовых заказов
     * @param endTime       момент окончания рабочего дня (мс с эпохи Unix)
     * @param id            порядковый номер пекаря (для логирования)
     */
    public Baker(OrderQueue orderQueue, long operatingTime, Storage storage, long endTime, int id) {
        super(operatingTime, storage, endTime, id);
        this.orderQueue = orderQueue;
    }

    /**
     * Кладёт готовый заказ в хранилище и уведомляет ожидающих курьеров.
     *
     * @param order приготовленный заказ
     */
    @Override
    public void handleOrder(Order order) {
        storage.add(order);
        log.info("{} № {} положил заказ с id: {} на склад",
                getClass().getSimpleName(), id, order.id());
    }

    /**
     * Ожидает появления заказа в очереди, после чего забирает его и начинает готовить.
     *
     * <p>Метод блокируется на мониторе {@code orderQueue} через {@code wait()}
     * до тех пор, пока очередь не станет непустой. Затем извлекает один заказ
     * и передаёт его в {@link #work(Order)}.</p>
     *
     * @throws InterruptedException если поток был прерван во время ожидания
     */
    @Override
    public void waitingForOrder() throws InterruptedException {
        Order order;
        synchronized (orderQueue) {
            if (orderQueue.isEmpty()) {
                log.info("Baker № {} ожидает заказ", id);
            }
            while (orderQueue.isEmpty()) {
                orderQueue.wait();
            }
            order = orderQueue.poll();
        }
        work(order);
    }
}
