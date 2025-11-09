package ru.nsu.vylegzhanin;

/**
 * Класс для хранения пары ключ-значение.
 * 
 * @param <V> тип значения
 * @param <K> тип ключа
 */
public class Entry<V,K> {
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
