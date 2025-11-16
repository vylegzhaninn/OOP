package ru.nsu.vylegzhanin.model;

/**
 * Класс, представляющий ориентированное ребро графа.
 * Ребро соединяет две вершины: начальную (from) и конечную (to).
 */
public class Edge {
    private final Vertex from;
    private final Vertex to;

    /**
     * Создаёт новое ребро между двумя вершинами.
     *
     * @param from начальная вершина ребра
     * @param to конечная вершина ребра
     */
    public Edge(Vertex from, Vertex to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Возвращает начальную вершину ребра.
     *
     * @return начальная вершина
     */
    public Vertex getFrom() {
        return from;
    }

    /**
     * Возвращает конечную вершину ребра.
     *
     * @return конечная вершина
     */
    public Vertex getTo() {
        return to;
    }
}
