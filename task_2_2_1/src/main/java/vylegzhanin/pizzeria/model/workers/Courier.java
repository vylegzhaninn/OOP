package vylegzhanin.pizzeria.model.workers;

import vylegzhanin.pizzeria.model.Order;
import vylegzhanin.pizzeria.repositories.Storage;

public class Courier extends Worker {
    private  int trankSize;

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
        Order order;
        synchronized (storage) {
            if (storage.isEmpty()) {
                log.info("Courier № {} ожидает заказ", id);
            }
            while (storage.isEmpty()) {
                storage.wait();
            }
            int tmpTrankSize = trankSize;
            //сделать так чтобы work принимал список ордеров
            while (!storage.isEmpty() && tmpTrankSize > 0) {
                if (storage.getOrderSize() <= tmpTrankSize) {
                    tmpTrankSize -= storage.getOrderSize();
                    order = storage.get();
                    work(order);
                } else {
                    tmpTrankSize = 0;
                }
            }
        }
    }
}
