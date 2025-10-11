package ru.nsu.vylegzhanin;

import java.util.Map;

public class Number extends Expression{
    int value;

    public Number(int value) {
        this.value = value;
    }

    @Override
    String print() {
        return Integer.toString(value);
    }

    @Override
    int eval(Map<String, Integer> vars) {
        return value;
    }

    @Override
    Expression derivative(String var) {
        return new Number(0);      
    }
    
    @Override
    Expression simplify() {
        return this;
    }
    
    @Override
    boolean hasVariables() {
        return false;
    }
    
    @Override
    boolean isEqual(Expression other) {
        return other instanceof Number && ((Number) other).value == this.value;
    }
}