package vylegzhanin.pizzeria.model.workers;

import vylegzhanin.pizzeria.model.Order;
import vylegzhanin.pizzeria.repositories.OrderQueue;
import vylegzhanin.pizzeria.repositories.Storage;

public class Baker extends Worker {
    private final OrderQueue orderQueue;

    public Baker(OrderQueue orderQueue, long operatingTime, Storage storage, long endTime, int id) {
        super(operatingTime, storage, endTime, id);
        this.orderQueue = orderQueue;
    }

    @Override
    public void uniqueTask(Order order) {
        storage.add(order);
        log.info("{} № {} положил заказ с id: {} на склад", getClass().getSimpleName(), id, order.id());
    }

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
