package ru.nsu.vylegzhanin;

import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class Sub extends Expression {
    List<Expression> terms;

    Sub(Expression... terms) {
        this.terms = Arrays.asList(terms);
    }


    @Override
    String print(){
        String result = "(";
        for (int i = 0; i  < terms.size(); i++) {
            result += terms.get(i).print();
            if (i != terms.size() - 1) {
                result += "-";
            }
        }
        result += ")";
        return result;
    }

    @Override
    int eval(Map<String, Integer> vars) {
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
        List<Expression> derivedTerms = new ArrayList<>();;
        for (Expression term : terms) {
            derivedTerms.add(term.derivative(var));
        }
        return new Sub(derivedTerms.toArray(new Expression[0]));
    }
    
    @Override
    Expression simplify() {
        // Сначала упрощаем все слагаемые
        List<Expression> simplifiedTerms = new ArrayList<>();
        for (Expression term : terms) {
            simplifiedTerms.add(term.simplify());
        }
        
        // Если только два элемента, проверяем на равенство (a - a = 0)
        if (simplifiedTerms.size() == 2) {
            if (simplifiedTerms.get(0).isEqual(simplifiedTerms.get(1))) {
                return new Number(0);
            }
        }
        
        // Если нет переменных, вычисляем результат
        boolean hasVars = false;
        for (Expression term : simplifiedTerms) {
            if (term.hasVariables()) {
                hasVars = true;
                break;
            }
        }
        
        if (!hasVars) {
            int result = ((Number) simplifiedTerms.get(0)).value;
            for (int i = 1; i < simplifiedTerms.size(); i++) {
                result -= ((Number) simplifiedTerms.get(i)).value;
            }
            return new Number(result);
        }
        
        return new Sub(simplifiedTerms.toArray(new Expression[0]));
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
        if (!(other instanceof Sub)) {
            return false;
        }
        
        Sub otherSub = (Sub) other;
        if (terms.size() != otherSub.terms.size()) {
            return false;
        }
        
        for (int i = 0; i < terms.size(); i++) {
            if (!terms.get(i).isEqual(otherSub.terms.get(i))) {
                return false;
            }
        }
        return true;
    }
}
