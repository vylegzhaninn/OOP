package ru.nsu.vylegzhanin.expression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Тесты для класса Add (сложение).
 */
class AddTest {

    @Test
    @DisplayName("Печать сложения двух чисел")
    void testPrintTwoNumbers() {
        Add add = new Add(new Number(3), new Number(5));
        assertEquals("(3+5)", add.print());
    }

    @Test
    @DisplayName("Печать сложения переменных")
    void testPrintVariables() {
        Add add = new Add(new Variable("x"), new Variable("y"));
        assertEquals("(x+y)", add.print());
    }

    @Test
    @DisplayName("Вычисление сложения чисел")
    void testEvalNumbers() {
        Add add = new Add(new Number(3), new Number(5));
        Map<String, Integer> vars = new HashMap<>();
        assertEquals(8, add.eval(vars));
    }

    @Test
    @DisplayName("Вычисление сложения переменных")
    void testEvalVariables() {
        Add add = new Add(new Variable("x"), new Variable("y"));
        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 10);
        vars.put("y", 20);
        assertEquals(30, add.eval(vars));
    }

    @Test
    @DisplayName("Производная суммы (d/dx(x+y) = 1+0)")
    void testDerivative() {
        Add add = new Add(new Variable("x"), new Variable("y"));
        Expression derivative = add.derivative("x");
        assertTrue(derivative instanceof Add);
        // (1+0)
    }

    @Test
    @DisplayName("Упрощение суммы чисел")
    void testSimplifyNumbers() {
        Add add = new Add(new Number(3), new Number(5));
        Expression simplified = add.simplify();
        assertTrue(simplified instanceof Number);
        assertEquals(8, ((Number) simplified).value);
    }

    @Test
    @DisplayName("Упрощение суммы с нулем")
    void testSimplifyWithZero() {
        Add add = new Add(new Number(0), new Number(5));
        Expression simplified = add.simplify();
        assertTrue(simplified instanceof Number);
        assertEquals(5, ((Number) simplified).value);
    }

    @Test
    @DisplayName("Упрощение суммы с переменной")
    void testSimplifyWithVariable() {
        Add add = new Add(new Variable("x"), new Number(5));
        Expression simplified = add.simplify();
        assertTrue(simplified instanceof Add);
    }

    @Test
    @DisplayName("Проверка наличия переменных")
    void testHasVariablesTrue() {
        Add add = new Add(new Variable("x"), new Number(5));
        assertTrue(add.hasVariables());
    }

    @Test
    @DisplayName("Проверка отсутствия переменных")
    void testHasVariablesFalse() {
        Add add = new Add(new Number(3), new Number(5));
        assertFalse(add.hasVariables());
    }

    @Test
    @DisplayName("Равенство двух одинаковых сумм")
    void testIsEqualTrue() {
        Add add1 = new Add(new Number(3), new Number(5));
        Add add2 = new Add(new Number(3), new Number(5));
        assertTrue(add1.isEqual(add2));
    }

    @Test
    @DisplayName("Неравенство разных сумм")
    void testIsEqualFalse() {
        Add add1 = new Add(new Number(3), new Number(5));
        Add add2 = new Add(new Number(3), new Number(7));
        assertFalse(add1.isEqual(add2));
    }

    @Test
    @DisplayName("Сложение трех чисел")
    void testThreeTerms() {
        Add add = new Add(new Number(1), new Number(2), new Number(3));
        Map<String, Integer> vars = new HashMap<>();
        assertEquals(6, add.eval(vars));
    }
}
