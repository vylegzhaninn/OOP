package ru.nsu.vylegzhanin;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

/**
 * Итератор для хэш-таблицы с проверкой одновременной модификации.
 * 
 * @param <V> тип значений
 * @param <K> тип ключей
 */
public class HashTableIterator<V, K> implements Iterator<Entry<V, K>> {
    private final List<LinkedList<Entry<V, K>>> map;
    private final int size;
    private final int expectedModCount;
    private final ModificationCounter modificationCounter;
    
    private int currentBucket = 0;
    private int currentIndex = 0;
    private Entry<V, K> nextEntry = null;
    
    /**
     * Интерфейс для получения счетчика модификаций.
     */
    public interface ModificationCounter {
        int getModCount();
    }
    
    /**
     * Создает новый итератор для хэш-таблицы.
     * 
     * @param map список bucket'ов хэш-таблицы
     * @param size размер хэш-таблицы
     * @param modificationCounter объект для получения счетчика модификаций
     */
    public HashTableIterator(
            List<LinkedList<Entry<V, K>>> map,
            int size,
            ModificationCounter modificationCounter) {
        this.map = map;
        this.size = size;
        this.modificationCounter = modificationCounter;
        this.expectedModCount = modificationCounter.getModCount();
        findNext();
    }
    
    /**
     * Находит следующий непустой элемент в хэш-таблице.
     */
    private void findNext() {
        nextEntry = null;
        while (currentBucket < size && nextEntry == null) {
            LinkedList<Entry<V, K>> bucket = map.get(currentBucket);
            if (currentIndex < bucket.size()) {
                nextEntry = bucket.get(currentIndex);
                currentIndex++;
            } else {
                currentBucket++;
                currentIndex = 0;
            }
        }
    }
    
    /**
     * Проверяет, есть ли следующий элемент в итерации.
     * 
     * @return true, если есть следующий элемент, иначе false
     * @throws ConcurrentModificationException если таблица была изменена во время итерации
     */
    @Override
    public boolean hasNext() {
        checkForModification();
        return nextEntry != null;
    }
    
    /**
     * Возвращает следующий элемент в итерации.
     * 
     * @return следующая пара ключ-значение
     * @throws NoSuchElementException если больше нет элементов
     * @throws ConcurrentModificationException если таблица была изменена во время итерации
     */
    @Override
    public Entry<V, K> next() {
        checkForModification();
        if (nextEntry == null) {
            throw new NoSuchElementException("Нет больше элементов в хэш-таблице");
        }
        Entry<V, K> result = nextEntry;
        findNext();
        return result;
    }
    
    /**
     * Проверяет, была ли изменена хэш-таблица во время итерации.
     * Выбрасывает ConcurrentModificationException при обнаружении изменения.
     */
    private void checkForModification() {
        if (modificationCounter.getModCount() != expectedModCount) {
            throw new ConcurrentModificationException(
                "Хэш-таблица была изменена во время итерации"
            );
        }
    }
}
