package ru.nsu.vylegzhanin;

import java.util.Map;

abstract class Expression {
    abstract String print();
    abstract int eval(Map<String, Integer> vars);
    abstract Expression derivative(String var);
    abstract Expression simplify();    
    abstract boolean hasVariables();
    abstract boolean isEqual(Expression other);

    int eval(String varsString) {
        return eval(VariableParser.parseVars(varsString));
    }
}



