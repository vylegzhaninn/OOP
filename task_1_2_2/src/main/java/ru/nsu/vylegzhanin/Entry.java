package ru.nsu.vylegzhanin;

/**
 * Класс для хранения пары ключ-значение.
 *
 * @param <V> тип значения
 * @param <K> тип ключа
 */
public class Entry<V, K> {
    private V value;
    private K key;
    
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
    
    /**
     * Устанавливает новое значение для записи.
     *
     * @param value новое значение
     */
    public void setValue(V value) {
        this.value = value;
    }
}
