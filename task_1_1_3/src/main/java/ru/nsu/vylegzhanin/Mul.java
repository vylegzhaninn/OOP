package ru.nsu.vylegzhanin;

import java.util.List;

public class Mul extends Expression{
    List<Expression> terms;

    Mul(Expression... terms) {
        this.terms = java.util.Arrays.asList(terms);
    }

    @Override
    String print(){
        String result = "(";
        for (int i = 0; i  < terms.size(); i++) {
            result += terms.get(i).print();
            if (i != terms.size() - 1) {
                result += " * ";
            }
        }
        result += ")";
        return result;
    }

    @Override
    int eval(java.util.Map<String, Integer> vars) {
        int result = 1;
        for (Expression term : terms) {
            result *= term.eval(vars);  
        }
        return result;
    }

    @Override
    Expression derivative(String var) {
        List<Expression> sumTerms = new java.util.ArrayList<>();

        for (int i = 0; i < terms.size(); i++) {
            List<Expression> productTerms = new java.util.ArrayList<>();

            for (int j = 0; j < terms.size(); j++) {
                if (i == j) {
                    productTerms.add(terms.get(j).derivative(var));
                } else {
                    productTerms.add(terms.get(j));
                }
            }
            sumTerms.add(new Mul(productTerms.toArray(new Expression[0])));
        }
        return new Add(sumTerms.toArray(new Expression[0]));
    }
}
