package ru.nsu.vylegzhanin;

import java.util.Arrays;

/**
 * Класс для реализации пирамидальной сортировки (HeapSort).
 * Предоставляет методы для сортировки массива с использованием алгоритма HeapSort.
 */
public class HeapSort {

    /**
     * Основной метод сортировки массива с использованием алгоритма HeapSort.
     *
     * @param arr массив для сортировки
     * @return отсортированный массив
     */
    public static int[] heapsort(int[] arr) {
        int n = arr.length;

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        for (int i = n - 1; i >= 0; i--) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            heapify(arr, i, 0);
        }

        return arr;
    }

    /**
     * Преобразует поддерево в max-heap.
     *
     * @param arr массив для преобразования
     * @param n   размер кучи
     * @param i   корневой индекс поддерева
     */
    public static void heapify(int[] arr, int n, int i) {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        if (l < n && arr[l] > arr[largest]) {
            largest = l;
        }
        if (r < n && arr[r] > arr[largest]) {
            largest = r;
        }

        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;
            heapify(arr, n, largest);
        }
    }

    /**
     * Основной метод для демонстрации работы алгоритма.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        int[] result = heapsort(new int[]{5, 4, 3, 2, 1});
        System.out.println(Arrays.toString(result));
    }
}