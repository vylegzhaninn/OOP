package ru.nsu.vylegzhanin;

public class Parse {
    static Expression parse(String terms){
       
        if (terms.startsWith("(") && terms.endsWith(")")) {
            terms = terms.substring(1, terms.length() - 1);
        }

        terms= terms.replace(" ", "");

        int depth = 0;
        for (int i = 0; i < terms.length(); i++) {
            char c = terms.charAt(i);
            if (c == ' ') continue;
            if (c == '(') depth++;
            else if (c == ')') depth--;
            else if (depth == 0 && (c == '+' || c == '-' || c == '*' || c == '/')) {
                String left = terms.substring(0, i);
                String right = terms.substring(i + 1);

                Expression leftExpr = parse(left);
                Expression rightExpr = parse(right);

                return switch (c) {
                    case '+' -> new Add(leftExpr, rightExpr);
                    case '-' -> new Sub(leftExpr, rightExpr);
                    case '*' -> new Mul(leftExpr, rightExpr);
                    case '/' -> new Div(leftExpr, rightExpr);
                    default -> throw new IllegalArgumentException("Неизвестный оператор: " + c);
                };
            }
        }

        if (Character.isDigit(terms.charAt(0))) {
            return new Number(Integer.parseInt(terms));
        } else {
            return new Variable(terms);
        }
    }
}
