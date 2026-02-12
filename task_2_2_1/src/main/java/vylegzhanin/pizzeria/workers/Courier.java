package vylegzhanin.pizzeria.workers;

import vylegzhanin.pizzeria.supportive.Order;
import vylegzhanin.pizzeria.supportive.Storage;

public class Courier extends Worker {
    public Courier(Storage storage, long operatingTime, long endTime, int id) {
        super(operatingTime, storage, endTime, id);
    }

    @Override
    protected void uniqueTask(Order order) {
        log.info("{} № {} отдал заказчику заказ с id: {}", getClass().getSimpleName(), id, order.id());
    }

    @Override
    public void waitingForOrder() throws InterruptedException {
        Order order;
        synchronized (storage) {
            if (storage.isEmpty()) {
                log.info("Courier № {} ожидает заказ", id);
            }
            while (storage.isEmpty()) {
                storage.wait();
            }
            order = storage.get();
        }
        work(order);
    }
}
