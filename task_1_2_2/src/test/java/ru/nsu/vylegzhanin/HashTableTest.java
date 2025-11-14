package ru.nsu.vylegzhanin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Unit тесты для класса HashTable.
 */
@DisplayName("HashTable Tests")
class HashTableTest {
    
    private HashTable<String, Integer> table;
    
    @BeforeEach
    void setUp() {
        table = new HashTable<>();
    }
    
    // ========== Тесты методов size() и isEmpty() ==========
    
    @Test
    @DisplayName("Размер пустой таблицы")
    void testSizeEmptyTable() {
        assertEquals(0, table.size());
        assertTrue(table.isEmpty());
    }
    
    @Test
    @DisplayName("Размер после добавления элементов")
    void testSizeAfterPut() {
        table.put("Alice", 1);
        assertEquals(1, table.size());
        assertFalse(table.isEmpty());
        
        table.put("Bob", 2);
        assertEquals(2, table.size());
        
        table.put("Charlie", 3);
        assertEquals(3, table.size());
    }
    
    @Test
    @DisplayName("Размер после перезаписи элемента")
    void testSizeAfterOverwrite() {
        table.put("Alice", 1);
        assertEquals(1, table.size());
        
        table.put("Bob", 1); // Перезаписываем
        assertEquals(1, table.size()); // Размер не должен измениться
    }
    
    @Test
    @DisplayName("Размер после удаления")
    void testSizeAfterDelete() {
        table.put("Alice", 1);
        table.put("Bob", 2);
        assertEquals(2, table.size());
        
        table.delete("Alice", 1);
        assertEquals(1, table.size());
        
        table.delete("Bob", 2);
        assertEquals(0, table.size());
        assertTrue(table.isEmpty());
    }
    
    @Test
    @DisplayName("Метод clear() очищает таблицу")
    void testClear() {
        table.put("Alice", 1);
        table.put("Bob", 2);
        table.put("Charlie", 3);
        assertEquals(3, table.size());
        
        table.clear();
        assertEquals(0, table.size());
        assertTrue(table.isEmpty());
        assertNull(table.get(1));
        assertNull(table.get(2));
        assertNull(table.get(3));
    }
    
    @Test
    @DisplayName("clear() на пустой таблице")
    void testClearEmptyTable() {
        table.clear();
        assertEquals(0, table.size());
        assertTrue(table.isEmpty());
    }
    
    // ========== Тесты метода put ==========
    
    @Test
    @DisplayName("Добавление одного элемента")
    void testPutSingleElement() {
        table.put("Alice", 1);
        assertEquals("Alice", table.get(1));
    }
    
    @Test
    @DisplayName("Добавление нескольких элементов")
    void testPutMultipleElements() {
        table.put("Alice", 1);
        table.put("Bob", 2);
        table.put("Charlie", 3);
        
        assertEquals("Alice", table.get(1));
        assertEquals("Bob", table.get(2));
        assertEquals("Charlie", table.get(3));
    }
    
    @Test
    @DisplayName("Перезапись существующего элемента")
    void testPutOverwriteExistingElement() {
        table.put("Alice", 1);
        table.put("Bob", 1); // Перезаписываем значение для ключа 1
        
        assertEquals("Bob", table.get(1));
    }
    
    @Test
    @DisplayName("Добавление с разными типами Number")
    void testPutWithDifferentNumberTypes() {
        HashTable<Number, String> numTable = new HashTable<>();
        numTable.put(1, "one");
        numTable.put(1.0, "one");
        numTable.put(2L, "two");
        
        assertEquals(1.0, numTable.get("one"));
        assertEquals(2L, numTable.get("two"));
    }
    
    // ========== Тесты метода get ==========
    
    @Test
    @DisplayName("Получение существующего элемента")
    void testGetExistingElement() {
        table.put("Alice", 1);
        assertEquals("Alice", table.get(1));
    }
    
    @Test
    @DisplayName("Получение несуществующего элемента")
    void testGetNonExistingElement() {
        assertNull(table.get(999));
    }
    
    @Test
    @DisplayName("Получение после удаления")
    void testGetAfterDelete() {
        table.put("Alice", 1);
        table.delete("Alice", 1);
        assertNull(table.get(1));
    }
    
    // ========== Тесты метода update ==========
    
    @Test
    @DisplayName("Обновление существующего элемента")
    void testUpdateExistingElement() {
        table.put("Alice", 1);
        table.update("Alicia", 1);
        assertEquals("Alicia", table.get(1));
    }
    
    @Test
    @DisplayName("Обновление несуществующего элемента")
    void testUpdateNonExistingElement() {
        table.update("Alice", 999);
        assertNull(table.get(999)); // Элемент не должен быть добавлен
    }
    
    @Test
    @DisplayName("Обновление с изменением типа Number")
    void testUpdateWithNumberTypeChange() {
        HashTable<Number, String> numTable = new HashTable<>();
        numTable.put(1, "one");
        numTable.update(1.0, "one");
        
        assertEquals(1.0, numTable.get("one"));
        assertTrue(numTable.get("one") instanceof Double);
    }
    
    // ========== Тесты метода delete ==========
    
    @Test
    @DisplayName("Удаление существующего элемента")
    void testDeleteExistingElement() {
        table.put("Alice", 1);
        table.delete("Alice", 1);
        assertNull(table.get(1));
    }
    
    @Test
    @DisplayName("Удаление несуществующего элемента")
    void testDeleteNonExistingElement() {
        table.delete("Ghost", 999); // Не должно вызывать ошибку
        assertNull(table.get(999));
    }
    
    @Test
    @DisplayName("Удаление всех элементов")
    void testDeleteAllElements() {
        table.put("Alice", 1);
        table.put("Bob", 2);
        table.put("Charlie", 3);
        
        table.delete("Alice", 1);
        table.delete("Bob", 2);
        table.delete("Charlie", 3);
        
        assertNull(table.get(1));
        assertNull(table.get(2));
        assertNull(table.get(3));
    }
    
    // ========== Тесты метода containsKey ==========
    
    @Test
    @DisplayName("Проверка существующего ключа")
    void testContainsKeyExistingKey() {
        table.put("Alice", 1);
        assertTrue(table.containsKey(1));
    }
    
    @Test
    @DisplayName("Проверка несуществующего ключа")
    void testContainsKeyNonExistingKey() {
        assertFalse(table.containsKey(999));
    }
    
    @Test
    @DisplayName("Проверка после удаления")
    void testContainsKeyAfterDelete() {
        table.put("Alice", 1);
        table.delete("Alice", 1);
        assertFalse(table.containsKey(1));
    }
    
    // ========== Тесты итератора ==========
    
    @Test
    @DisplayName("Итерация по пустой таблице")
    void testIteratorEmptyTable() {
        assertFalse(table.iterator().hasNext());
    }
    
    @Test
    @DisplayName("Итерация по таблице с элементами")
    void testIteratorWithElements() {
        table.put("Alice", 1);
        table.put("Bob", 2);
        table.put("Charlie", 3);
        
        int count = 0;
        for (Entry<String, Integer> entry : table) {
            assertNotNull(entry.getKey());
            assertNotNull(entry.getValue());
            count++;
        }
        
        assertEquals(3, count);
    }
    
    @Test
    @DisplayName("Итератор выбрасывает NoSuchElementException")
    void testIteratorNoSuchElementException() {
        table.put("Alice", 1);
        Iterator<Entry<String, Integer>> iterator = table.iterator();
        
        iterator.next(); // Первый элемент
        assertThrows(NoSuchElementException.class, iterator::next);
    }
    
    // ========== Тесты метода equals ==========
    
    @Test
    @DisplayName("Сравнение с самим собой")
    void testEqualsWithSelf() {
        table.put("Alice", 1);
        assertTrue(table.equals(table));
    }
    
    @Test
    @DisplayName("Сравнение с null")
    void testEqualsWithNull() {
        assertFalse(table.equals(null));
    }
    
    @Test
    @DisplayName("Сравнение с другим типом")
    void testEqualsWithDifferentType() {
        assertFalse(table.equals("Not a HashTable"));
    }
    
    @Test
    @DisplayName("Сравнение двух одинаковых таблиц")
    void testEqualsWithSameTables() {
        HashTable<String, Integer> table2 = new HashTable<>();
        
        table.put("Alice", 1);
        table.put("Bob", 2);
        
        table2.put("Alice", 1);
        table2.put("Bob", 2);
        
        assertTrue(table.equals(table2));
    }
    
    @Test
    @DisplayName("Сравнение двух разных таблиц")
    void testEqualsWithDifferentTables() {
        HashTable<String, Integer> table2 = new HashTable<>();
        
        table.put("Alice", 1);
        table2.put("Bob", 2);
        
        assertFalse(table.equals(table2));
    }
    
    @Test
    @DisplayName("Сравнение таблиц с разным количеством элементов")
    void testEqualsWithDifferentSizes() {
        HashTable<String, Integer> table2 = new HashTable<>();
        
        table.put("Alice", 1);
        table.put("Bob", 2);
        
        table2.put("Alice", 1);
        
        assertFalse(table.equals(table2));
    }
    
    // ========== Тесты метода hashCode ==========
    
    @Test
    @DisplayName("HashCode для одинаковых таблиц")
    void testHashCodeForEqualTables() {
        HashTable<String, Integer> table2 = new HashTable<>();
        
        table.put("Alice", 1);
        table.put("Bob", 2);
        
        table2.put("Alice", 1);
        table2.put("Bob", 2);
        
        assertEquals(table.hashCode(), table2.hashCode());
    }
    
    @Test
    @DisplayName("HashCode для разных таблиц")
    void testHashCodeForDifferentTables() {
        HashTable<String, Integer> table2 = new HashTable<>();
        
        table.put("Alice", 1);
        table2.put("Bob", 2);
        
        assertNotEquals(table.hashCode(), table2.hashCode());
    }
    
    // ========== Тесты метода toString ==========
    
    @Test
    @DisplayName("toString для пустой таблицы")
    void testToStringEmptyTable() {
        String result = table.toString();
        assertTrue(result.contains("HashTable"));
    }
    
    @Test
    @DisplayName("toString для таблицы с элементами")
    void testToStringWithElements() {
        table.put("Alice", 1);
        table.put("Bob", 2);
        
        String result = table.toString();
        assertTrue(result.contains("1"));
        assertTrue(result.contains("Alice"));
        assertTrue(result.contains("2"));
        assertTrue(result.contains("Bob"));
    }
    
    // ========== Тесты с коллизиями ==========
    
    @Test
    @DisplayName("Работа с коллизиями хэшей")
    void testHashCollisions() {
        // Добавляем много элементов, чтобы вызвать коллизии
        for (int i = 0; i < 200; i++) {
            table.put("Value" + i, i);
        }
        
        // Проверяем, что все элементы доступны
        for (int i = 0; i < 200; i++) {
            assertEquals("Value" + i, table.get(i));
        }
    }
    
    // ========== Тесты с граничными случаями ==========
    
    @Test
    @DisplayName("Работа с пустыми строками")
    void testEmptyStrings() {
        table.put("", 0);
        assertEquals("", table.get(0));
    }
    
    @Test
    @DisplayName("Работа с null значениями")
    void testNullValues() {
        table.put(null, 1);
        assertNull(table.get(1));
        assertTrue(table.containsKey(1));
    }
    
    @Test
    @DisplayName("Работа с отрицательными числами")
    void testNegativeNumbers() {
        table.put("Negative", -1);
        assertEquals("Negative", table.get(-1));
    }
    
    // ========== Интеграционные тесты ==========
    
    @Test
    @DisplayName("Сценарий: добавление, обновление, удаление")
    void testCompleteScenario() {
        // Добавление
        table.put("Alice", 1);
        table.put("Bob", 2);
        table.put("Charlie", 3);
        
        assertEquals("Alice", table.get(1));
        assertEquals("Bob", table.get(2));
        assertEquals("Charlie", table.get(3));
        
        // Обновление
        table.update("Alicia", 1);
        assertEquals("Alicia", table.get(1));
        
        // Удаление
        table.delete("Bob", 2);
        assertNull(table.get(2));
        
        // Проверка оставшихся элементов
        assertEquals("Alicia", table.get(1));
        assertEquals("Charlie", table.get(3));
    }
    
    @Test
    @DisplayName("Работа с пользовательским типом данных")
    void testCustomDataType() {
        class Person {
            String name;
            int age;
            
            Person(String name, int age) {
                this.name = name;
                this.age = age;
            }
            
            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof Person)) return false;
                Person other = (Person) obj;
                return this.name.equals(other.name) && this.age == other.age;
            }
        }
        
        HashTable<Person, Integer> personTable = new HashTable<>();
        Person alice = new Person("Alice", 25);
        Person bob = new Person("Bob", 30);
        
        personTable.put(alice, 1);
        personTable.put(bob, 2);
        
        assertEquals(alice, personTable.get(1));
        assertEquals(bob, personTable.get(2));
    }
    
    // ========== Тесты метода resize и автоматического изменения размера ==========
    
    @Test
    @DisplayName("Автоматическое изменение размера при заполнении")
    void testAutoResize() {
        HashTable<String, Integer> smallTable = new HashTable<>(4); // Маленькая таблица
        
        // Добавляем элементы до заполнения таблицы
        smallTable.put("A", 1);
        smallTable.put("B", 2);
        smallTable.put("C", 3);
        smallTable.put("D", 4); // Массив заполнен (4 элемента), должен произойти resize
        
        // Проверяем, что все элементы остались доступны после resize
        assertEquals("A", smallTable.get(1));
        assertEquals("B", smallTable.get(2));
        assertEquals("C", smallTable.get(3));
        assertEquals("D", smallTable.get(4));
        
        // Можем добавить ещё элементы
        smallTable.put("E", 5);
        assertEquals("E", smallTable.get(5));
        assertEquals(5, smallTable.size());
    }
    
    @Test
    @DisplayName("Изменение размера сохраняет все элементы")
    void testResizePreservesAllElements() {
        HashTable<String, Integer> resizeTable = new HashTable<>(8);
        
        // Добавляем много элементов
        for (int i = 0; i < 50; i++) {
            resizeTable.put("Value" + i, i);
        }
        
        // Проверяем, что все элементы доступны
        for (int i = 0; i < 50; i++) {
            assertEquals("Value" + i, resizeTable.get(i));
        }
        
        assertEquals(50, resizeTable.size());
    }
    
    @Test
    @DisplayName("После resize итератор работает корректно")
    void testIteratorAfterResize() {
        HashTable<String, Integer> resizeTable = new HashTable<>(4);
        
        // Добавляем элементы для вызова resize
        for (int i = 0; i < 10; i++) {
            resizeTable.put("Value" + i, i);
        }
        
        // Проверяем, что итератор видит все элементы
        int count = 0;
        for (Entry<String, Integer> entry : resizeTable) {
            assertNotNull(entry);
            count++;
        }
        
        assertEquals(10, count);
    }
    
    // ========== Тесты с коллизиями ==========
}
