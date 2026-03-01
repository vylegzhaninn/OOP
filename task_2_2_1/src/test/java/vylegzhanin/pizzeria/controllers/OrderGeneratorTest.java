package vylegzhanin.pizzeria.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vylegzhanin.pizzeria.configs.AppConfig;
import vylegzhanin.pizzeria.model.Order;
import vylegzhanin.pizzeria.repositories.OrderQueue;


@DisplayName("OrderGenerator — генератор заказов")
class OrderGeneratorTest {

    /** Стандартный конфиг для тестов: trunkSize=20, интервал=intervalMs. */
    private AppConfig cfg(long intervalMs) {
        return new AppConfig(1, 1, 5, 500, 20, intervalMs, 10000);
    }

    // ─── Базовая генерация ────────────────────────────────────────────────────

    @Test
    @DisplayName("Генератор добавляет заказы в очередь с заданным интервалом")
    void generator_producesOrdersIntoQueue() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        OrderGenerator generator = new OrderGenerator(100, queue, cfg(100));
        Thread thread = new Thread(generator);
        thread.start();

        // Ждём ~500 мс — за это время должно появиться ≥ 4 заказов
        Thread.sleep(500);
        generator.endGenerating();   // сбрасываем флаг isAlive
        thread.interrupt();          // прерываем именно поток генератора
        thread.join(1000);

        assertFalse(queue.isEmpty(), "Генератор должен был добавить хотя бы один заказ в очередь");
    }

    @Test
    @DisplayName("Генератор создаёт заказы с размером в диапазоне [0, trunkSize)")
    void generator_createsOrdersWithValidSize() throws InterruptedException {
        int trunkSize = 15;
        OrderQueue queue = new OrderQueue();
        AppConfig config = new AppConfig(1, 1, 5, 500, trunkSize, 50, 10000);

        OrderGenerator generator = new OrderGenerator(50, queue, config);
        Thread thread = new Thread(generator);
        thread.start();

        Thread.sleep(400);
        generator.endGenerating();
        thread.interrupt();
        thread.join(1000);

        List<Order> orders = new ArrayList<>();
        while (!queue.isEmpty()) {
            orders.add(queue.poll());
        }

        assertFalse(orders.isEmpty());
        orders.forEach(o -> assertTrue(o.size() >= 0 && o.size() < trunkSize,
                "Размер заказа должен быть в диапазоне [0, trunkSize), но был: " + o.size()));
    }

    @Test
    @DisplayName("endGenerating() останавливает генерацию новых заказов")
    void endGenerating_stopsProduction() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        OrderGenerator generator = new OrderGenerator(100, queue, cfg(100));
        Thread thread = new Thread(generator);
        thread.start();

        Thread.sleep(350);
        generator.endGenerating();
        thread.interrupt();
        thread.join(1000);

        // Дренируем очередь после остановки
        while (!queue.isEmpty()) {
            queue.poll();
        }

        Thread.sleep(300); // ждём ещё — новых заказов не должно появиться
        int countAfterDelay = 0;
        while (!queue.isEmpty()) {
            queue.poll();
            countAfterDelay++;
        }

        assertEquals(0, countAfterDelay,
                "После endGenerating() новых заказов поступать не должно");
    }

    @Test
    @DisplayName("Генератор присваивает заказам уникальные нарастающие id")
    void generator_assignsIncrementalOrderIds() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        OrderGenerator generator = new OrderGenerator(50, queue, cfg(50));
        Thread thread = new Thread(generator);
        thread.start();

        Thread.sleep(300);
        generator.endGenerating();
        thread.interrupt();
        thread.join(1000);

        List<Order> orders = new ArrayList<>();
        while (!queue.isEmpty()) {
            orders.add(queue.poll());
        }

        assertTrue(orders.size() >= 2, "Должно быть минимум 2 заказа для проверки нарастания id");
        for (int i = 0; i < orders.size() - 1; i++) {
            assertTrue(orders.get(i).id() < orders.get(i + 1).id(),
                    "id должны быть строго возрастающими");
        }
    }

    // ─── Поведение run() при InterruptedException ─────────────────────────────

    @Test
    @DisplayName("run() корректно завершается при прерывании потока")
    void run_finishesGracefully_onInterrupt() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        AtomicBoolean threadFinished = new AtomicBoolean(false);
        CountDownLatch latch = new CountDownLatch(1);
        OrderGenerator generator = new OrderGenerator(200, queue, cfg(200));

        Thread thread = new Thread(() -> {
            generator.run();
            threadFinished.set(true);
            latch.countDown();
        });
        thread.start();
        Thread.sleep(50);
        thread.interrupt();

        assertTrue(latch.await(2, TimeUnit.SECONDS),
                "Поток генератора должен завершиться после прерывания");
        assertTrue(threadFinished.get());
    }
}
