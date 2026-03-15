package vylegzhanin.pizzeria.model.workers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vylegzhanin.pizzeria.model.Order;
import vylegzhanin.pizzeria.repositories.OrderQueue;
import vylegzhanin.pizzeria.repositories.Storage;

/**
 * Абстрактный базовый класс для всех работников пиццерии.
 *
 * <p>Реализует {@link Runnable}: в методе {@link #run()} поток крутится
 * в цикле, вызывая {@link #waitingForOrder()}, пока не истечёт
 * {@code endTime} или поток не будет прерван.</p>
 *
 * <p>Конкретные подклассы ({@code Baker}, {@code Courier}) определяют:
 * <ul>
 *   <li>{@link #waitingForOrder()} — логику ожидания и получения заказа;</li>
 *   <li>{@link #handleOrder(Order)} — что делать с заказом по завершении работы.</li>
 * </ul>
 */
public abstract class Worker implements Runnable {
    /**
     * Логгер для вывода информации о работе работника.
     */
    protected final Logger log = LoggerFactory.getLogger(getClass());
    /**
     * Время выполнения одного заказа в миллисекундах.
     */
    protected final long operatingTime;
    /**
     * Общее хранилище готовых заказов.
     */
    protected final Storage storage;
    /**
     * Момент окончания рабочего дня (мс с эпохи Unix).
     */
    protected final long endTime;
    /**
     * Порядковый номер работника.
     */
    protected final int id;

    /**
     * Создаёт работника с заданными параметрами.
     *
     * @param operatingTime время выполнения одного заказа, мс
     * @param storage       общее хранилище готовых заказов
     * @param endTime       момент окончания рабочего дня (мс с эпохи Unix)
     * @param id            порядковый номер работника
     */
    public Worker(long operatingTime, Storage storage, long endTime, int id) {
        this.operatingTime = operatingTime;
        this.storage = storage;
        this.endTime = endTime;
        this.id = id;
    }

    /**
     * Выполняет финальное действие над заказом после завершения работы по нему.
     *
     * @param order заказ для обработки
     */
    protected abstract void handleOrder(Order order);

    /**
     * Обрабатывает один заказ: логирует начало работы, ждёт {@code operatingTime} мс
     * и вызывает {@link #handleOrder(Order)}.
     *
     * <p>Если до конца рабочего дня остаётся ��еньше {@code operatingTime + 100} мс,
     * поток прерывает сам себя, чтобы корректно завершиться.</p>
     *
     * @param order заказ для обработки
     * @throws InterruptedException если поток был прерван во время ожидания
     */
    protected final void work(Order order) throws InterruptedException {
        if (System.currentTimeMillis() + operatingTime + 100 < endTime) {
            log.info("{} № {} выполняет заказ с id: {}",
                getClass().getSimpleName(), id, order.id());
            Thread.sleep(operatingTime);
            handleOrder(order);
        } else {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Обрабатывает пачку заказов из временной очереди курьера.
     *
     * <p>Для каждого заказа из {@code orders} ждёт {@code operatingTime} мс
     * и вызывает {@link #handleOrder(Order)}.</p>
     *
     * @param orders временная очередь заказов, набранных курьером
     * @throws InterruptedException если поток был прерван во время ожидания
     */
    protected void work(OrderQueue orders) throws InterruptedException {
        while (!orders.isEmpty()) {
            Order order = orders.poll();
            if (order != null) {
                Thread.sleep(operatingTime);
                handleOrder(order);
            }
        }
    }

    /**
     * Ожидает появления доступного заказа, забирает его и передаёт на выполнение.
     *
     * <p>Реализация должна блокироваться на мониторе источника заказов
     * (очереди или хранилища) до тех пор, пока заказ не появится.</p>
     *
     * @throws InterruptedException если поток был прерван во время ожидания
     */
    protected abstract void waitingForOrder() throws InterruptedException;

    /**
     * Точка входа потока работника.
     *
     * <p>Запускает цикл обработки заказов, который выполняется до истечения
     * {@code endTime} или прерывания потока. При получении
     * {@link InterruptedException} восстанавливает флаг прерывания.</p>
     */
    @Override
    public void run() {
        try {
            log.info("{} {} начал работу с временем работы {} мс",
                getClass().getSimpleName(), id, operatingTime);
            while (!Thread.currentThread().isInterrupted()
                && System.currentTimeMillis() < endTime) {
                waitingForOrder();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
