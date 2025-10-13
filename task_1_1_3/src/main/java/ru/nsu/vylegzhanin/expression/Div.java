package ru.nsu.vylegzhanin.expression;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Класс для представления операции деления выражений.
 */
public class Div extends Expression {
    List<Expression> terms;

    public Div(Expression... terms) {
        if (terms.length != 2) {
            throw new IllegalArgumentException("Division requires exactly two terms.");
        }
        this.terms = Arrays.asList(terms);
    }

    @Override
    public String print() {
        StringBuilder result = new StringBuilder("(");
        for (int i = 0; i < terms.size(); i++) {
            result.append(terms.get(i).print());
            if (i != terms.size() - 1) {
                result.append("/");
            }
        }
        result.append(")");
        return result.toString();
    }

    @Override
    public int eval(Map<String, Integer> vars) {
        int denominator = terms.get(1).eval(vars);
        if (denominator == 0) {
            throw new ArithmeticException("Division by zero");
        }

        return terms.get(0).eval(vars) / denominator;
    }

    @Override
    public Expression derivative(String var) {
        Expression f = terms.get(0);
        Expression g = terms.get(1);

        Expression fderiv = f.derivative(var);
        Expression gderiv = g.derivative(var);

        Expression numerator = new Sub(
            new Mul(fderiv, g),
            new Mul(f, gderiv)
        );

        Expression denominator = new Mul(g, g);

        return new Div(numerator, denominator);
    }
    
    @Override
    public Expression simplify() {
        Expression numerator = terms.get(0).simplify();
        Expression denominator = terms.get(1).simplify();
        
        if (denominator instanceof Number denNumber && denNumber.value == 0) {
            throw new ArithmeticException("Division by zero");
        }
        
        if (numerator instanceof Number numNumber && numNumber.value == 0) {
            return new Number(0);
        }
        
        if (denominator instanceof Number denNumber && denNumber.value == 1) {
            return numerator;
        }

        if (numerator instanceof Number numNumber && denominator instanceof Number denNumber) {
            if (numNumber.value % denNumber.value == 0) {
                return new Number(numNumber.value / denNumber.value);
            }
        }
        
        return new Div(numerator, denominator);
    }
    
    @Override
    public boolean hasVariables() {
        for (Expression term : terms) {
            if (term.hasVariables()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isEqual(Expression other) {
        if (!(other instanceof Div otherDiv)) {
            return false;
        }
        
        if (terms.size() != otherDiv.terms.size()) {
            return false;
        }
        
        for (int i = 0; i < terms.size(); i++) {
            if (!terms.get(i).isEqual(otherDiv.terms.get(i))) {
                return false;
            }
        }
        return true;
    }
}
