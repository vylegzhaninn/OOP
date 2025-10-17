package ru.nsu.vylegzhanin.representations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.nsu.vylegzhanin.interfaces.Graph;
import ru.nsu.vylegzhanin.model.Vertex;

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
}
