package ru.nsu.vylegzhanin.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import ru.nsu.vylegzhanin.interfaces.Graph;
import ru.nsu.vylegzhanin.model.Vertex;

/**
 * Класс для чтения графа из файла.
 * Поддерживает формат: "вершина: сосед1, сосед2, ..."
 */
public class GraphReader {

    /**
     * Читает граф из файла и заполняет переданный объект графа.
     * Формат файла: каждая строка содержит вершину и список соседей через двоеточие.
     *
     * @param graph граф, который будет заполнен данными из файла
     * @param filename имя файла для чтения
     * @throws IOException если возникла ошибка при чтении файла
     */
    public static void readFromFile(Graph graph, String filename) throws IOException {
        Map<Integer, Vertex> vertexMap = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split(":");
                int vertexValue = Integer.parseInt(parts[0].trim());
                
                Vertex vertex = vertexMap.computeIfAbsent(vertexValue, Vertex::new);
                graph.addVertex(vertex);
                
                if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                    String[] neighbors = parts[1].split(",");
                    for (String neighborStr : neighbors) {
                        int neighborValue = Integer.parseInt(neighborStr.trim());
                        Vertex neighbor = vertexMap.computeIfAbsent(neighborValue, Vertex::new);
                        graph.addVertex(neighbor);
                        graph.addEdge(vertex, neighbor);
                    }
                }
            }
        }
    }
}
