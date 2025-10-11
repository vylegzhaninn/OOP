package ru.nsu.vylegzhanin;

import java.util.Map;


public class Variable extends Expression {
    String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    String print() {
        return name;   
    }

    @Override
    int eval(Map<String, Integer> vars) {
        return vars.get(name);
    }

    @Override
    Expression derivative(String var) {
        return name.equals(var) ? new Number(1) : new Number(0);
    }
    
    @Override
    Expression simplify() {
        return this;
    }
    
    @Override
    boolean hasVariables() {
        return true;
    }
    
    @Override
    boolean isEqual(Expression other) {
        return other instanceof Variable && ((Variable) other).name.equals(this.name);
    }
}
