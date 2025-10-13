package ru.nsu.vylegzhanin.expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Класс для представления операции умножения выражений.
 */
public class Mul extends Expression {
    List<Expression> terms;

    public Mul(Expression... terms) {
        this.terms = Arrays.asList(terms);
    }

    @Override
    public String print() {
        StringBuilder result = new StringBuilder("(");
        for (int i = 0; i  < terms.size(); i++) {
            result.append(terms.get(i).print());
            if (i != terms.size() - 1) {
                result.append("*");
            }
        }
        result.append(")");
        return result.toString();
    }

    @Override
    public int eval(Map<String, Integer> vars) {
        int result = 1;
        for (Expression term : terms) {
            result *= term.eval(vars);  
        }
        return result;
    }

    @Override
    public Expression derivative(String var) {
        List<Expression> sumTerms = new ArrayList<>();

        for (int i = 0; i < terms.size(); i++) {
            List<Expression> productTerms = new ArrayList<>();

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
    
    @Override
    public Expression simplify() {
        List<Expression> simplifiedTerms = new ArrayList<>();
        for (Expression term : terms) {
            simplifiedTerms.add(term.simplify());
        }
        
        for (Expression term : simplifiedTerms) {
            if (term instanceof Number number && number.value == 0) {
                return new Number(0);
            }
        }
        
        List<Expression> resultTerms = new ArrayList<>();
        int constantProduct = 1;
        
        for (Expression term : simplifiedTerms) {
            if (term instanceof Number number) {
                constantProduct *= number.value;
            } else {
                resultTerms.add(term);
            }
        }
        
        if (constantProduct != 1 || resultTerms.isEmpty()) {
            resultTerms.add(0, new Number(constantProduct));
        }
        
        if (resultTerms.size() == 1) {
            return resultTerms.get(0);
        }
        
        boolean hasVars = false;
        for (Expression term : resultTerms) {
            if (term.hasVariables()) {
                hasVars = true;
                break;
            }
        }
        
        if (!hasVars) {
            int result = 1;
            for (Expression term : resultTerms) {
                if (term instanceof Number number) {
                    result *= number.value;
                }
            }
            return new Number(result);
        }
        
        return new Mul(resultTerms.toArray(new Expression[0]));
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
        if (!(other instanceof Mul otherMul)) {
            return false;
        }
        
        if (terms.size() != otherMul.terms.size()) {
            return false;
        }

        for (int i = 0; i < terms.size(); i++) {
            if (!terms.get(i).isEqual(otherMul.terms.get(i))) {
                return false;
            }
        }
        return true;
    }
}
