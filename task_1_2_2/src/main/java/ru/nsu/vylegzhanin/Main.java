package ru.nsu.vylegzhanin;

/**
 * Класс для тестирования функциональности HashTable.
 */
public class Main {
    public static void main(String[] args) {
        HashTable<Number, String> hashTable = new HashTable<>();

        hashTable.put(1, "one");
        hashTable.update(1.0, "one"); 
        System.out.println(hashTable.get("one"));
    }
}
