package ru.nsu.vylegzhanin;

import java.util.List;

public class Div extends Expression {
    List<Expression> terms;

    Div(Expression... terms) {
        this.terms = java.util.Arrays.asList(terms);
    }

    @Override
    String print() {
        String result = "(";
        for (int i = 0; i < terms.size(); i++) {
            result += terms.get(i).print();
            if (i != terms.size() - 1) {
                result += " / ";
            }
        }
        result += ")";
        return result;
    }

    @Override
    int eval(java.util.Map<String, Integer> vars) {
        int result = terms.get(0).eval(vars);
        for (int i = 1; i < terms.size(); i++) {
            result /= terms.get(i).eval(vars);
        }
        return result;
    }

    @Override
    Expression derivative(String var) {
        if (terms.size() != 2) {
            throw new IllegalArgumentException("Division requires exactly two terms.");
        }

        Expression f = terms.get(0);
        Expression g = terms.get(1);

        Expression fPrime = f.derivative(var);
        Expression gPrime = g.derivative(var);

        Expression numerator = new Sub(
            new Mul(fPrime, g),
            new Mul(f, gPrime)
        );

        Expression denominator = new Mul(g, g);

        return new Div(numerator, denominator);
    }
}
