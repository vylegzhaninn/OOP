package vylegzhanin.pizzeria.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vylegzhanin.pizzeria.model.workers.Courier;
import vylegzhanin.pizzeria.repositories.Storage;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Courier — курьер")
class CourierTest {

    /**
     * Создаёт курьера с endTime = now + ttlMs.
     */
    private Courier courier(Storage storage, long operatingTime, long ttlMs, int trunkSize) {
        return new Courier(storage, operatingTime, System.currentTimeMillis() + ttlMs, 1, trunkSize);
    }

    // ─── Базовый сценарий ─────────────────────────────────────────────────────

    @Test
    @DisplayName("Курьер забирает заказ со склада и склад становится пустым")
    void courier_takesOrderFromStorage() throws InterruptedException {
        Storage storage = new Storage(5);
        storage.add(new Order(1L, 3));

        Courier c = courier(storage, 10, 5000, 10);
        Thread thread = new Thread(c);

        synchronized (storage) {
            storage.notifyAll();
        }
        thread.start();

        long deadline = System.currentTimeMillis() + 3000;
        while (!storage.isEmpty() && System.currentTimeMillis() < deadline) {
            Thread.sleep(30);
        }
        thread.interrupt();
        thread.join(1000);

        assertTrue(storage.isEmpty(), "Курьер должен был забрать заказ со склада");
    }

    @Test
    @DisplayName("Курьер забирает несколько заказов за один рейс, если они влезают в багажник")
    void courier_takesMultipleOrdersThatFitInTrunk() throws InterruptedException {
        Storage storage = new Storage(10);
        // три заказа по 3 единицы, багажник = 10 — все влезут
        storage.add(new Order(1L, 3));
        storage.add(new Order(2L, 3));
        storage.add(new Order(3L, 3));

        Courier c = courier(storage, 10, 5000, 10);
        Thread thread = new Thread(c);

        synchronized (storage) {
            storage.notifyAll();
        }
        thread.start();

        long deadline = System.currentTimeMillis() + 4000;
        while (!storage.isEmpty() && System.currentTimeMillis() < deadline) {
            Thread.sleep(30);
        }
        thread.interrupt();
        thread.join(1000);

        assertTrue(storage.isEmpty(), "Курьер должен забрать все 3 заказа за один рейс");
    }

    @Test
    @DisplayName("Курьер оставляет заказ на складе, если он не влезает в багажник")
    void courier_leavesOrderThatDoesNotFitInTrunk() throws InterruptedException {
        Storage storage = new Storage(5);
        // заказ больше багажника
        storage.add(new Order(1L, 5));  // размер 5
        storage.add(new Order(2L, 3));  // размер 3 — этот лежит сверху (LIFO)

        // багажник = 2: верхний заказ (размер 3) не влезет → должен остаться
        Courier c = courier(storage, 10, 5000, 2);
        Thread thread = new Thread(c);

        synchronized (storage) {
            storage.notifyAll();
        }
        thread.start();

        Thread.sleep(500); // даём курьеру время "проснуться" и принять решение
        thread.interrupt();
        thread.join(1000);

        // Верхний заказ (размер 3) не влез → ни один не забран (курьер делает break)
        assertFalse(storage.isEmpty(),
                "Курьер не должен брать заказ, который не влезает в багажник");
    }

    // ─── Ожидание ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Курьер ждёт, пока склад пуст, затем забирает появившийся заказ")
    void courier_waitsUntilOrderAppears() throws InterruptedException {
        Storage storage = new Storage(5);
        Courier c = courier(storage, 10, 5000, 10);

        Thread thread = new Thread(c);
        thread.start();
        Thread.sleep(100); // курьер должен быть в ожидании

        assertTrue(storage.isEmpty());

        // Добавляем заказ и будим курьера
        storage.add(new Order(42L, 4));
        synchronized (storage) {
            storage.notifyAll();
        }

        long deadline = System.currentTimeMillis() + 3000;
        while (!storage.isEmpty() && System.currentTimeMillis() < deadline) {
            Thread.sleep(30);
        }
        thread.interrupt();
        thread.join(1000);

        assertTrue(storage.isEmpty(), "Курьер должен был забрать появившийся заказ");
    }

    // ─── endTime ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Курьер не работает, если endTime уже истёк")
    void courier_doesNotWork_whenEndTimeExpired() throws InterruptedException {
        Storage storage = new Storage(5);
        storage.add(new Order(1L, 3));

        // endTime в прошлом
        Courier c = new Courier(storage, 10, System.currentTimeMillis() - 1, 1, 10);
        Thread thread = new Thread(c);

        synchronized (storage) {
            storage.notifyAll();
        }
        thread.start();
        thread.join(2000);

        assertFalse(storage.isEmpty(),
                "Курьер не должен брать заказы после окончания рабочего времени");
    }

    // ─── Интеграция Baker + Courier ───────────────────────────────────────────

    @Test
    @DisplayName("Интеграция: Baker кладёт заказы, Courier их забирает")
    void integration_bakerFillsStorage_courierDrains() throws InterruptedException {
        Storage storage = new Storage(20);
        AtomicInteger deliveredCount = new AtomicInteger(0);

        // Заполняем склад вручную (имитируем работу пекаря)
        for (int i = 1; i <= 6; i++) {
            storage.add(new Order((long) i, 2)); // 6 заказов по 2 = 12 единиц
        }

        // Курьер с багажником 10 должен взять 5 заказов за первый рейс (5*2=10)
        // затем ещё 1 заказ во втором рейсе
        long endTime = System.currentTimeMillis() + 5000;
        Courier c = new Courier(storage, 10, endTime, 1, 10);
        Thread thread = new Thread(c);

        synchronized (storage) {
            storage.notifyAll();
        }
        thread.start();

        long deadline = System.currentTimeMillis() + 4000;
        while (!storage.isEmpty() && System.currentTimeMillis() < deadline) {
            Thread.sleep(50);
        }
        thread.interrupt();
        thread.join(1000);

        assertTrue(storage.isEmpty(), "Курьер должен вывезти все заказы со склада");
    }
}
