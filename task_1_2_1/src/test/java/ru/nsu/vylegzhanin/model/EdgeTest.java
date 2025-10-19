package ru.nsu.vylegzhanin.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Тесты для класса Edge.
 */
class EdgeTest {

    @Test
    void testEdgeCreation() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Edge edge = new Edge(v1, v2);

        assertEquals(v1, edge.getFrom());
        assertEquals(v2, edge.getTo());
    }

    @Test
    void testEdgeWithSameVertex() {
        Vertex v1 = new Vertex(1);
        Edge edge = new Edge(v1, v1);

        assertEquals(v1, edge.getFrom());
        assertEquals(v1, edge.getTo());
    }
}
