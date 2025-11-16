package ru.nsu.vylegzhanin.representations;

import java.util.ArrayList;
import java.util.List;
import ru.nsu.vylegzhanin.interfaces.Graph;
import ru.nsu.vylegzhanin.model.Vertex;

/**
 * Реализация графа на основе матрицы смежности.
 * Использует двумерный список для хранения информации о рёбрах.
 */
public class AdjacencyMatrix implements Graph {
    private List<Vertex> vertices = new ArrayList<>();
    private List<List<Boolean>> matrix = new ArrayList<>();

    @Override
    public void addVertex(Vertex vertex) {
        if (vertices.contains(vertex)) return;
        vertices.add(vertex);
        for (List<Boolean> row : matrix) {
            row.add(false);
        }
        List<Boolean> newRow = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            newRow.add(false);
        }
        matrix.add(newRow);
    }

    @Override
    public void removeVertex(Vertex vertex) {
        int idx = vertices.indexOf(vertex);
        if (idx == -1) return;
        vertices.remove(idx);
        matrix.remove(idx);
        for (List<Boolean> row : matrix) {
            row.remove(idx);
        }
    }

    @Override
    public void addEdge(Vertex from, Vertex to) {
        int i = vertices.indexOf(from), j = vertices.indexOf(to);
        if (i != -1 && j != -1) matrix.get(i).set(j, true);
    }

    @Override
    public void removeEdge(Vertex from, Vertex to) {
        int i = vertices.indexOf(from), j = vertices.indexOf(to);
        if (i != -1 && j != -1) matrix.get(i).set(j, false);
    }

    @Override
    public List<Vertex> getAllVertices() {
        return new ArrayList<>(vertices);
    }

    @Override
    public List<Vertex> getNeighbors(Vertex vertex) {
        int idx = vertices.indexOf(vertex);
        List<Vertex> neighbors = new ArrayList<>();
        if (idx != -1) {
            for (int j = 0; j < vertices.size(); j++) {
                if (matrix.get(idx).get(j)) neighbors.add(vertices.get(j));
            }
        }
        return neighbors;
    }

    @Override
    public boolean hasEdge(Vertex from, Vertex to) {
        int i = vertices.indexOf(from), j = vertices.indexOf(to);
        return i != -1 && j != -1 && matrix.get(i).get(j);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Graph)) return false;
        Graph other = (Graph) obj;
        
        List<Vertex> otherVertices = other.getAllVertices();
        if (vertices.size() != otherVertices.size()) return false;
        if (!vertices.containsAll(otherVertices)) return false;
        
        for (Vertex v : vertices) {
            for (Vertex u : vertices) {
                if (hasEdge(v, u) != other.hasEdge(v, u)) return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = vertices.hashCode();
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = 0; j < vertices.size(); j++) {
                if (matrix.get(i).get(j)) {
                    result = 31 * result + vertices.get(i).hashCode() + vertices.get(j).hashCode();
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Граф (матрица смежности):\n");
        for (Vertex v : vertices) {
            sb.append(v.getValue()).append(": ");
            List<Vertex> neighbors = getNeighbors(v);
            for (int i = 0; i < neighbors.size(); i++) {
                sb.append(neighbors.get(i).getValue());
                if (i < neighbors.size() - 1) sb.append(", ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
