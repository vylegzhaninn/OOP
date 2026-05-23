package server;

import common.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkerHandlerTest {

    private ServerSocket serverSocket;
    private Socket workerSide;
    private Socket serverSide;

    @BeforeEach
    void setUp() throws Exception {
        serverSocket = new ServerSocket(0);
        Thread accept = new Thread(() -> {
            try {
                serverSide = serverSocket.accept();
            } catch (Exception ignored) {
            }
        });
        accept.start();
        workerSide = new Socket("localhost", serverSocket.getLocalPort());
        accept.join(2000);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (workerSide != null && !workerSide.isClosed()) workerSide.close();
        if (serverSide != null && !serverSide.isClosed()) serverSide.close();
        if (serverSocket != null && !serverSocket.isClosed()) serverSocket.close();
    }

    @Test
    @Timeout(15)
    void happyPath_compositeFound_decrementsCounterAndSetsFlag() throws Exception {
        BlockingQueue<Task> queue = new LinkedBlockingQueue<>();
        AtomicBoolean found = new AtomicBoolean(false);
        AtomicInteger remaining = new AtomicInteger(1);
        queue.offer(new Task(42, new int[]{6, 7, 8}));

        Thread handler = new Thread(new WorkerHandler(serverSide, queue, found, remaining));
        handler.start();

        try (
            ObjectInputStream in = new ObjectInputStream(workerSide.getInputStream());
            PrintWriter out = new PrintWriter(workerSide.getOutputStream(), true)
        ) {
            Task received = (Task) in.readObject();
            assertEquals(42, received.id());

            out.println("ack 42");
            out.println("beep 42");

            handler.join(5000);
        }

        assertTrue(found.get(), "compositeFound должен быть выставлен");
        assertEquals(0, remaining.get(), "remainingTasks должен декрементироваться");
        assertTrue(queue.isEmpty(), "очередь должна остаться пустой");
    }

    @Test
    @Timeout(15)
    void happyPath_noCompositeFound_decrementsCounterOnly() throws Exception {
        BlockingQueue<Task> queue = new LinkedBlockingQueue<>();
        AtomicBoolean found = new AtomicBoolean(false);
        AtomicInteger remaining = new AtomicInteger(1);
        queue.offer(new Task(7, new int[]{2, 3, 5, 11}));

        Thread handler = new Thread(new WorkerHandler(serverSide, queue, found, remaining));
        handler.start();

        try (
            ObjectInputStream in = new ObjectInputStream(workerSide.getInputStream());
            PrintWriter out = new PrintWriter(workerSide.getOutputStream(), true)
        ) {
            Task received = (Task) in.readObject();
            assertEquals(7, received.id());

            out.println("ack 7");
            out.println("ok 7");

            handler.join(5000);
        }

        assertFalse(found.get(), "compositeFound остался false");
        assertEquals(0, remaining.get());
        assertTrue(queue.isEmpty());
    }

    @Test
    @Timeout(15)
    void noAck_taskReturnsToQueue() throws Exception {
        BlockingQueue<Task> queue = new LinkedBlockingQueue<>();
        AtomicBoolean found = new AtomicBoolean(false);
        AtomicInteger remaining = new AtomicInteger(1);
        Task original = new Task(99, new int[]{4});
        queue.offer(original);

        Thread handler = new Thread(new WorkerHandler(serverSide, queue, found, remaining));
        handler.start();

        try (ObjectInputStream in = new ObjectInputStream(workerSide.getInputStream())) {
            Task received = (Task) in.readObject();
            assertEquals(99, received.id());
            handler.join(10000);
        }

        assertFalse(found.get());
        assertEquals(1, remaining.get(), "счётчик не должен меняться при сбое");
        Task requeued = queue.poll(1, TimeUnit.SECONDS);
        assertNotNull(requeued, "задача должна вернуться в очередь");
        assertEquals(99, requeued.id());
    }

    @Test
    @Timeout(20)
    void noResponseAfterAck_taskReturnsToQueue() throws Exception {
        BlockingQueue<Task> queue = new LinkedBlockingQueue<>();
        AtomicBoolean found = new AtomicBoolean(false);
        AtomicInteger remaining = new AtomicInteger(1);
        queue.offer(new Task(5, new int[]{9}));

        Thread handler = new Thread(new WorkerHandler(serverSide, queue, found, remaining));
        handler.start();

        try (
            ObjectInputStream in = new ObjectInputStream(workerSide.getInputStream());
            PrintWriter out = new PrintWriter(workerSide.getOutputStream(), true)
        ) {
            Task received = (Task) in.readObject();
            assertEquals(5, received.id());

            out.println("ack 5");

            handler.join(15000);
        }

        assertEquals(1, remaining.get());
        Task requeued = queue.poll(1, TimeUnit.SECONDS);
        assertNotNull(requeued);
        assertEquals(5, requeued.id());
    }

    @Test
    @Timeout(15)
    void wrongAckId_taskReturnsToQueue() throws Exception {
        BlockingQueue<Task> queue = new LinkedBlockingQueue<>();
        AtomicBoolean found = new AtomicBoolean(false);
        AtomicInteger remaining = new AtomicInteger(1);
        queue.offer(new Task(1, new int[]{4}));

        Thread handler = new Thread(new WorkerHandler(serverSide, queue, found, remaining));
        handler.start();

        try (
            ObjectInputStream in = new ObjectInputStream(workerSide.getInputStream());
            PrintWriter out = new PrintWriter(workerSide.getOutputStream(), true)
        ) {
            in.readObject();
            out.println("ack 999");
            handler.join(5000);
        }

        assertEquals(1, remaining.get());
        Task requeued = queue.poll(1, TimeUnit.SECONDS);
        assertNotNull(requeued);
        assertEquals(1, requeued.id());
    }

    @Test
    @Timeout(15)
    void emptyQueue_handlerExitsWithoutSendingTask() throws Exception {
        BlockingQueue<Task> queue = new LinkedBlockingQueue<>();
        AtomicBoolean found = new AtomicBoolean(false);
        AtomicInteger remaining = new AtomicInteger(0);

        Thread handler = new Thread(new WorkerHandler(serverSide, queue, found, remaining));
        handler.start();
        handler.join(5000);

        assertFalse(handler.isAlive(), "handler должен выйти при пустой очереди");
        assertEquals(0, remaining.get());
    }

    @Test
    @Timeout(15)
    void taskSerializationRoundtrip_preservesIdAndChunk() throws Exception {
        BlockingQueue<Task> queue = new LinkedBlockingQueue<>();
        AtomicBoolean found = new AtomicBoolean(false);
        AtomicInteger remaining = new AtomicInteger(1);
        int[] chunk = {20319251, 6997901, 6997927};
        queue.offer(new Task(123, chunk));

        Thread handler = new Thread(new WorkerHandler(serverSide, queue, found, remaining));
        handler.start();

        try (
            ObjectInputStream in = new ObjectInputStream(workerSide.getInputStream());
            PrintWriter out = new PrintWriter(workerSide.getOutputStream(), true);
            BufferedReader ignored = new BufferedReader(new InputStreamReader(workerSide.getInputStream()))
        ) {
            Task received = (Task) in.readObject();
            assertEquals(123, received.id());
            assertEquals(chunk.length, received.chunk().length);
            for (int i = 0; i < chunk.length; i++) {
                assertEquals(chunk[i], received.chunk()[i]);
            }

            out.println("ack 123");
            out.println("ok 123");
            handler.join(5000);
        }

        assertEquals(0, remaining.get());
    }
}
