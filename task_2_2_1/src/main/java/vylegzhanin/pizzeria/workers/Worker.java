package vylegzhanin.pizzeria.workers;

import vylegzhanin.pizzeria.supportive.Order;

public interface Worker {
    void work(Order order) throws InterruptedException;
    void waitingForOrder() throws InterruptedException;
}
