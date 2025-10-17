package ru.nsu.vylegzhanin.representations;

import java.util.ArrayList;
import java.util.List;

import ru.nsu.vylegzhanin.interfaces.Graph;
import ru.nsu.vylegzhanin.model.Vertex;

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
}
