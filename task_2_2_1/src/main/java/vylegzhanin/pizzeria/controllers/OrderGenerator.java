package vylegzhanin.pizzeria.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vylegzhanin.pizzeria.configs.AppConfig;
import vylegzhanin.pizzeria.model.Order;
import vylegzhanin.pizzeria.repositories.OrderQueue;

import java.util.Random;

public class OrderGenerator implements Runnable{
    private final long orderInterval;
    private final OrderQueue orderQueue;
    private boolean isAreLive;
    private static final Logger log = LoggerFactory.getLogger(OrderGenerator.class);
    private final AppConfig config;
    private final Random random;

    public OrderGenerator(long orderInterval, OrderQueue orderQueue, AppConfig config){
        this.orderInterval = orderInterval;
        this.orderQueue = orderQueue;
        this.isAreLive = true;
        this.config = config;
        this.random = new Random();
    }

    public void generateOrders() throws InterruptedException {
        long orderId = 1;
        while (isAreLive) {
            Thread.sleep(orderInterval);
            Order order = new Order(orderId++, random.nextInt(config.trankSize()));
            log.info("Поступил новый заказ {} с массой {}", orderId, order.size());
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
