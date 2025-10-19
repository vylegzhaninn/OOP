package ru.nsu.vylegzhanin.model;

import java.util.Objects;

/**
 * Класс, представляющий вершину графа.
 * Вершина идентифицируется целочисленным значением.
 */
public class Vertex {
    private final int value;

    /**
     * Создаёт новую вершину с заданным значением.
     *
     * @param value значение вершины
     */
    public Vertex(int value) {
        this.value = value;
    }

    /**
     * Возвращает значение вершины.
     *
     * @return значение вершины
     */
    public int getZnach() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vertex)) return false;
        return value == ((Vertex) obj).value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
