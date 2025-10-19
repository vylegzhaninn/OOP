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
    Map<Vertex, List<Vertex>> adjList;

    public AdjacencyList() {
        adjList = new HashMap<>();
    }

    @Override
    public void addVertex(Vertex vertex) {
        adjList.putIfAbsent(vertex, new ArrayList<>());
    }

    @Override
    public void removeVertex(Vertex vertex) {
        adjList.remove(vertex);
        for (List<Vertex> neighbors : adjList.values()) {
            neighbors.remove(vertex);
        }
    }

    @Override
    public void addEdge(Vertex from, Vertex to) {
        addVertex(from);
        addVertex(to);
        adjList.get(from).add(to);
    }

    @Override
    public void removeEdge(Vertex from, Vertex to) {
        if (adjList.containsKey(from)) {
            adjList.get(from).remove(to);
        }
    }

    @Override
    public List<Vertex> getNeighbors(Vertex vertex) {
        return adjList.getOrDefault(vertex, new ArrayList<>());
    }

    @Override
    public List<Vertex> getAllVertices() {
        return new ArrayList<>(adjList.keySet());
    }

    @Override
    public boolean hasEdge(Vertex from, Vertex to) {
        return adjList.containsKey(from) && adjList.get(from).contains(to);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Graph)) return false;
        Graph other = (Graph) obj;
        
        List<Vertex> otherVertices = other.getAllVertices();
        if (adjList.size() != otherVertices.size()) return false;
        if (!adjList.keySet().containsAll(otherVertices)) return false;
        
        for (Vertex v : adjList.keySet()) {
            for (Vertex u : adjList.keySet()) {
                if (hasEdge(v, u) != other.hasEdge(v, u)) return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = adjList.keySet().hashCode();
        for (Vertex v : adjList.keySet()) {
            for (Vertex neighbor : adjList.get(v)) {
                result = 31 * result + v.hashCode() + neighbor.hashCode();
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Граф (список смежности):\n");
        for (Vertex v : adjList.keySet()) {
            sb.append(v.getZnach()).append(": ");
            List<Vertex> neighbors = adjList.get(v);
            for (int i = 0; i < neighbors.size(); i++) {
                sb.append(neighbors.get(i).getZnach());
                if (i < neighbors.size() - 1) sb.append(", ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
