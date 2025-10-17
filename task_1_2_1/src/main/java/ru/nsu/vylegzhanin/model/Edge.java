package ru.nsu.vylegzhanin.model;

public class Edge {
    Vertex from;
    Vertex to;
    
    String name;

    public Edge(Vertex from, Vertex to) {
        this.from = from;
        this.to = to;
    }

    public Vertex getFrom() {
        return from;
    }

    public Vertex getTo() {
        return to;
    }
}
