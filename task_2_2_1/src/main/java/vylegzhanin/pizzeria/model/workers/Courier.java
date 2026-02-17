package vylegzhanin.pizzeria.model.workers;

import vylegzhanin.pizzeria.model.Order;
import vylegzhanin.pizzeria.repositories.OrderQueue;
import vylegzhanin.pizzeria.repositories.Storage;

public class Courier extends Worker {
    private final int trankSize;

    public Courier(Storage storage, long operatingTime, long endTime, int id, int trankSize) {
        super(operatingTime, storage, endTime, id);
        this.trankSize = trankSize;
    }

    @Override
    protected void uniqueTask(Order order) {
        log.info("{} № {} отдал заказчику заказ с id: {}", getClass().getSimpleName(), id, order.id());
    }

    @Override
    public void waitingForOrder() throws InterruptedException {
        OrderQueue orders = new OrderQueue();
        int tmpTrankSize = trankSize;

        synchronized (storage) {
            if (storage.isEmpty()) {
                log.info("Courier № {} ожидает заказ", id);
            }
            while (storage.isEmpty()) {
                storage.wait();
            }

            while (!storage.isEmpty() && tmpTrankSize > 0) {
                int orderSize = storage.getOrderSize();
                if (orderSize <= tmpTrankSize) {
                    Order order = storage.get();
                    tmpTrankSize -= orderSize;
                    orders.offer(order);
                    log.info("Courier № {} взял заказ с id: {} и массой: {}",
                            id, order.id(), order.size());
                } else {
                    break;
                }
            }
        }
        work(orders);
    }


    @Override
    public void run() {
        try {
            log.info("{} {} начал работу с объёмом багажника {} и временем работы {} мс",
                    getClass().getSimpleName(), id, trankSize, operatingTime);
            while (!Thread.currentThread().isInterrupted() && System.currentTimeMillis() < endTime) {
                waitingForOrder();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
