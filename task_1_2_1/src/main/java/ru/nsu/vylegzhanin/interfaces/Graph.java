package ru.nsu.vylegzhanin.interfaces;

import java.util.List;
import ru.nsu.vylegzhanin.model.Vertex;

public interface Graph {
    void addVertex(Vertex vertex);
    void removeVertex(Vertex vertex);
    void addEdge(Vertex from, Vertex to);
    void removeEdge(Vertex from, Vertex to);
    List<Vertex> getAllVertices();
    List<Vertex> getNeighbors(Vertex vertex);
}
