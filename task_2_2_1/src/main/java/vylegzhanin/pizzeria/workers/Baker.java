package vylegzhanin.pizzeria.workers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vylegzhanin.pizzeria.supportive.Order;
import vylegzhanin.pizzeria.supportive.OrderQueue;
import vylegzhanin.pizzeria.supportive.Storage;

public class Baker implements Worker, Runnable{
    private final OrderQueue orderQueue;
    private final long operatingTime;
    private final Storage storage;
    private final long endTime;
    private final int id;
    private static final Logger log = LoggerFactory.getLogger(Baker.class);

    public Baker(OrderQueue orderQueue, long operatingTime, Storage storage, long endTime, int id) {
        this.orderQueue = orderQueue;
        this.operatingTime = operatingTime;
        this.storage = storage;
        this.endTime = endTime;
        this.id = id;
    }

    @Override
    public void work(Order order) throws InterruptedException {
        if (System.currentTimeMillis() + operatingTime < endTime){
            log.info("Пекарь {} готовит заказ", id);
            Thread.sleep(operatingTime);
            storage.add(order);
            log.info("Пекарь {} положил заказ в склад", id);
        }else {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void waitingForOrder() throws InterruptedException {
        Order order;
        synchronized (orderQueue) {
            if (orderQueue.isEmpty()) {
                log.info("Пекарь {} ожидает заказ", id);
            }
            while (orderQueue.isEmpty()) {
                orderQueue.wait();
            }
            order = orderQueue.poll();
        }
        work(order);
    }

    @Override
    public void run() {
        try {
            log.info("Пекарь {} начал работу", id);
            while (!Thread.currentThread().isInterrupted() && System.currentTimeMillis() < endTime){
                waitingForOrder();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
