package vylegzhanin.pizzeria.model.workers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vylegzhanin.pizzeria.model.Order;
import vylegzhanin.pizzeria.repositories.Storage;

public abstract class Worker implements Runnable {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final long operatingTime;
    protected final Storage storage;
    protected final long endTime;
    protected final int id;

    public Worker(long operatingTime, Storage storage, long endTime, int id) {
        this.operatingTime = operatingTime;
        this.storage = storage;
        this.endTime = endTime;
        this.id = id;
    }

    protected abstract void uniqueTask(Order order);

    protected final void work(Order order) throws InterruptedException {
        if (System.currentTimeMillis() + operatingTime + 100 < endTime) {
            log.info("{} № {} выполняет заказ с id: {}", getClass().getSimpleName(), id, order.id());
            Thread.sleep(operatingTime);
            uniqueTask(order);
        } else {
            Thread.currentThread().interrupt();
        }
    }

    protected abstract void waitingForOrder() throws InterruptedException;

    @Override
    public void run() {
        try {
            log.info("{} {} начал работу", getClass().getSimpleName(), id);
            while (!Thread.currentThread().isInterrupted() && System.currentTimeMillis() < endTime) {
                waitingForOrder();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
