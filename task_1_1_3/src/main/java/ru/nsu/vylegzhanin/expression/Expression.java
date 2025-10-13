package ru.nsu.vylegzhanin.expression;

import java.util.Map;
import ru.nsu.vylegzhanin.parser.VariableParser;

/**
 * Абстрактный базовый класс для всех математических выражений.
 */
public abstract class Expression {
    /**
     * Возвращает строковое представление выражения.
     *
     * @return строковое представление
     */
    public abstract String print();

    /**
     * Вычисляет значение выражения с заданными переменными.
     *
     * @param vars карта переменных и их значений
     * @return результат вычисления
     */
    public abstract int eval(Map<String, Integer> vars);

    /**
     * Вычисляет значение выражения, парсируя строку переменных.
     *
     * @param varsString строка переменных в формате "x=5; y=10"
     * @return результат вычисления
     */
    public int eval(String varsString) {
        return eval(VariableParser.parseVars(varsString));
    }

    /**
     * Вычисляет производную выражения по заданной переменной.
     *
     * @param var переменная, по которой берется производная
     * @return производная выражения
     */
    public abstract Expression derivative(String var);

    /**
     * Упрощает выражение.
     *
     * @return упрощенное выражение
     */
    public abstract Expression simplify();

    /**
     * Проверяет, содержит ли выражение переменные.
     *
     * @return true, если содержит переменные, иначе false
     */
    public abstract boolean hasVariables();

    /**
     * Проверяет равенство двух выражений.
     *
     * @param other другое выражение для сравнения
     * @return true, если выражения равны, иначе false
     */
    public abstract boolean isEqual(Expression other);
}



