package ru.nsu.vylegzhanin.representations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.vylegzhanin.interfaces.Graph;
import ru.nsu.vylegzhanin.model.Vertex;

/**
 * Тесты для класса IncidenceMatrix.
 */
class IncidenceMatrixTest {

    private Graph graph;
    private Vertex v1, v2, v3;

    @BeforeEach
    void setUp() {
        graph = new IncidenceMatrix();
        v1 = new Vertex(1);
        v2 = new Vertex(2);
        v3 = new Vertex(3);
    }

    @Test
    void testAddVertex() {
        graph.addVertex(v1);
        assertEquals(1, graph.getAllVertices().size());
    }

    @Test
    void testAddEdge() {
        graph.addEdge(v1, v2);
        assertTrue(graph.hasEdge(v1, v2));
    }

    @Test
    void testRemoveEdge() {
        graph.addEdge(v1, v2);
        graph.removeEdge(v1, v2);
        assertFalse(graph.hasEdge(v1, v2));
    }

    @Test
    void testGetNeighbors() {
        graph.addEdge(v1, v2);
        graph.addEdge(v1, v3);
        
        List<Vertex> neighbors = graph.getNeighbors(v1);
        assertEquals(2, neighbors.size());
    }
}
