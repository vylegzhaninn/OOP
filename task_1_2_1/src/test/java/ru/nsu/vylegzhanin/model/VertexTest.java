package ru.nsu.vylegzhanin.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

/**
 * Тесты для класса Vertex.
 */
class VertexTest {

    @Test
    void testVertexCreation() {
        Vertex vertex = new Vertex(1);
        assertEquals(1, vertex.getValue());
    }

    @Test
    void testVertexEquals() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(1);
        Vertex v3 = new Vertex(2);

        assertEquals(v1, v2);
        assertNotEquals(v1, v3);
        assertEquals(v1, v1);
    }

    @Test
    void testVertexHashCode() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(1);
        Vertex v3 = new Vertex(2);

        assertEquals(v1.hashCode(), v2.hashCode());
        assertNotEquals(v1.hashCode(), v3.hashCode());
    }
}
