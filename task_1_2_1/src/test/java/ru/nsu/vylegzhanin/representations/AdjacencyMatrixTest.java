package ru.nsu.vylegzhanin.representations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.vylegzhanin.interfaces.Graph;
import ru.nsu.vylegzhanin.model.Vertex;

/**
 * Тесты для класса AdjacencyMatrix.
 */
class AdjacencyMatrixTest {

    private Graph graph;
    private Vertex v1, v2, v3;

    @BeforeEach
    void setUp() {
        graph = new AdjacencyMatrix();
        v1 = new Vertex(1);
        v2 = new Vertex(2);
        v3 = new Vertex(3);
    }

    @Test
    void testAddVertex() {
        graph.addVertex(v1);
        List<Vertex> vertices = graph.getAllVertices();
        assertEquals(1, vertices.size());
        assertTrue(vertices.contains(v1));
    }

    @Test
    void testAddDuplicateVertex() {
        graph.addVertex(v1);
        graph.addVertex(v1);
        assertEquals(1, graph.getAllVertices().size());
    }

    @Test
    void testRemoveVertex() {
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.removeVertex(v1);
        
        List<Vertex> vertices = graph.getAllVertices();
        assertEquals(1, vertices.size());
        assertFalse(vertices.contains(v1));
        assertTrue(vertices.contains(v2));
    }

    @Test
    void testAddEdge() {
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(v1, v2);
        
        assertTrue(graph.hasEdge(v1, v2));
        assertFalse(graph.hasEdge(v2, v1));
    }

    @Test
    void testRemoveEdge() {
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(v1, v2);
        graph.removeEdge(v1, v2);
        
        assertFalse(graph.hasEdge(v1, v2));
    }

    @Test
    void testGetNeighbors() {
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(v1, v2);
        graph.addEdge(v1, v3);
        
        List<Vertex> neighbors = graph.getNeighbors(v1);
        assertEquals(2, neighbors.size());
        assertTrue(neighbors.contains(v2));
        assertTrue(neighbors.contains(v3));
    }

    @Test
    void testGetNeighborsEmptyGraph() {
        List<Vertex> neighbors = graph.getNeighbors(v1);
        assertTrue(neighbors.isEmpty());
    }

    @Test
    void testGraphEquals() {
        Graph graph2 = new AdjacencyMatrix();
        
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(v1, v2);
        
        graph2.addVertex(v1);
        graph2.addVertex(v2);
        graph2.addEdge(v1, v2);
        
        assertEquals(graph, graph2);
    }

    @Test
    void testGraphNotEquals() {
        Graph graph2 = new AdjacencyMatrix();
        
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(v1, v2);
        
        graph2.addVertex(v1);
        graph2.addVertex(v2);
        
        assertNotEquals(graph, graph2);
    }

    @Test
    void testRemoveVertexWithEdges() {
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(v1, v2);
        graph.addEdge(v2, v3);
        
        graph.removeVertex(v2);
        
        assertFalse(graph.hasEdge(v1, v2));
        assertFalse(graph.hasEdge(v2, v3));
        assertEquals(2, graph.getAllVertices().size());
    }
}
