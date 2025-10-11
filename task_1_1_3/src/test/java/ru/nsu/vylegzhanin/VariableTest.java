package ru.nsu.vylegzhanin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Тесты для класса Variable.
 */
class VariableTest {

    @Test
    @DisplayName("Создание переменной")
    void testVariableCreation() {
        Variable var = new Variable("x");
        assertEquals("x", var.print());
    }

    @Test
    @DisplayName("Печать переменной")
    void testPrint() {
        Variable var = new Variable("myVar");
        assertEquals("myVar", var.print());
    }

    @Test
    @DisplayName("Вычисление переменной")
    void testEval() {
        Variable var = new Variable("x");
        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 42);
        assertEquals(42, var.eval(vars));
    }

    @Test
    @DisplayName("Производная переменной по себе равна 1")
    void testDerivativeSameVariable() {
        Variable var = new Variable("x");
        Expression derivative = var.derivative("x");
        assertTrue(derivative instanceof Number);
        assertEquals(1, ((Number) derivative).value);
    }

    @Test
    @DisplayName("Производная переменной по другой переменной равна 0")
    void testDerivativeDifferentVariable() {
        Variable var = new Variable("x");
        Expression derivative = var.derivative("y");
        assertTrue(derivative instanceof Number);
        assertEquals(0, ((Number) derivative).value);
    }

    @Test
    @DisplayName("Упрощение переменной возвращает саму переменную")
    void testSimplify() {
        Variable var = new Variable("x");
        Expression simplified = var.simplify();
        assertSame(var, simplified);
    }

    @Test
    @DisplayName("Переменная содержит переменные")
    void testHasVariables() {
        Variable var = new Variable("x");
        assertTrue(var.hasVariables());
    }

    @Test
    @DisplayName("Равенство двух одинаковых переменных")
    void testIsEqualTrue() {
        Variable var1 = new Variable("x");
        Variable var2 = new Variable("x");
        assertTrue(var1.isEqual(var2));
    }

    @Test
    @DisplayName("Неравенство разных переменных")
    void testIsEqualFalse() {
        Variable var1 = new Variable("x");
        Variable var2 = new Variable("y");
        assertFalse(var1.isEqual(var2));
    }

    @Test
    @DisplayName("Переменная не равна числу")
    void testIsEqualWithNumber() {
        Variable var = new Variable("x");
        Number num = new Number(42);
        assertFalse(var.isEqual(num));
    }
}
