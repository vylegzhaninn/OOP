package vylegzhanin.pizzeria.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vylegzhanin.pizzeria.model.Order;


@DisplayName("OrderQueue — очередь входящих заказов")
class OrderQueueTest {

    private OrderQueue queue;

    @BeforeEach
    void setUp() {
        queue = new OrderQueue();
    }

    // ─── isEmpty ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("isEmpty() возвращает true для новой очереди")
    void isEmpty_whenNew_returnsTrue() {
        assertTrue(queue.isEmpty());
    }

    @Test
    @DisplayName("isEmpty() возвращает false после добавления заказа")
    void isEmpty_afterOffer_returnsFalse() {
        queue.offer(new Order(1L, 5));
        assertFalse(queue.isEmpty());
    }

    @Test
    @DisplayName("isEmpty() возвращает true после того как единственный заказ извлечён")
    void isEmpty_afterPollLastElement_returnsTrue() {
        queue.offer(new Order(1L, 5));
        queue.poll();
        assertTrue(queue.isEmpty());
    }

    // ─── offer / poll ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("poll() возвращает null для пустой очереди")
    void poll_onEmptyQueue_returnsNull() {
        assertNull(queue.poll());
    }

    @Test
    @DisplayName("Очередь работает по принципу FIFO")
    void offer_and_poll_respectFifo() {
        Order first = new Order(1L, 5);
        Order second = new Order(2L, 10);
        Order third = new Order(3L, 15);

        queue.offer(first);
        queue.offer(second);
        queue.offer(third);

        assertSame(first, queue.poll());
        assertSame(second, queue.poll());
        assertSame(third, queue.poll());
        assertTrue(queue.isEmpty());
    }

    @Test
    @DisplayName("poll() возвращает корректные данные заказа")
    void poll_returnsCorrectOrderData() {
        queue.offer(new Order(77L, 42));
        Order result = queue.poll();

        assertNotNull(result);
        assertEquals(77L, result.id());
        assertEquals(42, result.size());
        assertNotNull(result.reservationTime());
    }

    @Test
    @DisplayName("Несколько операций offer/poll подряд работают корректно")
    void multipleOffersAndPolls_workCorrectly() {
        for (int i = 1; i <= 10; i++) {
            queue.offer(new Order((long) i, i * 2));
        }
        for (int i = 1; i <= 10; i++) {
            Order order = queue.poll();
            assertNotNull(order);
            assertEquals((long) i, order.id());
            assertEquals(i * 2, order.size());
        }
        assertTrue(queue.isEmpty());
    }

    // ─── Конкурентный доступ ──────────────────────────────────────────────────

    @Test
    @DisplayName("Конкурентное добавление: все заказы сохраняются")
    void concurrentOffer_allOrdersStored() throws InterruptedException {
        int threadCount = 10;
        int ordersPerThread = 100;
        CountDownLatch latch = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int t = 0; t < threadCount; t++) {
            final int offset = t * ordersPerThread;
            executor.submit(() -> {
                try {
                    latch.await();
                    for (int i = 0; i < ordersPerThread; i++) {
                        queue.offer(new Order((long) (offset + i), 1));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        latch.countDown();
        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));

        int count = 0;
        while (!queue.isEmpty()) {
            queue.poll();
            count++;
        }
        assertEquals(threadCount * ordersPerThread, count);
    }

    @Test
    @DisplayName("Конкурентный producer-consumer: ни один заказ не теряется")
    void concurrentProducerConsumer_noOrdersLost() throws InterruptedException {
        int totalOrders = 200;
        AtomicInteger produced = new AtomicInteger(0);
        AtomicInteger consumed = new AtomicInteger(0);
        CountDownLatch done = new CountDownLatch(2);

        // Producer
        Thread producer = new Thread(() -> {
            for (int i = 0; i < totalOrders; i++) {
                queue.offer(new Order((long) i, 1));
                produced.incrementAndGet();
                synchronized (queue) {
                    queue.notifyAll();
                }
            }
            done.countDown();
        });

        // Consumer
        Thread consumer = new Thread(() -> {
            int got = 0;
            while (got < totalOrders) {
                synchronized (queue) {
                    while (queue.isEmpty()) {
                        try {
                            queue.wait(100);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    Order o = queue.poll();
                    if (o != null) {
                        consumed.incrementAndGet();
                        got++;
                    }
                }
            }
            done.countDown();
        });

        producer.start();
        consumer.start();
        assertTrue(done.await(10, TimeUnit.SECONDS));

        assertEquals(totalOrders, produced.get());
        assertEquals(totalOrders, consumed.get());
        assertTrue(queue.isEmpty());
    }
}
