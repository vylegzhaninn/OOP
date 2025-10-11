package ru.nsu.vylegzhanin;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тесты для класса VariableParser.
 */
class VariableParserTest {

    @Test
    @DisplayName("Парсинг одной переменной")
    void testParseOneVariable() {
        Map<String, Integer> result = VariableParser.parseVars("x=5");
        assertEquals(1, result.size());
        assertEquals(5, result.get("x"));
    }

    @Test
    @DisplayName("Парсинг нескольких переменных")
    void testParseMultipleVariables() {
        Map<String, Integer> result = VariableParser.parseVars("x=5; y=10; z=15");
        assertEquals(3, result.size());
        assertEquals(5, result.get("x"));
        assertEquals(10, result.get("y"));
        assertEquals(15, result.get("z"));
    }

    @Test
    @DisplayName("Парсинг с пробелами")
    void testParseWithSpaces() {
        Map<String, Integer> result = VariableParser.parseVars("x = 5; y = 10");
        assertEquals(2, result.size());
        assertEquals(5, result.get("x"));
        assertEquals(10, result.get("y"));
    }

    @Test
    @DisplayName("Парсинг отрицательных чисел")
    void testParseNegativeNumbers() {
        Map<String, Integer> result = VariableParser.parseVars("x=-5; y=-10");
        assertEquals(2, result.size());
        assertEquals(-5, result.get("x"));
        assertEquals(-10, result.get("y"));
    }

    @Test
    @DisplayName("Ошибка: пустая строка")
    void testEmptyString() {
        assertThrows(IllegalArgumentException.class, () -> {
            VariableParser.parseVars("");
        });
    }

    @Test
    @DisplayName("Ошибка: null")
    void testNullString() {
        assertThrows(IllegalArgumentException.class, () -> {
            VariableParser.parseVars(null);
        });
    }

    @Test
    @DisplayName("Ошибка: неверный формат (без знака =)")
    void testInvalidFormatNoEquals() {
        assertThrows(IllegalArgumentException.class, () -> {
            VariableParser.parseVars("x5");
        });
    }

    @Test
    @DisplayName("Ошибка: пустое имя переменной")
    void testEmptyVariableName() {
        assertThrows(IllegalArgumentException.class, () -> {
            VariableParser.parseVars("=5");
        });
    }

    @Test
    @DisplayName("Ошибка: пустое значение")
    void testEmptyValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            VariableParser.parseVars("x=");
        });
    }

    @Test
    @DisplayName("Ошибка: не числовое значение")
    void testNonNumericValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            VariableParser.parseVars("x=abc");
        });
    }

    @Test
    @DisplayName("Перезапись переменной (последнее значение)")
    void testVariableOverwrite() {
        Map<String, Integer> result = VariableParser.parseVars("x=5; x=10");
        assertEquals(1, result.size());
        assertEquals(10, result.get("x"));
    }
}
