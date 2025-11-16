package ru.nsu.vylegzhanin.algorithms;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import ru.nsu.vylegzhanin.interfaces.Graph;
import ru.nsu.vylegzhanin.interfaces.GraphSortAlgorithm;
import ru.nsu.vylegzhanin.model.Vertex;

/**
 * Класс для выполнения топологической сортировки графа.
 * Предоставляет различные алгоритмы сортировки.
 */
public class TopologicalSort {
    
    /**
     * Алгоритм Кана для топологической сортировки.
     * Использует подсчёт входящих степеней вершин.
     */
    public static final GraphSortAlgorithm KAHN = TopologicalSort::kahnSort;

    /**
     * Выполняет топологическую сортировку графа алгоритмом Кана.
     *
     * @param graph граф для сортировки
     * @return список вершин в топологическом порядке
     * @throws IllegalArgumentException если граф содержит цикл
     */
    public static List<Vertex> kahnSort(Graph graph) {
        List<Vertex> vertices = graph.getAllVertices();
        Map<Vertex, Integer> indegree = new HashMap<>();

        for (Vertex v : vertices) {
            for (Vertex neighbor : graph.getNeighbors(v)) {
                indegree.put(neighbor, indegree.getOrDefault(neighbor, 0) + 1);
            }
            indegree.putIfAbsent(v, indegree.getOrDefault(v, 0));
        }

        Queue<Vertex> queue = new ArrayDeque<>();
        for (Vertex v : vertices) {
            if (indegree.get(v) == 0) {
                queue.offer(v);
            }
        }

        List<Vertex> result = new ArrayList<>();

        while (!queue.isEmpty()) {
            Vertex current = queue.poll();
            result.add(current);

            for (Vertex neighbor : graph.getNeighbors(current)) {
                int newDegree = indegree.merge(neighbor, -1, Integer::sum);
                if (newDegree == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        if (result.size() != vertices.size()) {
            throw new IllegalArgumentException("Граф содержит цикл — топологическая сортировка невозможна!");
        }

        return result;
    }
}
