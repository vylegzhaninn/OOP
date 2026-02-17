package vylegzhanin.pizzeria.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vylegzhanin.pizzeria.configs.AppConfig;
import vylegzhanin.pizzeria.service.PizzeriaService;

public class Pizzeria extends PizzeriaService {
    private static final Logger log = LoggerFactory.getLogger(Pizzeria.class);

    public Pizzeria(AppConfig config){
        super(config, System.currentTimeMillis() + config.pizzeriaWorkTime());
    }

    public void work() throws InterruptedException {
        OrderGenerator orderGenerator = new OrderGenerator(config.orderInterval(), orderQueue, config);
        Thread generatorThread = new Thread(orderGenerator);
        generatorThread.start();
        workersThreadsStart();

        while (System.currentTimeMillis() < endTime) {
            Thread.sleep(1000);
            synchronized (orderQueue){
                orderQueue.notify();
            }

            synchronized (storage){
                storage.notify();
            }
        }
        log.info("Время работы пиццерии закончилось");

        orderGenerator.endGenerating();
        endThreads();
        log.info("Пиццерия закрылась");
    }
}
