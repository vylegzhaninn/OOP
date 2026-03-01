package vylegzhanin.pizzeria.model;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vylegzhanin.pizzeria.model.workers.Baker;
import vylegzhanin.pizzeria.repositories.OrderQueue;
import vylegzhanin.pizzeria.repositories.Storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Baker — пекарь.")
class BakerTest {

    /**
     * Вспомогательный метод: создаёт Baker с endTime = now + ttl.
     */
    private Baker baker(OrderQueue queue, Storage storage, long operatingTime, long ttlMs) {
        return new Baker(queue, operatingTime, storage, System.currentTimeMillis() + ttlMs, 1);
    }

    // ─── handleOrder ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("handleOrder() кладёт заказ в хранилище")
    void handleOrder_placesOrderIntoStorage() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        Storage storage = new Storage(5);
        Baker b = baker(queue, storage, 50, 5000);

        Order order = new Order(1L, 5);
        queue.offer(order);
        synchronized (queue) {
            queue.notifyAll();
        }

        Thread bakerThread = new Thread(b);
        bakerThread.start();

        long deadline = System.currentTimeMillis() + 3000;
        while (storage.isEmpty() && System.currentTimeMillis() < deadline) {
            Thread.sleep(50);
        }
        bakerThread.interrupt();
        bakerThread.join(1000);

        assertFalse(storage.isEmpty(), "Пекарь должен был положить заказ в хранилище");
        assertEquals(order.id(), storage.get().id());
    }

    @Test
    @DisplayName("Пекарь обрабатывает несколько заказов подряд")
    void baker_processesMultipleOrders() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        Storage storage = new Storage(10);
        Baker b = baker(queue, storage, 10, 5000);

        int count = 5;
        for (int i = 1; i <= count; i++) {
            queue.offer(new Order((long) i, i));
        }
        synchronized (queue) {
            queue.notifyAll();
        }

        Thread bakerThread = new Thread(b);
        bakerThread.start();

        long deadline = System.currentTimeMillis() + 5000;
        while ((storage.isEmpty() || !queue.isEmpty())
                && System.currentTimeMillis() < deadline) {
            Thread.sleep(50);
        }
        Thread.sleep(300);
        bakerThread.interrupt();
        bakerThread.join(1000);

        int stored = 0;
        while (!storage.isEmpty()) {
            storage.get();
            stored++;
        }
        assertEquals(count, stored, "Пекарь должен положить все заказы на склад");
    }

    // ─── Ожидание / пробуждение ───────────────────────────────────────────────

    @Test
    @DisplayName("Пекарь ждёт, пока в очереди нет заказов, затем обрабатывает новый")
    void baker_waitsUntilOrderAppears() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        Storage storage = new Storage(5);
        Baker b = baker(queue, storage, 10, 5000);

        Thread bakerThread = new Thread(b);
        bakerThread.start();
        Thread.sleep(100);

        assertTrue(storage.isEmpty(), "До появления заказа хранилище должно быть пустым");

        Order order = new Order(99L, 7);
        queue.offer(order);
        synchronized (queue) {
            queue.notifyAll();
        }

        long deadline = System.currentTimeMillis() + 3000;
        while (storage.isEmpty() && System.currentTimeMillis() < deadline) {
            Thread.sleep(50);
        }
        bakerThread.interrupt();
        bakerThread.join(1000);

        assertFalse(storage.isEmpty(), "После появления заказа пекарь должен обработать его");
    }

    // ─── endTime ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Пекарь не обрабатывает заказ, если endTime уже истёк")
    void baker_doesNotWork_ifEndTimeExpired() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        Storage storage = new Storage(5);
        Baker b = new Baker(queue, 50, storage, System.currentTimeMillis() - 1, 1);

        Order order = new Order(1L, 5);
        queue.offer(order);
        synchronized (queue) {
            queue.notifyAll();
        }

        Thread bakerThread = new Thread(b);
        bakerThread.start();
        bakerThread.join(TimeUnit.SECONDS.toMillis(2));

        assertTrue(storage.isEmpty(),
                "Пекарь не должен класть заказ на склад, если рабочее время истекло");
    }
}
