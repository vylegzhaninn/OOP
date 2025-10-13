package ru.nsu.vylegzhanin.expression;

import java.util.Map;

/**
 * Класс для представления числовых констант в выражениях.
 */
public class Number extends Expression {
    int value;

    public Number(int value) {
        this.value = value;
    }

    @Override
    public String print() {
        return Integer.toString(value);
    }

    @Override
    public int eval(Map<String, Integer> vars) {
        return value;
    }

    @Override
    public Expression derivative(String var) {
        return new Number(0);      
    }
    
    @Override
    public Expression simplify() {
        return this;
    }
    
    @Override
    public boolean hasVariables() {
        return false;
    }
    
    @Override
    public boolean isEqual(Expression other) {
        return other instanceof Number otherNumber && otherNumber.value == this.value;
    }
}