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
 * Тесты для класса AdjacencyList.
 */
class AdjacencyListTest {

    private Graph graph;
    private Vertex v1, v2, v3;

    @BeforeEach
    void setUp() {
        graph = new AdjacencyList();
        v1 = new Vertex(1);
        v2 = new Vertex(2);
        v3 = new Vertex(3);
    }

    @Test
    void testAddVertex() {
        graph.addVertex(v1);
        assertEquals(1, graph.getAllVertices().size());
        assertTrue(graph.getAllVertices().contains(v1));
    }

    @Test
    void testAddEdge() {
        graph.addEdge(v1, v2);
        
        assertTrue(graph.hasEdge(v1, v2));
        assertFalse(graph.hasEdge(v2, v1));
    }

    @Test
    void testRemoveVertex() {
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(v1, v2);
        
        graph.removeVertex(v1);
        
        assertFalse(graph.getAllVertices().contains(v1));
        assertFalse(graph.hasEdge(v1, v2));
    }

    @Test
    void testGetNeighbors() {
        graph.addEdge(v1, v2);
        graph.addEdge(v1, v3);
        
        List<Vertex> neighbors = graph.getNeighbors(v1);
        assertEquals(2, neighbors.size());
        assertTrue(neighbors.contains(v2));
        assertTrue(neighbors.contains(v3));
    }
}
