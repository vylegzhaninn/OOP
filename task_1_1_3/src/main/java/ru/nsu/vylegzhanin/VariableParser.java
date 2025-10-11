package ru.nsu.vylegzhanin;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс для парсинга строки переменных в формате "x=5; y=10".
 */
public class VariableParser {
    
    /**
     * Парсит строку переменных в формате "x=5; y=10" в Map.
     *
     * @param s строка переменных
     * @return Map с переменными и их значениями
     * @throws IllegalArgumentException если формат строки неверный
     */
    public static Map<String, Integer> parseVars(String s) {
        if (s == null || s.isBlank()) {
            throw new IllegalArgumentException(
                "Строка параметров не должна быть пустой или null");
        }
        s = s.replace(" ", "");
        Map<String, Integer> map = new HashMap<>();
 
        String[] parts = s.split(";");
        for (String part : parts) {
            if (part.isEmpty()) {
                throw new IllegalArgumentException("Ожидалось имя=значение");
            }

            String[] value = part.split("=", 2);
            if (value.length != 2) {
                throw new IllegalArgumentException("Ожидалось имя=значение, получено: " + part);
            }

            if (value[0].isEmpty()) {
                throw new IllegalArgumentException("Пустое имя параметра");
            }
            if (value[1].isEmpty()) {
                throw new IllegalArgumentException("Пустое значение параметра");
            }

            try {
                map.put(value[0], Integer.parseInt(value[1]));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                    "Значение параметра '" + value[0] + "' должно быть числом");
            }
        }

        if (map.isEmpty()) {
            throw new IllegalArgumentException("Не найдено ни одного корректного параметра");
        }

        return map;
    }
}
