package ru.nsu.vylegzhanin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

/**
 * Тесты для класса Sub (вычитание).
 */
class SubTest {

    @Test
    @DisplayName("Печать вычитания двух чисел")
    void testPrintTwoNumbers() {
        Sub sub = new Sub(new Number(10), new Number(3));
        assertEquals("(10-3)", sub.print());
    }

    @Test
    @DisplayName("Печать вычитания переменных")
    void testPrintVariables() {
        Sub sub = new Sub(new Variable("x"), new Variable("y"));
        assertEquals("(x-y)", sub.print());
    }

    @Test
    @DisplayName("Вычисление вычитания чисел")
    void testEvalNumbers() {
        Sub sub = new Sub(new Number(10), new Number(3));
        Map<String, Integer> vars = new HashMap<>();
        assertEquals(7, sub.eval(vars));
    }

    @Test
    @DisplayName("Вычисление вычитания переменных")
    void testEvalVariables() {
        Sub sub = new Sub(new Variable("x"), new Variable("y"));
        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 20);
        vars.put("y", 5);
        assertEquals(15, sub.eval(vars));
    }

    @Test
    @DisplayName("Производная разности (d/dx(x-y) = 1-0)")
    void testDerivative() {
        Sub sub = new Sub(new Variable("x"), new Variable("y"));
        Expression derivative = sub.derivative("x");
        assertTrue(derivative instanceof Sub);
    }

    @Test
    @DisplayName("Упрощение разности чисел")
    void testSimplifyNumbers() {
        Sub sub = new Sub(new Number(10), new Number(3));
        Expression simplified = sub.simplify();
        assertTrue(simplified instanceof Number);
        assertEquals(7, ((Number) simplified).value);
    }

    @Test
    @DisplayName("Упрощение разности одинаковых переменных (x-x=0)")
    void testSimplifySameVariables() {
        Sub sub = new Sub(new Variable("x"), new Variable("x"));
        Expression simplified = sub.simplify();
        assertTrue(simplified instanceof Number);
        assertEquals(0, ((Number) simplified).value);
    }

    @Test
    @DisplayName("Упрощение разности одинаковых чисел")
    void testSimplifySameNumbers() {
        Sub sub = new Sub(new Number(5), new Number(5));
        Expression simplified = sub.simplify();
        assertTrue(simplified instanceof Number);
        assertEquals(0, ((Number) simplified).value);
    }

    @Test
    @DisplayName("Упрощение разности с переменной")
    void testSimplifyWithVariable() {
        Sub sub = new Sub(new Variable("x"), new Number(5));
        Expression simplified = sub.simplify();
        assertTrue(simplified instanceof Sub);
    }

    @Test
    @DisplayName("Проверка наличия переменных")
    void testHasVariablesTrue() {
        Sub sub = new Sub(new Variable("x"), new Number(5));
        assertTrue(sub.hasVariables());
    }

    @Test
    @DisplayName("Проверка отсутствия переменных")
    void testHasVariablesFalse() {
        Sub sub = new Sub(new Number(10), new Number(3));
        assertFalse(sub.hasVariables());
    }

    @Test
    @DisplayName("Равенство двух одинаковых разностей")
    void testIsEqualTrue() {
        Sub sub1 = new Sub(new Number(10), new Number(3));
        Sub sub2 = new Sub(new Number(10), new Number(3));
        assertTrue(sub1.isEqual(sub2));
    }

    @Test
    @DisplayName("Неравенство разных разностей")
    void testIsEqualFalse() {
        Sub sub1 = new Sub(new Number(10), new Number(3));
        Sub sub2 = new Sub(new Number(10), new Number(5));
        assertFalse(sub1.isEqual(sub2));
    }

    @Test
    @DisplayName("Вычитание с отрицательным результатом")
    void testNegativeResult() {
        Sub sub = new Sub(new Number(3), new Number(10));
        Map<String, Integer> vars = new HashMap<>();
        assertEquals(-7, sub.eval(vars));
    }
}
