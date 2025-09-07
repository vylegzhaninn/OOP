package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;

class HeapSortTest {

    @Test
    void testHeapifySimple() {
        int[] arr = {3, 5, 1};     
        HeapSort.heapify(arr, arr.length, 0);
        assertEquals(5, arr[0]);
        assertTrue(arr[0] > arr[1] && arr[0] > arr[2]);
    }

    @Test
    void testHeapifySubtree() {
        int[] arr = {10, 15, 2, 11, 0};
        HeapSort.heapify(arr, arr.length, 0);
        assertEquals(15, arr[0]);
        assertEquals(11, arr[1]);
        assertEquals(10, arr[3]);
    }

    @Test
    void HeapifyRightSubtree(){
        int[] arr = {5, 4, 6, 2, 3, 4, 8};
        HeapSort.heapify(arr, arr.length, 0);
        assertEquals(5, arr[6]);
        assertEquals(6, arr[0]);
        assertEquals(8, arr[2]);
    }

    @Test
    void testReverseArray() {
        int[] input = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};
        assertArrayEquals(expected, HeapSort.heapsort(input));
    }

    @Test
    void testAlreadySorted() {
        int[] input = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};
        assertArrayEquals(expected, HeapSort.heapsort(input));
    }

    @Test
    void testWithDuplicates() {
        int[] input = {3, 2, 1, 3, 2};
        int[] expected = {1, 2, 2, 3, 3};
        assertArrayEquals(expected, HeapSort.heapsort(input));
    }

    @Test
    void testEmptyArray() {
        int[] input = {};
        int[] expected = {};
        assertArrayEquals(expected, HeapSort.heapsort(input));
    }

    @Test
    void testSingleElement() {
        int[] input = {52};
        int[] expected = {52};
        assertArrayEquals(expected, HeapSort.heapsort(input));
    }

    @Test
    @Timeout(1)
    void testPerformance() {
        int[] input = new int[10000];
        for (int i = 0; i < input.length; i++) {
            input[i] = (int) (Math.random() * 1000);
        }
        int[] res = HeapSort.heapsort(input);
        assertNotNull(res);
    }
}