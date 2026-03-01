package vylegzhanin.pizzeria.controllers;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vylegzhanin.pizzeria.configs.AppConfig;
import vylegzhanin.pizzeria.model.Order;
import vylegzhanin.pizzeria.repositories.OrderQueue;

/**
 * Генератор входящих заказов пиццерии.
 *
 * <p>Запускается в отдельном потоке и периодически создаёт новые {@link Order}
 * со случайной массой, помещая их в {@link OrderQueue}. После каждого добавления
 * уведомляет ожидающих пекарей через {@code notifyAll()} на мониторе очереди.</p>
 *
 * <p>Генерация останавливается вызовом {@link #endGenerating()}.</p>
 */
public class OrderGenerator implements Runnable {
    private final long orderInterval;
    private final OrderQueue orderQueue;
    private boolean isAlive;
    private static final Logger log = LoggerFactory.getLogger(OrderGenerator.class);
    private final AppConfig config;
    private final Random random;

    /**
     * Создаёт генератор заказов с заданными параметрами.
     *
     * @param orderInterval интервал между заказами, мс
     * @param orderQueue    очередь, в которую помещаются новые заказы
     * @param config        конфигурация пиццерии
     */
    public OrderGenerator(long orderInterval, OrderQueue orderQueue, AppConfig config) {
        this.orderInterval = orderInterval;
        this.orderQueue = orderQueue;
        this.isAlive = true;
        this.config = config;
        this.random = new Random();
    }

    /**
     * Основной цикл генерации заказов.
     *
     * <p>Каждые {@code orderInterval} мс создаёт новый заказ со случайной массой
     * в диапазоне {@code [0, trunkSize)}, добавляет его в очередь и уведомляет
     * ожидающих пекарей.</p>
     *
     * @throws InterruptedException если поток был прерван во время ожидания
     */
    public void generateOrders() throws InterruptedException {
        long orderId = 1;
        while (isAlive) {
            Thread.sleep(orderInterval);
            Order order = new Order(orderId++, random.nextInt(config.trankSize()));
            log.info("Поступил новый заказ {} с массой {}", orderId, order.size());
            orderQueue.offer(order);
        }
    }

    /**
     * Точка входа потока генератора.
     *
     * <p>Вызывает {@link #generateOrders()}. При возникновении
     * {@link InterruptedException} логирует ошибку и восстанавливает флаг прерывания.</p>
     */
    @Override
    public void run() {
        try {
            log.info("Заказы поступают");
            generateOrders();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Генератор заказов был прерван: {}", e.getMessage());
        }
    }

    /**
     * Останавливает генерацию заказов.
     *
     * <p>Устанавливает флаг {@code isALive} в {@code false} и прерывает
     * текущий поток, если он заблокирован в {@code Thread.sleep()}.</p>
     */
    public void endGenerating() {
        isAlive = false;
        log.info("Заказы перестали поступать");
    }
}
