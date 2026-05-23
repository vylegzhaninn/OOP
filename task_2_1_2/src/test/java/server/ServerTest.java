package server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ServerTest {

    @Test
    void getChunk_splitsEvenlyWhenDivisible() {
        int[] arr = {1, 2, 3, 4, 5, 6};
        assertArrayEquals(new int[]{1, 2}, Server.getChunk(arr, 0, 3));
        assertArrayEquals(new int[]{3, 4}, Server.getChunk(arr, 1, 3));
        assertArrayEquals(new int[]{5, 6}, Server.getChunk(arr, 2, 3));
    }

    @Test
    void getChunk_distributesRemainderToFirstWorkers() {
        int[] arr = {1, 2, 3, 4, 5};
        assertArrayEquals(new int[]{1, 2}, Server.getChunk(arr, 0, 3));
        assertArrayEquals(new int[]{3, 4}, Server.getChunk(arr, 1, 3));
        assertArrayEquals(new int[]{5}, Server.getChunk(arr, 2, 3));
    }

    @Test
    void getChunk_singleWorkerGetsWholeArray() {
        int[] arr = {6, 8, 7, 13, 5, 9, 4};
        assertArrayEquals(arr, Server.getChunk(arr, 0, 1));
    }

    @Test
    void getChunk_arraySmallerThanWorkers() {
        int[] arr = {7, 11};
        assertArrayEquals(new int[]{7}, Server.getChunk(arr, 0, 3));
        assertArrayEquals(new int[]{11}, Server.getChunk(arr, 1, 3));
        assertArrayEquals(new int[]{}, Server.getChunk(arr, 2, 3));
    }

    @Test
    void getChunk_coversAllElementsExactlyOnce() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        int total = 4;
        int covered = 0;
        for (int i = 0; i < total; i++) {
            covered += Server.getChunk(arr, i, total).length;
        }
        assertEquals(arr.length, covered);
    }
}
