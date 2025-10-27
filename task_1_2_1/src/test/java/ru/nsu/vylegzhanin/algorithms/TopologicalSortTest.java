package ru.nsu.vylegzhanin.algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.vylegzhanin.interfaces.Graph;
import ru.nsu.vylegzhanin.model.Vertex;
import ru.nsu.vylegzhanin.representations.AdjacencyMatrix;

/**
 * Тесты для класса TopologicalSort.
 */
class TopologicalSortTest {

    private Graph graph;
    private Vertex v1, v2, v3, v4, v5;

    @BeforeEach
    void setUp() {
        graph = new AdjacencyMatrix();
        v1 = new Vertex(1);
        v2 = new Vertex(2);
        v3 = new Vertex(3);
        v4 = new Vertex(4);
        v5 = new Vertex(5);
    }

    @Test
    void testTopologicalSortSimpleGraph() {
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(v1, v2);
        graph.addEdge(v2, v3);
        
        List<Vertex> sorted = TopologicalSort.kahnSort(graph);
        
        assertEquals(3, sorted.size());
        assertTrue(sorted.indexOf(v1) < sorted.indexOf(v2));
        assertTrue(sorted.indexOf(v2) < sorted.indexOf(v3));
    }

    @Test
    void testTopologicalSortComplexGraph() {
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addVertex(v5);
        
        graph.addEdge(v1, v2);
        graph.addEdge(v1, v3);
        graph.addEdge(v2, v4);
        graph.addEdge(v3, v4);
        graph.addEdge(v4, v5);
        
        List<Vertex> sorted = TopologicalSort.kahnSort(graph);
        
        assertEquals(5, sorted.size());
        assertTrue(sorted.indexOf(v1) < sorted.indexOf(v2));
        assertTrue(sorted.indexOf(v1) < sorted.indexOf(v3));
        assertTrue(sorted.indexOf(v2) < sorted.indexOf(v4));
        assertTrue(sorted.indexOf(v3) < sorted.indexOf(v4));
        assertTrue(sorted.indexOf(v4) < sorted.indexOf(v5));
    }

    @Test
    void testTopologicalSortWithCycle() {
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(v1, v2);
        graph.addEdge(v2, v3);
        graph.addEdge(v3, v1); // цикл
        
        assertThrows(IllegalArgumentException.class, () -> {
            TopologicalSort.kahnSort(graph);
        });
    }

    @Test
    void testTopologicalSortEmptyGraph() {
        List<Vertex> sorted = TopologicalSort.kahnSort(graph);
        assertTrue(sorted.isEmpty());
    }

    @Test
    void testTopologicalSortSingleVertex() {
        graph.addVertex(v1);
        
        List<Vertex> sorted = TopologicalSort.kahnSort(graph);
        
        assertEquals(1, sorted.size());
        assertEquals(v1, sorted.get(0));
    }

    @Test
    void testGraphSortMethod() {
        // Тест метода sort() из интерфейса Graph
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(v1, v2);
        graph.addEdge(v2, v3);
        
        List<Vertex> sorted = graph.sort(TopologicalSort.KAHN);
        
        assertEquals(3, sorted.size());
        assertTrue(sorted.indexOf(v1) < sorted.indexOf(v2));
        assertTrue(sorted.indexOf(v2) < sorted.indexOf(v3));
    }

    @Test
    void testGraphSortMethodWithCycle() {
        // Тест метода sort() с циклом
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(v1, v2);
        graph.addEdge(v2, v3);
        graph.addEdge(v3, v1);
        
        assertThrows(IllegalArgumentException.class, () -> {
            graph.sort(TopologicalSort.KAHN);
        });
    }

    @Test
    void testGraphSortWithAlgorithmParameter() {
        // Тест метода sort() с параметром алгоритма
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(v1, v2);
        graph.addEdge(v2, v3);
        
        List<Vertex> sorted = graph.sort(TopologicalSort.KAHN);
        
        assertEquals(3, sorted.size());
        assertTrue(sorted.indexOf(v1) < sorted.indexOf(v2));
        assertTrue(sorted.indexOf(v2) < sorted.indexOf(v3));
    }

    @Test
    void testGraphSortWithCustomAlgorithm() {
        // Тест метода sort() с кастомным алгоритмом (лямбда)
        graph.addVertex(v1);
        graph.addVertex(v2);
        
        List<Vertex> sorted = graph.sort(g -> {
            // Простой кастомный алгоритм для теста
            return TopologicalSort.kahnSort(g);
        });
        
        assertEquals(2, sorted.size());
    }
}
