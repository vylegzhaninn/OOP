package vylegzhanin.pizzeria.supportive;

import java.util.LinkedList;

public class OrderQueue {
    private final LinkedList<Order> orderQueue;

    public OrderQueue(){
        orderQueue = new LinkedList<>();
    }

    public synchronized void offer(Order order){
        orderQueue.offer(order);
    }

    public synchronized Order poll(){
        return orderQueue.poll();
    }

    public synchronized boolean isEmpty(){
        return orderQueue.isEmpty();
    }
}
