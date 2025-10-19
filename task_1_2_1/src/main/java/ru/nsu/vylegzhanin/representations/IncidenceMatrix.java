package ru.nsu.vylegzhanin.representations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.nsu.vylegzhanin.interfaces.Graph;
import ru.nsu.vylegzhanin.model.Edge;
import ru.nsu.vylegzhanin.model.Vertex;

/**
 * Реализация графа на основе матрицы инцидентности.
 * Хранит информацию о рёбрах и их связях с вершинами.
 */
public class IncidenceMatrix implements Graph {
    Map<Vertex, List<Edge>> incMatrix;
    List<Edge> edges;

    public IncidenceMatrix() {
        incMatrix = new HashMap<>();
        edges = new ArrayList<>();
    }

    @Override
    public void addVertex(Vertex vertex) {
        incMatrix.putIfAbsent(vertex, new ArrayList<>());
    }

    @Override
    public void removeVertex(Vertex vertex) {
        List<Edge> toRemove = new ArrayList<>(incMatrix.getOrDefault(vertex, new ArrayList<>()));
        incMatrix.remove(vertex);
        
        for (Edge edge : toRemove) {
            edges.remove(edge);
            for (List<Edge> edgeList : incMatrix.values()) {
                edgeList.remove(edge);
            }
        }
    }

    @Override
    public void addEdge(Vertex from, Vertex to) {
        addVertex(from);
        addVertex(to);
        
        Edge edge = new Edge(from, to);
        edges.add(edge);
        incMatrix.get(from).add(edge);
        incMatrix.get(to).add(edge);
    }

    @Override
    public void removeEdge(Vertex from, Vertex to) {
        Edge toRemove = null;
        for (Edge edge : edges) {
            if (edge.getFrom().equals(from) && edge.getTo().equals(to)) {
                toRemove = edge;
                break;
            }
        }
        
        if (toRemove != null) {
            edges.remove(toRemove);
            incMatrix.get(from).remove(toRemove);
            incMatrix.get(to).remove(toRemove);
        }
    }

    @Override
    public List<Vertex> getNeighbors(Vertex vertex) {
        List<Vertex> neighbors = new ArrayList<>();
        List<Edge> vertexEdges = incMatrix.getOrDefault(vertex, new ArrayList<>());
        
        for (Edge edge : vertexEdges) {
            if (edge.getFrom().equals(vertex)) {
                neighbors.add(edge.getTo());
            }
        }
        return neighbors;
    }

    @Override
    public List<Vertex> getAllVertices() {
        return new ArrayList<>(incMatrix.keySet());
    }

    @Override
    public boolean hasEdge(Vertex from, Vertex to) {
        for (Edge edge : edges) {
            if (edge.getFrom().equals(from) && edge.getTo().equals(to)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Graph)) return false;
        Graph other = (Graph) obj;
        
        List<Vertex> otherVertices = other.getAllVertices();
        if (incMatrix.size() != otherVertices.size()) return false;
        if (!incMatrix.keySet().containsAll(otherVertices)) return false;
        
        for (Vertex v : incMatrix.keySet()) {
            for (Vertex u : incMatrix.keySet()) {
                if (hasEdge(v, u) != other.hasEdge(v, u)) return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = incMatrix.keySet().hashCode();
        for (Edge edge : edges) {
            result = 31 * result + edge.getFrom().hashCode() + edge.getTo().hashCode();
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Граф (матрица инцидентности):\n");
        for (Vertex v : incMatrix.keySet()) {
            sb.append(v.getZnach()).append(": ");
            List<Vertex> neighbors = getNeighbors(v);
            for (int i = 0; i < neighbors.size(); i++) {
                sb.append(neighbors.get(i).getZnach());
                if (i < neighbors.size() - 1) sb.append(", ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
