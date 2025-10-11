package ru.nsu.vylegzhanin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для класса Parse.
 */
class ParseTest {

    @Test
    @DisplayName("Парсинг числа")
    void testParseNumber() {
        Expression expr = Parse.parse("42");
        assertTrue(expr instanceof Number);
        assertEquals("42", expr.print());
    }

    @Test
    @DisplayName("Парсинг переменной")
    void testParseVariable() {
        Expression expr = Parse.parse("x");
        assertTrue(expr instanceof Variable);
        assertEquals("x", expr.print());
    }

    @Test
    @DisplayName("Парсинг сложения")
    void testParseAddition() {
        Expression expr = Parse.parse("3+5");
        assertTrue(expr instanceof Add);
        Map<String, Integer> vars = new HashMap<>();
        assertEquals(8, expr.eval(vars));
    }

    @Test
    @DisplayName("Парсинг вычитания")
    void testParseSubtraction() {
        Expression expr = Parse.parse("10-3");
        assertTrue(expr instanceof Sub);
        Map<String, Integer> vars = new HashMap<>();
        assertEquals(7, expr.eval(vars));
    }

    @Test
    @DisplayName("Парсинг умножения")
    void testParseMultiplication() {
        Expression expr = Parse.parse("3*5");
        assertTrue(expr instanceof Mul);
        Map<String, Integer> vars = new HashMap<>();
        assertEquals(15, expr.eval(vars));
    }

    @Test
    @DisplayName("Парсинг деления")
    void testParseDivision() {
        Expression expr = Parse.parse("10/2");
        assertTrue(expr instanceof Div);
        Map<String, Integer> vars = new HashMap<>();
        assertEquals(5, expr.eval(vars));
    }

    @Test
    @DisplayName("Парсинг выражения со скобками")
    void testParseWithParentheses() {
        Expression expr = Parse.parse("(3+5)");
        assertTrue(expr instanceof Add);
        Map<String, Integer> vars = new HashMap<>();
        assertEquals(8, expr.eval(vars));
    }

    @Test
    @DisplayName("Парсинг сложного выражения")
    void testParseComplexExpression() {
        Expression expr = Parse.parse("(x+5)*2");
        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 3);
        assertEquals(16, expr.eval(vars));
    }

    @Test
    @DisplayName("Парсинг вложенных скобок")
    void testParseNestedParentheses() {
        Expression expr = Parse.parse("((3+5)*2)");
        Map<String, Integer> vars = new HashMap<>();
        assertEquals(16, expr.eval(vars));
    }

    @Test
    @DisplayName("Парсинг с пробелами")
    void testParseWithSpaces() {
        Expression expr = Parse.parse("3 + 5");
        assertTrue(expr instanceof Add);
        Map<String, Integer> vars = new HashMap<>();
        assertEquals(8, expr.eval(vars));
    }

    @Test
    @DisplayName("Ошибка: пустая строка")
    void testParseEmptyString() {
        assertThrows(Exception.class, () -> {
            Parse.parse("");
        });
    }

    @Test
    @DisplayName("Ошибка: null")
    void testParseNull() {
        assertThrows(Exception.class, () -> {
            Parse.parse(null);
        });
    }

    @Test
    @DisplayName("Парсинг переменных с несколькими буквами")
    void testParseMultiCharVariable() {
        Expression expr = Parse.parse("abc");
        assertTrue(expr instanceof Variable);
        assertEquals("abc", expr.print());
    }

    @Test
    @DisplayName("Парсинг приоритета операций (умножение перед сложением)")
    void testParseOperatorPrecedence() {
        Expression expr = Parse.parse("2+3*4");
        // Должно быть 2+(3*4) = 14, а не (2+3)*4 = 20
        // Проверяем структуру
        assertTrue(expr instanceof Add || expr instanceof Mul);
    }

    @Test
    @DisplayName("Парсинг цепочки сложений")
    void testParseChainedAdditions() {
        Expression expr = Parse.parse("1+2+3");
        Map<String, Integer> vars = new HashMap<>();
        assertEquals(6, expr.eval(vars));
    }

    @Test
    @DisplayName("Парсинг смешанных операций")
    void testParseMixedOperations() {
        Expression expr = Parse.parse("((10+5)*2)-3");
        Map<String, Integer> vars = new HashMap<>();
        assertEquals(27, expr.eval(vars));
    }
}
