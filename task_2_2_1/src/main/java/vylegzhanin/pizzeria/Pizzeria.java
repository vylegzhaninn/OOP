package vylegzhanin.pizzeria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vylegzhanin.pizzeria.supportive.AppConfig;
import vylegzhanin.pizzeria.supportive.OrderQueue;
import vylegzhanin.pizzeria.supportive.Storage;
import vylegzhanin.pizzeria.workers.Baker;
import vylegzhanin.pizzeria.workers.Courier;

public class Pizzeria {
    private final AppConfig config;
    private final OrderQueue orderQueue;
    private final Storage storage;
    private final long startTime;
    private static final Logger log = LoggerFactory.getLogger(Pizzeria.class);

    public Pizzeria(AppConfig config){
        this.orderQueue = new OrderQueue();
        this.config = config;
        this.storage = new Storage(config.storageCapacity());
        this.startTime = System.currentTimeMillis();
    }

    public void work() throws InterruptedException {
        OrderGenerator orderGenerator = new OrderGenerator(config.orderInterval(), orderQueue);
        Thread generatorThread = new Thread(orderGenerator);
        Thread[] bakers = new Thread[config.bakersCount()];
        Thread[] couriers = new Thread[config.couriersCount()];
        generatorThread.start();

        long endTime = startTime + config.pizzeriaWorkTime();

        int i = 0;
        while (i < config.bakersCount()){
            Baker baker = new Baker(orderQueue, config.workTime(), storage, endTime, i + 1);
            bakers[i] = new Thread(baker);
            bakers[i].start();
            i++;
        }

        i = 0;
        while (i < config.couriersCount()){
            Courier courier = new Courier(storage, config.workTime(), endTime, i + 1);
            couriers[i] = new Thread(courier);
            couriers[i].start();
            i++;
        }


        while (System.currentTimeMillis() < endTime) {
            Thread.sleep(100);
            synchronized (orderQueue){
                orderQueue.notify();
            }

            synchronized (storage){
                storage.notify();
            }
        }

        orderGenerator.endGenerating();

        for (Thread baker : bakers) {
            baker.interrupt();
        }

        for (Thread courier : couriers) {
            courier.interrupt();
        }

        log.info("Пиццерия закрылась");
    }
}
