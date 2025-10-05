package ru.nsu.vylegzhanin;

import java.util.HashMap;
import java.util.Map;

abstract class Expression {
    abstract String print();
    abstract int eval(Map<String, Integer> vars);
    abstract Expression derivative(String var);

    int eval(String varsString) {
        return eval(parseVars(varsString));
    }

    static Map<String, Integer> parseVars(String s) {
        Map<String, Integer> map = new HashMap<>();
        if (s == null || s.isBlank()) return map;

        for (String part : s.split("[,;]+")) {
            if (part.isBlank()) continue;
            int eq = part.indexOf('=');
            if (eq < 0) {
                throw new IllegalArgumentException("Ожидалось имя=значение, получено: " + part);
            }
            String key = part.substring(0, eq).trim();
            String val = part.substring(eq + 1).trim();
            map.put(key, Integer.parseInt(val));
        }
        return map;
    }
}



