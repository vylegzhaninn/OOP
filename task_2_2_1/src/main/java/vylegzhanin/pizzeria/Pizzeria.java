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
        generatorThread.start();

        long endTime = startTime + config.pizzeriaWorkTime();

        int i = 1;
        while (i < config.bakersCount()){
            Baker baker = new Baker(orderQueue, config.workTime(), storage, endTime, i);
            Thread bakerThread = new Thread(baker);
            bakerThread.start();
            i++;
        }

        i = 1;
        while (i <= config.couriersCount()){
            Courier courier = new Courier(storage, config.workTime(), endTime, i);
            Thread courierThread = new Thread(courier);
            courierThread.start();
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

        log.info("Пиццерия закрылась");
    }
}
