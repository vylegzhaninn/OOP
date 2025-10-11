package ru.nsu.vylegzhanin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Класс для представления операции сложения выражений.
 */
public class Add extends Expression {
    List<Expression> terms;

    Add(Expression... terms) {
        this.terms = Arrays.asList(terms);
    }

    @Override
    String print() {
        String result = "(";
        for (int i = 0; i  < terms.size(); i++) {
            result += terms.get(i).print();
            if (i != terms.size() - 1) {
                result += "+";
            }
        }
        result += ")";
        return result;
    }

    @Override
    int eval(Map<String, Integer> vars) {
        int result = 0;
        for (Expression term : terms) {
            result += term.eval(vars);
        }
        return result;
    }

    @Override
    Expression derivative(String var) {
        List<Expression> derivedTerms = new ArrayList<>();;
        for (Expression term : terms) {
            derivedTerms.add(term.derivative(var));
        }
        return new Add(derivedTerms.toArray(new Expression[0]));
    }
    
    @Override
    Expression simplify() {
        List<Expression> simplifiedTerms = new ArrayList<>();
        for (Expression term : terms) {
            simplifiedTerms.add(term.simplify());
        }
        
        List<Expression> resultTerms = new ArrayList<>();
        int constantSum = 0;
        
        for (Expression term : simplifiedTerms) {
            if (term instanceof Number) {
                constantSum += ((Number) term).value;
            } else {
                resultTerms.add(term);
            }
        }
        
        if (constantSum != 0 || resultTerms.isEmpty()) {
            resultTerms.add(0, new Number(constantSum));
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
            int result = 0;
            for (Expression term : resultTerms) {
                result += ((Number) term).value;
            }
            return new Number(result);
        }
        
        return new Add(resultTerms.toArray(new Expression[0]));
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
        if (!(other instanceof Add)) {
            return false;
        }
        
        Add otherAdd = (Add) other;
        if (terms.size() != otherAdd.terms.size()) {
            return false;
        }
        
        for (int i = 0; i < terms.size(); i++) {
            if (!terms.get(i).isEqual(otherAdd.terms.get(i))) {
                return false;
            }
        }
        return true;
    }
}
