package vylegzhanin.pizzeria.model.workers;

import vylegzhanin.pizzeria.model.Order;
import vylegzhanin.pizzeria.repositories.OrderQueue;
import vylegzhanin.pizzeria.repositories.Storage;

/**
 * Курьер — работник, который забирает готовые заказы со склада и доставляет их заказчикам.
 *
 * <p>Курьер блокируется на мониторе {@link Storage}, пока склад пуст, затем
 * набирает заказы до исчерпания вместимости багажника ({@code trunkSize}) и
 * доставляет их, тратя {@code operatingTime} мс на каждый заказ.</p>
 *
 * <p>В отличие от {@link Baker}, курьер переопределяет метод {@link #run()},
 * дополнительно логируя объём своего багажника при старте.</p>
 */
public class Courier extends Worker {
    private final int trankSize;

    /**
     * Создаёт курьера с заданными параметрами.
     *
     * @param storage       общее хранилище готовых заказов
     * @param operatingTime время доставки одного заказа, мс
     * @param endTime       момент окончания рабочего дня (мс с эпохи Unix)
     * @param id            порядковый номер курьера (для логирования)
     * @param trankSize     вместимость багажника (максимальная суммарная масса заказов за рейс)
     */
    public Courier(Storage storage, long operatingTime, long endTime, int id, int trankSize) {
        super(operatingTime, storage, endTime, id);
        this.trankSize = trankSize;
    }

    /**
     * Логирует факт доставки заказа заказчику.
     *
     * @param order доставленный заказ
     */
    @Override
    protected void handleOrder(Order order) {
        log.info("{} № {} отдал заказчику заказ с id: {}", getClass().getSimpleName(), id, order.id());
    }

    /**
     * Ожидает появления заказов на складе, набирает их в багажник и доставляет.
     *
     * <p>Алгоритм:
     * <ol>
     *   <li>Блокируется на мониторе {@code storage} через {@code wait()}, пока склад пуст.</li>
     *   <li>Набирает заказы, пока склад непуст и в багажнике есть место
     *       (размер следующего заказа не превышает оставшуюся вместимость).</li>
     *   <li>Освобождает блокировку и доставляет каждый набранный заказ
     *       через {@link #work(OrderQueue)}.</li>
     * </ol>
     * </p>
     *
     * @throws InterruptedException если поток был прерван во время ожидания
     */
    @Override
    public void waitingForOrder() throws InterruptedException {
        OrderQueue orders = new OrderQueue();
        int tmpTrankSize = trankSize;

        synchronized (storage) {
            if (storage.isEmpty()) {
                log.info("Courier № {} ожидает заказ", id);
            }
            while (storage.isEmpty()) {
                storage.wait();
            }

            while (!storage.isEmpty() && tmpTrankSize > 0) {
                int orderSize = storage.getOrderSize();
                if (orderSize <= tmpTrankSize) {
                    Order order = storage.get();
                    tmpTrankSize -= orderSize;
                    orders.offer(order);
                    log.info("Courier № {} взял заказ с id: {} и массой: {}",
                            id, order.id(), order.size());
                } else {
                    break;
                }
            }
        }
        work(orders);
    }

    /**
     * Точка входа потока курьера.
     *
     * <p>Аналогична базовой реализации {@link Worker#run()}, но дополнительно
     * выводит в лог объём багажника курьера при старте.</p>
     */
    @Override
    public void run() {
        try {
            log.info("{} {} начал работу с объёмом багажника {} и временем работы {} мс",
                    getClass().getSimpleName(), id, trankSize, operatingTime);
            while (!Thread.currentThread().isInterrupted() && System.currentTimeMillis() < endTime) {
                waitingForOrder();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
