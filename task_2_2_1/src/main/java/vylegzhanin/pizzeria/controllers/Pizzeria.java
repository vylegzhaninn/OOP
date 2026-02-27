package vylegzhanin.pizzeria.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vylegzhanin.pizzeria.configs.AppConfig;

/**
 * Конкретная реализация пиццерии.
 *
 * <p>Запускает генератор заказов и потоки работников, затем удерживает главный поток
 * в ожидании окончания рабочего дня, периодически будя пекарей и курьеров
 * через {@code notify()} на мониторах {@code orderQueue} и {@code storage}.
 * По истечении {@code pizzeriaWorkTime} корректно завершает все потоки.</p>
 */
public class Pizzeria extends AbstractPizzeria {
    private static final Logger log = LoggerFactory.getLogger(Pizzeria.class);

    /**
     * Создаёт пиццерию с заданной конфигурацией.
     *
     * <p>Время окончания работы вычисляется как
     * {@code System.currentTimeMillis() + config.pizzeriaWorkTime()}.</p>
     *
     * @param config конфигурация пиццерии
     */
    public Pizzeria(AppConfig config) {
        super(config, System.currentTimeMillis() + config.pizzeriaWorkTime());
    }

    /**
     * Запускает пиццерию и ожидает окончания рабочего дня.
     *
     * <p>Алгоритм:
     * <ol>
     *   <li>Стартует {@link OrderGenerator} в отдельном потоке.</li>
     *   <li>Запускает потоки пекарей и курьеров через {@link #workersThreadsStart()}.</li>
     *   <li>Каждые 1 секунду будит пекарей и курьеров, которые могут ожидать
     *       на мониторах {@code orderQueue} и {@code storage}.</li>
     *   <li>По достижении {@code endTime} останавливает генератор заказов
     *       и прерывает все рабочие потоки через {@link #endThreads()}.</li>
     * </ol>
     * </p>
     *
     * @throws InterruptedException если главный поток был прерван во время ожидания
     */
    public void work() throws InterruptedException {
        OrderGenerator orderGenerator = new OrderGenerator(config.orderInterval(), orderQueue, config);
        Thread generatorThread = new Thread(orderGenerator);
        generatorThread.start();
        workersThreadsStart();

        while (System.currentTimeMillis() < endTime) {
            Thread.sleep(1000);
            synchronized (orderQueue) {
                orderQueue.notify();
            }
            synchronized (storage) {
                storage.notify();
            }
        }
        log.info("Время работы пиццерии закончилось");

        orderGenerator.endGenerating();
        endThreads();
        log.info("Пиццерия закрылась");
    }
}
