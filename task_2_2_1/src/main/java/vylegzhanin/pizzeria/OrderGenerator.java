package vylegzhanin.pizzeria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vylegzhanin.pizzeria.supportive.Order;
import vylegzhanin.pizzeria.supportive.OrderQueue;

public class OrderGenerator implements Runnable{
    private final long orderInterval;
    private final OrderQueue orderQueue;
    private boolean isAreLive;
    private static final Logger log = LoggerFactory.getLogger(OrderGenerator.class);

    public OrderGenerator(long orderInterval, OrderQueue orderQueue){
        this.orderInterval = orderInterval;
        this.orderQueue = orderQueue;
        this.isAreLive = true;
    }

    public void generateOrders() throws InterruptedException {
        long orderId = 1;
        while (isAreLive) {
            Thread.sleep(orderInterval);
            Order order = new Order(orderId++);
            log.info("Поступил новый заказ {}", orderId);
            orderQueue.offer(order);
        }
    }

    @Override
    public void run(){
        try {
            log.info("Заказы поступают");
            generateOrders();
        } catch (InterruptedException e) {
            throw new RuntimeException("Генератор заказов был прерван" + e.getMessage());
        }
    }

    public void endGenerating(){
        isAreLive = false;
        Thread.currentThread().interrupt();
        log.info("Заказы перестали поступать");
    }
}
