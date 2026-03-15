package vylegzhanin.pizzeria.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vylegzhanin.pizzeria.model.Order;


@DisplayName("Storage — промежуточное хранилище заказов")
class StorageTest {

    private Storage storage;

    @BeforeEach
    void setUp() {
        storage = new Storage(5);
    }

    // ─── isEmpty ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("isEmpty() возвращает true для пустого хранилища")
    void isEmpty_whenNew_returnsTrue() {
        assertTrue(storage.isEmpty());
    }

    @Test
    @DisplayName("isEmpty() возвращает false после добавления заказа")
    void isEmpty_afterAdd_returnsFalse() {
        storage.add(new Order(1L, 10));
        assertFalse(storage.isEmpty());
    }

    @Test
    @DisplayName("isEmpty() возвращает true после добавления и извлечения заказа")
    void isEmpty_afterAddAndGet_returnsTrue() {
        storage.add(new Order(1L, 10));
        storage.get();
        assertTrue(storage.isEmpty());
    }

    // ─── isFull ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("isFull() возвращает false для пустого хранилища")
    void isFull_whenNew_returnsFalse() {
        assertFalse(storage.isFull());
    }

    @Test
    @DisplayName("isFull() возвращает true когда хранилище заполнено")
    void isFull_whenFull_returnsTrue() {
        for (int i = 1; i <= 5; i++) {
            storage.add(new Order((long) i, i));
        }
        assertTrue(storage.isFull());
    }

    @Test
    @DisplayName("isFull() возвращает false после извлечения заказа из полного хранилища")
    void isFull_afterGetFromFull_returnsFalse() {
        for (int i = 1; i <= 5; i++) {
            storage.add(new Order((long) i, i));
        }
        storage.get();
        assertFalse(storage.isFull());
    }

    // ─── add / get ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("get() возвращает последний добавленный заказ (LIFO)")
    void get_returnsLastAddedOrderLifo() {
        Order first = new Order(1L, 5);
        Order second = new Order(2L, 10);
        storage.add(first);
        storage.add(second);

        assertSame(second, storage.get());
        assertSame(first, storage.get());
    }

    @Test
    @DisplayName("get() возвращает именно тот заказ, который был добавлен")
    void get_returnsSameOrderThatWasAdded() {
        Order order = new Order(42L, 99);
        storage.add(order);
        Order retrieved = storage.get();
        assertEquals(42L, retrieved.id());
        assertEquals(99, retrieved.size());
    }

    // ─── getOrderSize ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("getOrderSize() возвращает размер верхнего заказа без его удаления")
    void getOrderSize_returnsTopOrderSize_withoutRemoving() {
        storage.add(new Order(1L, 7));
        storage.add(new Order(2L, 13));

        assertEquals(13, storage.getOrderSize());
        assertFalse(storage.isEmpty()); // заказ остался
    }

    @Test
    @DisplayName("getOrderSize() не изменяет количество заказов в хранилище")
    void getOrderSize_doesNotChangeSize() {
        storage.add(new Order(1L, 5));
        storage.add(new Order(2L, 3));

        // двукратный вызов getOrderSize не должен изменять стек
        assertEquals(3, storage.getOrderSize());
        assertEquals(3, storage.getOrderSize());

        assertFalse(storage.isEmpty());
        // get() возвращает верхний заказ (LIFO) — тот что был добавлен последним (id=2, size=3)
        assertEquals(3, storage.get().size());
        // теперь на вершине первый заказ (id=1, size=5)
        assertEquals(5, storage.get().size());
        assertTrue(storage.isEmpty());
    }

    // ─── Конкурентный доступ ──────────────────────────────────────────────────

    @Test
    @DisplayName("Конкурентное добавление: все заказы сохраняются без гонок данных")
    void concurrentAdd_allOrdersAreStored() throws InterruptedException {
        Storage bigStorage = new Storage(100);
        int threadCount = 10;
        int ordersPerThread = 10;
        CountDownLatch latch = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            executor.submit(() -> {
                try {
                    latch.await();
                    for (int i = 0; i < ordersPerThread; i++) {
                        bigStorage.add(new Order((long) (threadId * ordersPerThread + i), 1));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        latch.countDown();
        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));

        // 10 потоков × 10 заказов = 100 = ёмкость хранилища → оно заполнено
        assertTrue(bigStorage.isFull());
        // извлечём все заказы и проверим количество
        int count = 0;
        while (!bigStorage.isEmpty()) {
            bigStorage.get();
            count++;
        }
        assertEquals(threadCount * ordersPerThread, count);
    }

    @Test
    @DisplayName("Конкурентное чтение/запись: данные не теряются")
    void concurrentAddAndGet_noDataLoss() throws InterruptedException {
        Storage sharedStorage = new Storage(200);
        int itemCount = 50;

        // Заполняем заранее
        for (int i = 0; i < itemCount; i++) {
            sharedStorage.add(new Order((long) i, 1));
        }

        List<Order> retrieved = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(5);

        for (int t = 0; t < 5; t++) {
            executor.submit(() -> {
                try {
                    latch.await();
                    while (!sharedStorage.isEmpty()) {
                        synchronized (sharedStorage) {
                            if (!sharedStorage.isEmpty()) {
                                Order o = sharedStorage.get();
                                synchronized (retrieved) {
                                    retrieved.add(o);
                                }
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        latch.countDown();
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        assertTrue(sharedStorage.isEmpty());
        assertEquals(itemCount, retrieved.size());
    }
}
