package vylegzhanin.pizzeria.service;

import vylegzhanin.pizzeria.configs.AppConfig;
import vylegzhanin.pizzeria.repositories.OrderQueue;
import vylegzhanin.pizzeria.repositories.Storage;
import vylegzhanin.pizzeria.model.workers.Baker;
import vylegzhanin.pizzeria.model.workers.Courier;

import java.util.Random;

public abstract class PizzeriaService {
    protected final Thread[] bakers;
    protected final Thread[] couriers;
    protected final OrderQueue orderQueue;
    protected final Storage storage;
    protected final AppConfig config;
    protected final long endTime;
    private final Random random;

    public PizzeriaService(AppConfig config, long endTime) {
        this.bakers = new Thread[config.bakersCount()];
        this.couriers = new Thread[config.couriersCount()];
        this.orderQueue = new OrderQueue();
        this.storage = new Storage(config.storageCapacity());
        this.config = config;
        this.endTime = endTime;
        this.random = new Random();
    }

    protected void workersThreadsStart(){
        int i = 0;
        while (i < config.bakersCount()){
            Baker baker = new Baker(orderQueue, random.nextInt((int) config.workTime()), storage, endTime, i + 1);
            bakers[i] = new Thread(baker);
            bakers[i].start();
            i++;
        }
        i = 0;
        while (i < config.couriersCount()){
            Courier courier = new Courier(storage, random.nextInt((int) config.workTime()),
                    endTime, i + 1, random.nextInt(config.trankSize()));
            couriers[i] = new Thread(courier);
            couriers[i].start();
            i++;
        }
    }

    protected void endThreads(){
        for (Thread baker : bakers) {
            baker.interrupt();
        }
        for (Thread courier : couriers) {
            courier.interrupt();
        }
    }

    public abstract void work() throws InterruptedException;
}
