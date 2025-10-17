package ru.nsu.vylegzhanin.representations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.nsu.vylegzhanin.interfaces.Graph;
import ru.nsu.vylegzhanin.model.Edge;
import ru.nsu.vylegzhanin.model.Vertex;

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
}
