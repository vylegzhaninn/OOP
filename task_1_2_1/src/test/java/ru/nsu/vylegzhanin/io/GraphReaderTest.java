package ru.nsu.vylegzhanin.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.nsu.vylegzhanin.interfaces.Graph;
import ru.nsu.vylegzhanin.model.Vertex;
import ru.nsu.vylegzhanin.representations.AdjacencyMatrix;

/**
 * Тесты для класса GraphReader.
 */
class GraphReaderTest {

    @TempDir
    Path tempDir;

    private Graph graph;

    @BeforeEach
    void setUp() {
        graph = new AdjacencyMatrix();
    }

    @Test
    void testReadSimpleGraph() throws IOException {
        Path file = tempDir.resolve("graph.txt");
        Files.writeString(file, "1: 2, 3\n2: 3\n3:\n");
        
        GraphReader.readFromFile(graph, file.toString());
        
        assertEquals(3, graph.getAllVertices().size());
        assertTrue(graph.hasEdge(new Vertex(1), new Vertex(2)));
        assertTrue(graph.hasEdge(new Vertex(1), new Vertex(3)));
        assertTrue(graph.hasEdge(new Vertex(2), new Vertex(3)));
    }

    @Test
    void testReadEmptyGraph() throws IOException {
        Path file = tempDir.resolve("empty.txt");
        Files.writeString(file, "");
        
        GraphReader.readFromFile(graph, file.toString());
        
        assertTrue(graph.getAllVertices().isEmpty());
    }

    @Test
    void testReadGraphWithEmptyLines() throws IOException {
        Path file = tempDir.resolve("graph.txt");
        Files.writeString(file, "1: 2\n\n2:\n");
        
        GraphReader.readFromFile(graph, file.toString());
        
        assertEquals(2, graph.getAllVertices().size());
        assertTrue(graph.hasEdge(new Vertex(1), new Vertex(2)));
    }

    @Test
    void testReadNonExistentFile() {
        assertThrows(IOException.class, () -> {
            GraphReader.readFromFile(graph, "nonexistent.txt");
        });
    }

    @Test
    void testReadGraphWithSelfLoop() throws IOException {
        Path file = tempDir.resolve("graph.txt");
        Files.writeString(file, "1: 1\n");
        
        GraphReader.readFromFile(graph, file.toString());
        
        assertTrue(graph.hasEdge(new Vertex(1), new Vertex(1)));
    }
}
