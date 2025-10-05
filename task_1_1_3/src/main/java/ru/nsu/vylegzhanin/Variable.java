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
}
