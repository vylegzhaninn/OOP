package ru.nsu.vylegzhanin;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Реализация хэш-таблицы с методом цепочек для разрешения коллизий.
 * Поддерживает итерирование с обработкой ConcurrentModificationException.
 * Автоматически увеличивает размер в 2 раза при заполнении.
 * 
 * @param <V> тип значений, хранящихся в таблице
 * @param <K> тип ключей
 */
public class HashTable <V,K> implements Iterable<Entry<V,K>>, HashTableIterator.ModificationCounter {  
    private static final int DEFAULT_CAPACITY = 32; // Начальная емкость
    
    private int tableSize; // Текущий размер массива bucket'ов
    private int elementCount = 0; // Количество элементов в таблице
    List<LinkedList<Entry<V,K>>> map = new ArrayList<>();
    private int modCount = 0; // Счетчик модификаций для отслеживания изменений
    
    /**
     * Вычисляет хэш для ключа с учетом размера хэш-таблицы.
     * 
     * @param key ключ, для которого вычисляется хэш
     * @return индекс в диапазоне [0, tableSize-1]
     */
    private int getHash(K key) {
        if (key == null) return 0;
        return Math.abs(key.hashCode()) % tableSize;
    }
    
    /**
     * Вычисляет хэш для ключа с учетом конкретного размера.
     * Используется при изменении размера таблицы.
     * 
     * @param key ключ, для которого вычисляется хэш
     * @param size размер таблицы
     * @return индекс в диапазоне [0, size-1]
     */
    private int getHash(K key, int size) {
        if (key == null) return 0;
        return Math.abs(key.hashCode()) % size;
    }
    
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
     * Возвращает количество пар ключ-значение в хэш-таблице.
     * 
     * @return количество элементов
     */
    public int size() {
        return elementCount;
    }
    
    /**
     * Проверяет, пуста ли хэш-таблица.
     * 
     * @return true, если таблица не содержит элементов
     */
    public boolean isEmpty() {
        return elementCount == 0;
    }

    /**
     * Создает новую пустую хэш-таблицу с начальной емкостью по умолчанию.
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
        this.tableSize = initialCapacity;
        for (int i = 0; i < tableSize; i++) {
            map.add(new LinkedList<>());
        }
    }

    /**
     * Добавляет пару ключ-значение в хэш-таблицу.
     * Если ключ уже существует, обновляет его значение.
     * Автоматически увеличивает размер в 2 раза при заполнении.
     * 
     * @param value значение для добавления
     * @param key ключ для добавления
     */
    public void put(V value, K key) {
        int hash = getHash(key);
        LinkedList<Entry<V,K>> bucket = map.get(hash);
        
        for (Entry<V,K> entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value;
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
     * Если ключ не найден, не выполняет никаких действий.
     * 
     * @param value новое значение
     * @param key ключ для обновления
     */
    public void update(V value, K key) {
        int hash = getHash(key);
        LinkedList<Entry<V,K>> bucket = map.get(hash);
        for (Entry<V,K> entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value;
                modCount++;
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
        int hash = getHash(key);
        LinkedList<Entry<V,K>> bucket = map.get(hash);
        for (Entry<V,K> entry : bucket) {
            if (entry.key.equals(key)) {
                bucket.remove(entry);
                elementCount--;
                modCount++;
                return;
            }
        }
    }
    
    /**
     * Удаляет все элементы из хэш-таблицы.
     */
    public void clear() {
        if (elementCount == 0) return;
        
        for (LinkedList<Entry<V,K>> bucket : map) {
            bucket.clear();
        }
        elementCount = 0;
        modCount++;
    }
    
    /**
     * Увеличивает размер хэш-таблицы в 2 раза и перераспределяет все элементы.
     * 
     * @param newCapacity новая емкость таблицы (должна быть больше текущей)
     */
    private void resize(int newCapacity) {
        List<LinkedList<Entry<V,K>>> oldMap = map;
        
        tableSize = newCapacity;
        map = new ArrayList<>(tableSize);
        
        for (int i = 0; i < tableSize; i++) {
            map.add(new LinkedList<>());
        }
        
        elementCount = 0;
        
        for (LinkedList<Entry<V,K>> bucket : oldMap) {
            for (Entry<V,K> entry : bucket) {
                int newHash = getHash(entry.key, tableSize);
                map.get(newHash).add(entry);
                elementCount++;
            }
        }
        
        modCount++;
    }
    
    /**
     * Получает значение по ключу.
     * 
     * @param key ключ для поиска
     * @return значение, связанное с ключом, или null, если ключ не найден
     */
    public V get(K key){
        int hash = getHash(key);
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
    public boolean containsKey(K key){
        int hash = getHash(key);
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
        if (!(obj instanceof HashTable)) return false;
        
        @SuppressWarnings("unchecked")
        HashTable<V,K> other = (HashTable<V,K>) obj;
        
        if (this.size() != other.size()) return false;
        
        for (Entry<V,K> entry : this) {
            if (!other.containsKey(entry.getKey())) {
                return false;
            }
            
            V thisValue = entry.getValue();
            V otherValue = other.get(entry.getKey());
            
            if (thisValue == null) {
                if (otherValue != null) return false;
            } else {
                if (!thisValue.equals(otherValue)) return false;
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
        return new HashTableIterator<>(map, tableSize, this);
    }
}
