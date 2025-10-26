package ru.nsu.vylegzhanin.representations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.nsu.vylegzhanin.interfaces.Graph;
import ru.nsu.vylegzhanin.model.Vertex;

/**
 * Реализация графа на основе списка смежности.
 * Использует HashMap для хранения списка соседей каждой вершины.
 */
public class AdjacencyList implements Graph {
    private Map<Vertex, List<Vertex>> adjMap;

    public AdjacencyList() {
        adjMap = new HashMap<>();
    }

    @Override
    public void addVertex(Vertex vertex) {
        adjMap.putIfAbsent(vertex, new ArrayList<>());
    }

    @Override
    public void removeVertex(Vertex vertex) {
        adjMap.remove(vertex);
        for (List<Vertex> neighbors : adjMap.values()) {
            neighbors.remove(vertex);
        }
    }

    @Override
    public void addEdge(Vertex from, Vertex to) {
        addVertex(from);
        addVertex(to);
        adjMap.get(from).add(to);
    }

    @Override
    public void removeEdge(Vertex from, Vertex to) {
        if (adjMap.containsKey(from)) {
            adjMap.get(from).remove(to);
        }
    }

    @Override
    public List<Vertex> getNeighbors(Vertex vertex) {
        return adjMap.getOrDefault(vertex, new ArrayList<>());
    }

    @Override
    public List<Vertex> getAllVertices() {
        return new ArrayList<>(adjMap.keySet());
    }

    @Override
    public boolean hasEdge(Vertex from, Vertex to) {
        return adjMap.containsKey(from) && adjMap.get(from).contains(to);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Graph)) return false;
        Graph other = (Graph) obj;
        
        List<Vertex> otherVertices = other.getAllVertices();
        if (adjMap.size() != otherVertices.size()) return false;
        if (!adjMap.keySet().containsAll(otherVertices)) return false;
        
        for (Vertex v : adjMap.keySet()) {
            for (Vertex u : adjMap.keySet()) {
                if (hasEdge(v, u) != other.hasEdge(v, u)) return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = adjMap.keySet().hashCode();
        for (Vertex v : adjMap.keySet()) {
            for (Vertex neighbor : adjMap.get(v)) {
                result = 31 * result + v.hashCode() + neighbor.hashCode();
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Граф (список смежности):\n");
        for (Vertex v : adjMap.keySet()) {
            sb.append(v.getValue()).append(": ");
            List<Vertex> neighbors = adjMap.get(v);
            for (int i = 0; i < neighbors.size(); i++) {
                sb.append(neighbors.get(i).getValue());
                if (i < neighbors.size() - 1) sb.append(", ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
