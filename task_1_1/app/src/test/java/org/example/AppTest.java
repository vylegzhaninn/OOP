package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class HeapSortTest {

    @Test
    void testReverseArray() {
        assertArrayEquals(new int[]{1, 2, 3, 4, 5},
                HeapSort.heapsort(new int[]{5, 4, 3, 2, 1}));
    }

    @Test
    void testAlreadySorted() {
        assertArrayEquals(new int[]{1, 2, 3, 4, 5},
                HeapSort.heapsort(new int[]{1, 2, 3, 4, 5}));
    }

    @Test
    void testWithDuplicates() {
        assertArrayEquals(new int[]{1, 2, 2, 3, 3},
                HeapSort.heapsort(new int[]{3, 2, 1, 3, 2}));
    }

    @Test
    void testEmptyArray() {
        assertArrayEquals(new int[]{},
                HeapSort.heapsort(new int[]{}));
    }

    @Test
    void testSingleElement() {
        assertArrayEquals(new int[]{42},
                HeapSort.heapsort(new int[]{42}));
    }
}
