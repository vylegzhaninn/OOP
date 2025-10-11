package ru.nsu.vylegzhanin;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для класса Mul (умножение).
 */
class MulTest {

    @Test
    @DisplayName("Печать умножения двух чисел")
    void testPrintTwoNumbers() {
        Mul mul = new Mul(new Number(3), new Number(5));
        assertEquals("(3*5)", mul.print());
    }

    @Test
    @DisplayName("Печать умножения переменных")
    void testPrintVariables() {
        Mul mul = new Mul(new Variable("x"), new Variable("y"));
        assertEquals("(x*y)", mul.print());
    }

    @Test
    @DisplayName("Вычисление умножения чисел")
    void testEvalNumbers() {
        Mul mul = new Mul(new Number(3), new Number(5));
        Map<String, Integer> vars = new HashMap<>();
        assertEquals(15, mul.eval(vars));
    }

    @Test
    @DisplayName("Вычисление умножения переменных")
    void testEvalVariables() {
        Mul mul = new Mul(new Variable("x"), new Variable("y"));
        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 4);
        vars.put("y", 7);
        assertEquals(28, mul.eval(vars));
    }

    @Test
    @DisplayName("Производная произведения")
    void testDerivative() {
        // d/dx(x*y) = 1*y + x*0 = y
        Mul mul = new Mul(new Variable("x"), new Variable("y"));
        Expression derivative = mul.derivative("x");
        assertTrue(derivative instanceof Add);
    }

    @Test
    @DisplayName("Упрощение произведения чисел")
    void testSimplifyNumbers() {
        Mul mul = new Mul(new Number(3), new Number(5));
        Expression simplified = mul.simplify();
        assertTrue(simplified instanceof Number);
        assertEquals(15, ((Number) simplified).value);
    }

    @Test
    @DisplayName("Упрощение произведения на ноль")
    void testSimplifyMultiplyByZero() {
        Mul mul = new Mul(new Variable("x"), new Number(0));
        Expression simplified = mul.simplify();
        assertTrue(simplified instanceof Number);
        assertEquals(0, ((Number) simplified).value);
    }

    @Test
    @DisplayName("Упрощение произведения на единицу")
    void testSimplifyMultiplyByOne() {
        Mul mul = new Mul(new Variable("x"), new Number(1));
        Expression simplified = mul.simplify();
        assertTrue(simplified instanceof Variable);
        assertEquals("x", simplified.print());
    }

    @Test
    @DisplayName("Упрощение произведения с переменной")
    void testSimplifyWithVariable() {
        Mul mul = new Mul(new Variable("x"), new Number(5));
        Expression simplified = mul.simplify();
        assertTrue(simplified instanceof Mul);
    }

    @Test
    @DisplayName("Проверка наличия переменных")
    void testHasVariablesTrue() {
        Mul mul = new Mul(new Variable("x"), new Number(5));
        assertTrue(mul.hasVariables());
    }

    @Test
    @DisplayName("Проверка отсутствия переменных")
    void testHasVariablesFalse() {
        Mul mul = new Mul(new Number(3), new Number(5));
        assertFalse(mul.hasVariables());
    }

    @Test
    @DisplayName("Равенство двух одинаковых произведений")
    void testIsEqualTrue() {
        Mul mul1 = new Mul(new Number(3), new Number(5));
        Mul mul2 = new Mul(new Number(3), new Number(5));
        assertTrue(mul1.isEqual(mul2));
    }

    @Test
    @DisplayName("Неравенство разных произведений")
    void testIsEqualFalse() {
        Mul mul1 = new Mul(new Number(3), new Number(5));
        Mul mul2 = new Mul(new Number(3), new Number(7));
        assertFalse(mul1.isEqual(mul2));
    }

    @Test
    @DisplayName("Умножение трех чисел")
    void testThreeTerms() {
        Mul mul = new Mul(new Number(2), new Number(3), new Number(4));
        Map<String, Integer> vars = new HashMap<>();
        assertEquals(24, mul.eval(vars));
    }

    @Test
    @DisplayName("Умножение на отрицательное число")
    void testMultiplyByNegative() {
        Mul mul = new Mul(new Number(5), new Number(-3));
        Map<String, Integer> vars = new HashMap<>();
        assertEquals(-15, mul.eval(vars));
    }
}
