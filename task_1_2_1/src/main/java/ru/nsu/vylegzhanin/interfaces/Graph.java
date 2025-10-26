package ru.nsu.vylegzhanin.interfaces;

import java.util.List;
import ru.nsu.vylegzhanin.algorithms.TopologicalSort;
import ru.nsu.vylegzhanin.model.Vertex;

/**
 * Интерфейс для представления ориентированного графа.
 * Предоставляет базовые операции для работы с вершинами и рёбрами.
 */
public interface Graph {
    /**
     * Добавляет вершину в граф.
     *
     * @param vertex вершина для добавления
     */
    void addVertex(Vertex vertex);

    /**
     * Удаляет вершину из графа вместе со всеми связанными рёбрами.
     *
     * @param vertex вершина для удаления
     */
    void removeVertex(Vertex vertex);

    /**
     * Добавляет ориентированное ребро между двумя вершинами.
     *
     * @param from начальная вершина ребра
     * @param to конечная вершина ребра
     */
    void addEdge(Vertex from, Vertex to);

    /**
     * Удаляет ребро между двумя вершинами.
     *
     * @param from начальная вершина ребра
     * @param to конечная вершина ребра
     */
    void removeEdge(Vertex from, Vertex to);

    /**
     * Возвращает список всех вершин графа.
     *
     * @return список вершин
     */
    List<Vertex> getAllVertices();

    /**
     * Возвращает список соседей (исходящих вершин) для заданной вершины.
     *
     * @param vertex вершина, для которой ищутся соседи
     * @return список соседних вершин
     */
    List<Vertex> getNeighbors(Vertex vertex);

    /**
     * Проверяет наличие ребра между двумя вершинами.
     *
     * @param from начальная вершина
     * @param to конечная вершина
     * @return true, если ребро существует, иначе false
     */
    boolean hasEdge(Vertex from, Vertex to);

    /**
     * Выполняет топологическую сортировку графа.
     * Использует алгоритм Кана для получения линейного упорядочивания вершин.
     *
     * @return список вершин в топологическом порядке
     * @throws IllegalArgumentException если граф содержит цикл
     */
    default List<Vertex> sort() {
        return TopologicalSort.kahnSort(this);
    }
}
