package vylegzhanin.pizzeria.controllers;

import java.util.Random;

import vylegzhanin.pizzeria.configs.AppConfig;
import vylegzhanin.pizzeria.model.workers.Baker;
import vylegzhanin.pizzeria.model.workers.Courier;
import vylegzhanin.pizzeria.repositories.OrderQueue;
import vylegzhanin.pizzeria.repositories.Storage;

/**
 * Базовый класс пиццерии, содержащий общую инфраструктуру для управления работниками.
 *
 * <p>Хранит ссылки на разделяемые ресурсы ({@link OrderQueue}, {@link Storage}),
 * конфигурацию и потоки работников. Подклассы реализуют метод {@link #work()},
 * определяя конкретную логику работы пиццерии.</p>
 */
public abstract class AbstractPizzeria {
    /**
     * Потоки пекарей, работающих в пиццерии.
     */
    protected final Thread[] bakers;

    /**
     * Потоки курьеров, работающих в пиццерии.
     */
    protected final Thread[] couriers;

    /**
     * Очередь заказов, доступная для пекарей и курьеров.
     */
    protected final OrderQueue orderQueue;

    /**
     * Хранилище готовых заказов.
     */
    protected final Storage storage;

    /**
     * Конфигурация пиццерии.
     */
    protected final AppConfig config;

    /**
     * Время завершения работы пиццерии (мс с эпохи Unix).
     */
    protected final long endTime;
    private final Random random;

    /**
     * Инициализирует общую инфраструктуру пиццерии.
     *
     * @param config  конфигурация пиццерии
     * @param endTime момент завершения работы (мс с эпохи Unix)
     */
    public AbstractPizzeria(AppConfig config, long endTime) {
        this.bakers = new Thread[config.bakersCount()];
        this.couriers = new Thread[config.couriersCount()];
        this.orderQueue = new OrderQueue();
        this.storage = new Storage(config.storageCapacity());
        this.config = config;
        this.endTime = endTime;
        this.random = new Random();
    }

    /**
     * Создаёт и запускает потоки для всех пекарей и курьеров.
     *
     * <p>Каждому работнику назначается случайное время выполнения за��аза
     * в диапазоне {@code [0, workTime)} мс.</p>
     */
    protected void workersThreadsStart() {
        int i = 0;
        while (i < config.bakersCount()) {
            Baker baker = new Baker(orderQueue, random.nextInt((int) config.workTime()),
                    storage, endTime, i + 1);
            bakers[i] = new Thread(baker);
            bakers[i].start();
            i++;
        }
        i = 0;
        while (i < config.couriersCount()) {
            Courier courier = new Courier(storage, random.nextInt((int) config.workTime()),
                    endTime, i + 1, random.nextInt(config.trankSize()));
            couriers[i] = new Thread(courier);
            couriers[i].start();
            i++;
        }
    }

    /**
     * Прерывает потоки всех работников (пекарей и курьеров).
     *
     * <p>Вызывается после окончания рабочего времени пиццерии, чтобы
     * разблокировать потоки, ожидающих на {@code wait()}.</p>
     */
    protected void endThreads() {
        for (Thread baker : bakers) {
            baker.interrupt();
        }
        for (Thread courier : couriers) {
            courier.interrupt();
        }
    }

    /**
     * Запускает основной цикл работы пиццерии.
     *
     * <p>Реализация должна стартовать генератор заказов и потоки работников,
     * ожидать окончания {@code endTime}, после чего корректно завершить работу.</p>
     *
     * @throws InterruptedException если главный поток пиццерии был прерван
     */
    public abstract void work() throws InterruptedException;
}
