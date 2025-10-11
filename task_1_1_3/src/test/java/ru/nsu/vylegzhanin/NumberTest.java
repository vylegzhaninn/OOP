package ru.nsu.vylegzhanin;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для класса Number.
 */
class NumberTest {

    @Test
    @DisplayName("Создание числа")
    void testNumberCreation() {
        Number num = new Number(42);
        assertEquals("42", num.print());
    }

    @Test
    @DisplayName("Печать положительного числа")
    void testPrintPositive() {
        Number num = new Number(123);
        assertEquals("123", num.print());
    }

    @Test
    @DisplayName("Печать отрицательного числа")
    void testPrintNegative() {
        Number num = new Number(-456);
        assertEquals("-456", num.print());
    }

    @Test
    @DisplayName("Печать нуля")
    void testPrintZero() {
        Number num = new Number(0);
        assertEquals("0", num.print());
    }

    @Test
    @DisplayName("Вычисление числа")
    void testEval() {
        Number num = new Number(42);
        Map<String, Integer> vars = new HashMap<>();
        assertEquals(42, num.eval(vars));
    }

    @Test
    @DisplayName("Производная числа равна нулю")
    void testDerivative() {
        Number num = new Number(42);
        Expression derivative = num.derivative("x");
        assertTrue(derivative instanceof Number);
        assertEquals(0, ((Number) derivative).value);
    }

    @Test
    @DisplayName("Упрощение числа возвращает само число")
    void testSimplify() {
        Number num = new Number(42);
        Expression simplified = num.simplify();
        assertSame(num, simplified);
    }

    @Test
    @DisplayName("Число не содержит переменных")
    void testHasVariables() {
        Number num = new Number(42);
        assertFalse(num.hasVariables());
    }

    @Test
    @DisplayName("Равенство двух одинаковых чисел")
    void testIsEqualTrue() {
        Number num1 = new Number(42);
        Number num2 = new Number(42);
        assertTrue(num1.isEqual(num2));
    }

    @Test
    @DisplayName("Неравенство разных чисел")
    void testIsEqualFalse() {
        Number num1 = new Number(42);
        Number num2 = new Number(43);
        assertFalse(num1.isEqual(num2));
    }

    @Test
    @DisplayName("Число не равно переменной")
    void testIsEqualWithVariable() {
        Number num = new Number(42);
        Variable var = new Variable("x");
        assertFalse(num.isEqual(var));
    }
}
