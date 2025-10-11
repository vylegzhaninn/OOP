package ru.nsu.vylegzhanin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

/**
 * Тесты для класса Div (деление).
 */
class DivTest {

    @Test
    @DisplayName("Печать деления двух чисел")
    void testPrintTwoNumbers() {
        Div div = new Div(new Number(10), new Number(2));
        assertEquals("(10/2)", div.print());
    }

    @Test
    @DisplayName("Печать деления переменных")
    void testPrintVariables() {
        Div div = new Div(new Variable("x"), new Variable("y"));
        assertEquals("(x/y)", div.print());
    }

    @Test
    @DisplayName("Вычисление деления чисел")
    void testEvalNumbers() {
        Div div = new Div(new Number(10), new Number(2));
        Map<String, Integer> vars = new HashMap<>();
        assertEquals(5, div.eval(vars));
    }

    @Test
    @DisplayName("Вычисление деления переменных")
    void testEvalVariables() {
        Div div = new Div(new Variable("x"), new Variable("y"));
        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 20);
        vars.put("y", 4);
        assertEquals(5, div.eval(vars));
    }

    @Test
    @DisplayName("Ошибка деления на ноль при вычислении")
    void testEvalDivisionByZero() {
        Div div = new Div(new Number(10), new Number(0));
        Map<String, Integer> vars = new HashMap<>();
        assertThrows(ArithmeticException.class, () -> {
            div.eval(vars);
        });
    }

    @Test
    @DisplayName("Производная частного")
    void testDerivative() {
        // d/dx(x/y) = (1*y - x*0) / (y*y)
        Div div = new Div(new Variable("x"), new Variable("y"));
        Expression derivative = div.derivative("x");
        assertTrue(derivative instanceof Div);
    }

    @Test
    @DisplayName("Упрощение деления чисел")
    void testSimplifyNumbers() {
        Div div = new Div(new Number(10), new Number(2));
        Expression simplified = div.simplify();
        assertTrue(simplified instanceof Number);
        assertEquals(5, ((Number) simplified).value);
    }

    @Test
    @DisplayName("Упрощение деления нуля")
    void testSimplifyZeroDividend() {
        Div div = new Div(new Number(0), new Number(5));
        Expression simplified = div.simplify();
        assertTrue(simplified instanceof Number);
        assertEquals(0, ((Number) simplified).value);
    }

    @Test
    @DisplayName("Упрощение деления на единицу")
    void testSimplifyDivideByOne() {
        Div div = new Div(new Variable("x"), new Number(1));
        Expression simplified = div.simplify();
        assertTrue(simplified instanceof Variable);
        assertEquals("x", simplified.print());
    }

    @Test
    @DisplayName("Ошибка деления на ноль при упрощении")
    void testSimplifyDivisionByZero() {
        Div div = new Div(new Number(10), new Number(0));
        assertThrows(ArithmeticException.class, () -> {
            div.simplify();
        });
    }

    @Test
    @DisplayName("Упрощение деления с переменной")
    void testSimplifyWithVariable() {
        Div div = new Div(new Variable("x"), new Number(5));
        Expression simplified = div.simplify();
        assertTrue(simplified instanceof Div);
    }

    @Test
    @DisplayName("Проверка наличия переменных")
    void testHasVariablesTrue() {
        Div div = new Div(new Variable("x"), new Number(5));
        assertTrue(div.hasVariables());
    }

    @Test
    @DisplayName("Проверка отсутствия переменных")
    void testHasVariablesFalse() {
        Div div = new Div(new Number(10), new Number(2));
        assertFalse(div.hasVariables());
    }

    @Test
    @DisplayName("Равенство двух одинаковых делений")
    void testIsEqualTrue() {
        Div div1 = new Div(new Number(10), new Number(2));
        Div div2 = new Div(new Number(10), new Number(2));
        assertTrue(div1.isEqual(div2));
    }

    @Test
    @DisplayName("Неравенство разных делений")
    void testIsEqualFalse() {
        Div div1 = new Div(new Number(10), new Number(2));
        Div div2 = new Div(new Number(10), new Number(5));
        assertFalse(div1.isEqual(div2));
    }

    @Test
    @DisplayName("Целочисленное деление с остатком")
    void testIntegerDivision() {
        Div div = new Div(new Number(10), new Number(3));
        Map<String, Integer> vars = new HashMap<>();
        assertEquals(3, div.eval(vars)); // 10 / 3 = 3 (целочисленное)
    }

    @Test
    @DisplayName("Деление отрицательных чисел")
    void testNegativeDivision() {
        Div div = new Div(new Number(-10), new Number(2));
        Map<String, Integer> vars = new HashMap<>();
        assertEquals(-5, div.eval(vars));
    }
}
