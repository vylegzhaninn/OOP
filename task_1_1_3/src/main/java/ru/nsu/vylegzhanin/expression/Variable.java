package ru.nsu.vylegzhanin.expression;

import java.util.Map;

/**
 * Класс для представления переменных в выражениях.
 */
public class Variable extends Expression {
    String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public String print() {
        return name;   
    }

    @Override
    public int eval(Map<String, Integer> vars) {
        return vars.get(name);
    }

    @Override
    public Expression derivative(String var) {
        return name.equals(var) ? new Number(1) : new Number(0);
    }
    
    @Override
    public Expression simplify() {
        return this;
    }
    
    @Override
    public boolean hasVariables() {
        return true;
    }
    
    @Override
    public boolean isEqual(Expression other) {
        return other instanceof Variable otherVar && otherVar.name.equals(this.name);
    }
}
