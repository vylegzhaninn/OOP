package ru.nsu.vylegzhanin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Реализация хэш-таблицы с методом цепочек для разрешения коллизий.
 * Поддерживает итерирование с обработкой ConcurrentModificationException.
 * Автоматически увеличивает размер, когда количество элементов
 * достигает текущей емкости.
 *
 * @param <V> тип значений, хранящихся в таблице
 * @param <K> тип ключей
 */
public class HashTable<V, K> implements Iterable<Entry<V, K>>, HashTableIterator.ModificationCounter {
    private static final int DEFAULT_CAPACITY = 32;

    private int tableSize;
    private int elementCount;
    private int modCount;
    private List<LinkedList<Entry<V, K>>> map;

    /**
     * Создает новую пустую хэш-таблицу с емкостью по умолчанию.
     */
    public HashTable() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Создает новую пустую хэш-таблицу с указанной начальной емкостью.
     *
     * @param initialCapacity начальная емкость таблицы
     */
    public HashTable(int initialCapacity) {
        tableSize = Math.max(initialCapacity, 1);
        elementCount = 0;
        modCount = 0;
        map = new ArrayList<>(tableSize);
        for (int i = 0; i < tableSize; i++) {
            map.add(new LinkedList<>());
        }
    }

    /**
     * Возвращает количество элементов в таблице.
     *
     * @return количество элементов
     */
    public int size() {
        return elementCount;
    }

    /**
     * Проверяет, пуста ли хэш-таблица.
     *
     * @return true, если таблица пуста
     */
    public boolean isEmpty() {
        return elementCount == 0;
    }

    /**
     * Возвращает значение счетчика модификаций.
     *
     * @return текущее значение счетчика модификаций
     */
    @Override
    public int getModCount() {
        return modCount;
    }

    /**
     * Добавляет новую пару ключ-значение или обновляет существующую.
     *
     * @param value значение для сохранения
     * @param key   ключ
     */
    public void put(V value, K key) {
        LinkedList<Entry<V, K>> bucket = map.get(getHash(key));
        for (Entry<V, K> entry : bucket) {
            if (entry.getKey().equals(key)) {
                entry.setValue(value);
                modCount++;
                return;
            }
        }

        bucket.add(new Entry<>(key, value));
        elementCount++;
        modCount++;

        if (elementCount >= tableSize) {
            resize(tableSize * 2);
        }
    }

    /**
     * Обновляет значение для существующего ключа.
     *
     * @param value новое значение
     * @param key   ключ
     */
    public void update(V value, K key) {
        LinkedList<Entry<V, K>> bucket = map.get(getHash(key));
        for (Entry<V, K> entry : bucket) {
            if (entry.getKey().equals(key)) {
                entry.setValue(value);
                modCount++;
                return;
            }
        }
    }

    /**
     * Удаляет элемент по ключу.
     *
     * @param value значение (не используется)
     * @param key   ключ
     */
    public void delete(V value, K key) {
        LinkedList<Entry<V, K>> bucket = map.get(getHash(key));
        for (Entry<V, K> entry : bucket) {
            if (entry.getKey().equals(key)) {
                bucket.remove(entry);
                elementCount--;
                modCount++;
                return;
            }
        }
    }

    /**
     * Очищает таблицу.
     */
    public void clear() {
        if (elementCount == 0) {
            return;
        }
        for (LinkedList<Entry<V, K>> bucket : map) {
            bucket.clear();
        }
        elementCount = 0;
        modCount++;
    }

    /**
     * Возвращает значение по ключу.
     *
     * @param key ключ
     * @return значение или null, если ключ отсутствует
     */
    public V get(K key) {
        LinkedList<Entry<V, K>> bucket = map.get(getHash(key));
        for (Entry<V, K> entry : bucket) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Проверяет, содержится ли ключ в таблице.
     *
     * @param key ключ
     * @return true, если ключ найден
     */
    public boolean containsKey(K key) {
        LinkedList<Entry<V, K>> bucket = map.get(getHash(key));
        for (Entry<V, K> entry : bucket) {
            if (entry.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Возвращает строковое представление таблицы.
     *
     * @return строка с содержимым таблицы
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("HashTable {\n");
        for (Entry<V, K> entry : this) {
            builder.append("  ")
                    .append(entry.getKey())
                    .append(" -> ")
                    .append(entry.getValue())
                    .append('\n');
        }
        builder.append('}');
        return builder.toString();
    }

    /**
     * Выводит содержимое таблицы в консоль.
     */
    public void print() {
        System.out.println(this);
    }

    /**
     * Сравнивает таблицу с другим объектом.
     *
     * @param obj объект для сравнения
     * @return true, если таблицы равны
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HashTable<?, ?>)) {
            return false;
        }
        @SuppressWarnings("unchecked")
        HashTable<V, K> other = (HashTable<V, K>) obj;
        if (size() != other.size()) {
            return false;
        }
        for (Entry<V, K> entry : this) {
            if (!other.containsKey(entry.getKey())) {
                return false;
            }
            V thisValue = entry.getValue();
            Object otherValue = other.get(entry.getKey());
            if (thisValue == null) {
                if (otherValue != null) {
                    return false;
                }
            } else if (!thisValue.equals(otherValue)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Считает хэш-код таблицы.
     *
     * @return хэш-код
     */
    @Override
    public int hashCode() {
        int result = 0;
        for (Entry<V, K> entry : this) {
            int keyHash = entry.getKey() == null ? 0 : entry.getKey().hashCode();
            int valueHash = entry.getValue() == null ? 0 : entry.getValue().hashCode();
            result += keyHash ^ valueHash;
        }
        return result;
    }

    /**
     * Возвращает итератор по таблице.
     *
     * @return итератор
     */
    @Override
    public Iterator<Entry<V, K>> iterator() {
        return new HashTableIterator<>(map, tableSize, this);
    }

    private int getHash(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % tableSize;
    }

    private int getHash(K key, int size) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % size;
    }

    private void resize(int newCapacity) {
        List<LinkedList<Entry<V, K>>> oldMap = map;
        tableSize = newCapacity;
        elementCount = 0;
        map = new ArrayList<>(tableSize);
        for (int i = 0; i < tableSize; i++) {
            map.add(new LinkedList<>());
        }
        for (LinkedList<Entry<V, K>> bucket : oldMap) {
            for (Entry<V, K> entry : bucket) {
                map.get(getHash(entry.getKey(), tableSize)).add(entry);
                elementCount++;
            }
        }
        modCount++;
    }
}
