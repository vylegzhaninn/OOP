package ru.nsu.vylegzhanin;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Класс для представления операции деления выражений.
 */
public class Div extends Expression {
    List<Expression> terms;

    Div(Expression... terms) {
        this.terms = Arrays.asList(terms);
    }

    @Override
    String print() {
        String result = "(";
        for (int i = 0; i < terms.size(); i++) {
            result += terms.get(i).print();
            if (i != terms.size() - 1) {
                result += "/";
            }
        }
        result += ")";
        return result;
    }

    @Override
    int eval(Map<String, Integer> vars) {
        if (terms.size() != 2) {
            throw new IllegalArgumentException("Division requires exactly two terms.");
        }
        
        int denominator = terms.get(1).eval(vars);
        if (denominator == 0) {
            throw new ArithmeticException("Division by zero");
        }

        return terms.get(0).eval(vars) / denominator;
    }

    @Override
    Expression derivative(String var) {
        if (terms.size() != 2) {
            throw new IllegalArgumentException("Division requires exactly two terms.");
        }

        Expression f = terms.get(0);
        Expression g = terms.get(1);

        Expression fDerivative = f.derivative(var);
        Expression gDerivative = g.derivative(var);

        Expression numerator = new Sub(
            new Mul(fDerivative, g),
            new Mul(f, gDerivative)
        );

        Expression denominator = new Mul(g, g);

        return new Div(numerator, denominator);
    }
    
    @Override
    Expression simplify() {
        if (terms.size() != 2) {
            throw new IllegalArgumentException("Division requires exactly two terms.");
        }
        
        Expression numerator = terms.get(0).simplify();
        Expression denominator = terms.get(1).simplify();
        
        if (denominator instanceof Number && ((Number) denominator).value == 0) {
            throw new ArithmeticException("Division by zero");
        }
        
        if (numerator instanceof Number && ((Number) numerator).value == 0) {
            return new Number(0);
        }
        
        if (denominator instanceof Number && ((Number) denominator).value == 1) {
            return numerator;
        }

        if (numerator instanceof Number && denominator instanceof Number) {
            int numValue = ((Number) numerator).value;
            int denValue = ((Number) denominator).value;
            
            if (numValue % denValue == 0) {
                return new Number(numValue / denValue);
            }
        }
        
        return new Div(numerator, denominator);
    }
    
    @Override
    boolean hasVariables() {
        for (Expression term : terms) {
            if (term.hasVariables()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    boolean isEqual(Expression other) {
        if (!(other instanceof Div)) {
            return false;
        }
        
        Div otherDiv = (Div) other;
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
