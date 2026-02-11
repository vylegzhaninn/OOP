package vylegzhanin.pizzeria.workers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vylegzhanin.pizzeria.supportive.Order;
import vylegzhanin.pizzeria.supportive.Storage;

public class Courier implements Worker, Runnable{
    private final int id;
    private final Storage storage;
    private final long operatingTime;
    private final long endTime;
    private static final Logger log = LoggerFactory.getLogger(Courier.class);

    public Courier(Storage storage, long operatingTime, long endTime, int id) {
        this.storage = storage;
        this.operatingTime = operatingTime;
        this.endTime = endTime;
        this.id = id;
    }

    @Override
    public void work(Order order) throws InterruptedException {
        if (System.currentTimeMillis() + operatingTime < endTime) {
            log.info("Курьер {} доставляет заказ", id);
            Thread.sleep(operatingTime);
        } else {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void waitingForOrder() throws InterruptedException {
        Order order;
        synchronized (storage) {
            if (storage.isEmpty()) {
                log.info("Курьер {} ожидает заказ", id);
            }
            while (storage.isEmpty()) {
                storage.wait();
            }
            order = storage.get();
        }
        work(order);
    }

    @Override
    public void run() {
        try {
            log.info("Курьер {} начал работу", id);
            while (!Thread.currentThread().isInterrupted()
                    && System.currentTimeMillis() < endTime) {
                waitingForOrder();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
