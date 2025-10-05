package ru.nsu.vylegzhanin;

import java.util.List;

public class Sub extends Expression {
    List<Expression> terms;

    Sub(Expression... terms) {
        this.terms = java.util.Arrays.asList(terms);
    }


    @Override
    String print(){
        String result = "(";
        for (int i = 0; i  < terms.size(); i++) {
            result += terms.get(i).print();
            if (i != terms.size() - 1) {
                result += " - ";
            }
        }
        result += ")";
        return result;
    }

    @Override
    int eval(java.util.Map<String, Integer> vars) {
        int result = 0;
        for (int i = 0; i < terms.size(); i++) {
            if (i == 0) {
                result += terms.get(i).eval(vars);
            } else {
                result -= terms.get(i).eval(vars);
            }
        }
        return result;
    }

    @Override
    Expression derivative(String var) {
        List<Expression> derivedTerms = new java.util.ArrayList<>();;
        for (Expression term : terms) {
            derivedTerms.add(term.derivative(var));
        }
        return new Sub(derivedTerms.toArray(new Expression[0]));
    }
}
