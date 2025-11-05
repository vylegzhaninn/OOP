package ru.nsu.vylegzhanin;

public class Hash {
    /**
     * Вычисляет хэш для объекта с учетом размера хэш-таблицы.
     * 
     * @param key объект, для которого вычисляется хэш
     * @param size размер хэш-таблицы
     * @return индекс в диапазоне [0, size-1]
     */
    static public int getHash(Object key, int size) {
        // Получаем hashCode объекта и берем его абсолютное значение
        // для избежания отрицательных индексов
        int hash = key.hashCode();
        return Math.abs(hash) % size;
    }
}
