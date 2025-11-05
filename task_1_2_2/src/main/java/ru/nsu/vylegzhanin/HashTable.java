package ru.nsu.vylegzhanin;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Реализация хэш-таблицы с методом цепочек для разрешения коллизий.
 * Поддерживает итерирование с обработкой ConcurrentModificationException.
 * 
 * @param <V> тип значений, хранящихся в таблице
 * @param <K> тип ключей
 */
public class HashTable <V,K> implements Iterable<HashTable.Entry<V,K>>, HashTableIterator.ModificationCounter {  
    private final int size = 100;
    List<LinkedList<Entry<V,K>>> map = new ArrayList<>();
    private int modCount = 0; // Счетчик модификаций для отслеживания изменений
    
    /**
     * Возвращает счетчик модификаций.
     * 
     * @return текущее значение счетчика модификаций
     */
    @Override
    public int getModCount() {
        return modCount;
    }

    /**
     * Внутренний класс для хранения пары ключ-значение.
     * 
     * @param <V> тип значения
     * @param <K> тип ключа
     */
    public static class Entry<V,K> {
        V value;
        K key;
        
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
        
        /**
         * Возвращает ключ записи.
         * 
         * @return ключ
         */
        public K getKey() { 
            return key; 
        }
        
        /**
         * Возвращает значение записи.
         * 
         * @return значение
         */
        public V getValue() { 
            return value; 
        }
    }

    /**
     * Создает новую пустую хэш-таблицу.
     */
    public HashTable() {
        for (int i = 0; i < size; i++) {
            map.add(new LinkedList<>());
        }
    }

    /**
     * Добавляет пару ключ-значение в хэш-таблицу.
     * Если ключ уже существует, обновляет его значение.
     * 
     * @param value значение для добавления
     * @param key ключ для добавления
     */
    public void put(V value, K key) {
        int hash = Hash.getHash(key, size);
        LinkedList<Entry<V,K>> bucket = map.get(hash);
        
        // Проверяем, существует ли ключ, и обновляем значение
        for (Entry<V,K> entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value;
                modCount++;
                return;
            }
        }
        
        // Если ключ не найден, добавляем новую запись
        bucket.add(new Entry<>(key, value));
        modCount++;
    }

    /**
     * Обновляет значение для существующего ключа.
     * Если ключ не найден, не выполняет никаких действий.
     * 
     * @param value новое значение
     * @param key ключ для обновления
     */
    public void update(V value, K key) {
        int hash = Hash.getHash(key, size);
        LinkedList<Entry<V,K>> bucket = map.get(hash);
        for (Entry<V,K> entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value;
                modCount++; // Увеличиваем счетчик при изменении
                return;
            }
        }
    }

    /**
     * Удаляет пару ключ-значение из хэш-таблицы.
     * 
     * @param value значение (не используется в текущей реализации)
     * @param key ключ для удаления
     */
    public void delete(V value, K key){
        int hash = Hash.getHash(key, size);
        LinkedList<Entry<V,K>> bucket = map.get(hash);
        for (Entry<V,K> entry : bucket) {
            if (entry.key.equals(key)) {
                bucket.remove(entry);
                modCount++; // Увеличиваем счетчик при изменении
                return;
            }
        }
    }

    /**
     * Получает значение по ключу.
     * 
     * @param key ключ для поиска
     * @return значение, связанное с ключом, или null, если ключ не найден
     */
    public V get(K key){
        int hash = Hash.getHash(key, size);
        LinkedList<Entry<V,K>> bucket = map.get(hash);
        for (Entry<V,K> entry : bucket) {
            if (entry.key.equals(key)) return entry.value;
        }
        return null;
    }

    /**
     * Проверяет, содержится ли ключ в хэш-таблице.
     * 
     * @param key ключ для проверки
     * @return true, если ключ найден, иначе false
     */
    public boolean hasValue(K key){
        int hash = Hash.getHash(key, size);
        LinkedList<Entry<V,K>> bucket = map.get(hash);
        for (Entry<V,K> entry : bucket) {
            if (entry.key.equals(key)) return true;
        }
        return false;
    }
    
    /**
     * Выводит содержимое хэш-таблицы в строковом формате.
     * Возвращает строковое представление всех пар ключ-значение.
     * 
     * @return строковое представление хэш-таблицы
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HashTable {\n");
        for (Entry<V,K> entry : this) {
            sb.append("  ").append(entry.getKey())
              .append(" -> ").append(entry.getValue())
              .append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
    
    /**
     * Выводит содержимое хэш-таблицы в консоль.
     */
    public void print() {
        System.out.println(this.toString());
    }
    
    /**
     * Сравнивает эту хэш-таблицу с другой на равенство.
     * Две хэш-таблицы считаются равными, если содержат одинаковые пары ключ-значение.
     * 
     * @param obj объект для сравнения
     * @return true, если таблицы равны, иначе false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        @SuppressWarnings("unchecked")
        HashTable<V,K> other = (HashTable<V,K>) obj;
        
        // Сравниваем количество элементов
        int thisCount = 0;
        int otherCount = 0;
        
        for (@SuppressWarnings("unused") Entry<V,K> entry : this) {
            thisCount++;
        }
        for (@SuppressWarnings("unused") Entry<V,K> entry : other) {
            otherCount++;
        }
        
        if (thisCount != otherCount) return false;
        
        // Проверяем, что все элементы из this есть в other
        for (Entry<V,K> entry : this) {
            V otherValue = other.get(entry.getKey());
            if (otherValue == null) {
                // Проверяем, может ли значение быть null
                if (!other.hasValue(entry.getKey())) {
                    return false;
                }
            }
            if (otherValue != null && !entry.getValue().equals(otherValue)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Возвращает хэш-код для этой хэш-таблицы.
     * 
     * @return хэш-код
     */
    @Override
    public int hashCode() {
        int result = 0;
        for (Entry<V,K> entry : this) {
            result += entry.getKey().hashCode() ^ entry.getValue().hashCode();
        }
        return result;
    }
    
    /**
     * Возвращает итератор для перебора всех пар (ключ, значение) в хэш-таблице.
     * Итератор выбрасывает ConcurrentModificationException при изменении таблицы во время итерации.
     * 
     * @return итератор для пар ключ-значение
     */
    @Override
    public Iterator<Entry<V,K>> iterator() {
        return new HashTableIterator<>(map, size, this);
    }
}
