package ru.nsu.vylegzhanin.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import ru.nsu.vylegzhanin.algorithms.TopologicalSort;
import ru.nsu.vylegzhanin.interfaces.Graph;
import ru.nsu.vylegzhanin.model.Vertex;
import ru.nsu.vylegzhanin.representations.AdjacencyList;
import ru.nsu.vylegzhanin.representations.AdjacencyMatrix;
import ru.nsu.vylegzhanin.representations.IncidenceMatrix;

public class ConsoleInterface {
    private Scanner scanner;
    private Graph graph;
    private Map<Integer, Vertex> vertexCache;

    public ConsoleInterface() {
        scanner = new Scanner(System.in);
        vertexCache = new HashMap<>();
    }

    public void run() {
        boolean running = true;
        
        while (running) {
            printMenu();
            int choice = readInt("Выберите действие: ");
            System.out.println();
            
            switch (choice) {
                case 1: loadGraphFromFile(); break;
                case 2: printGraph(); break;
                case 3: compareGraphs(); break;
                case 4: addVertex(); break;
                case 5: removeVertex(); break;
                case 6: addEdge(); break;
                case 7: removeEdge(); break;
                case 8: getNeighbors(); break;
                case 9: topologicalSort(); break;
                case 0: running = false; break;
                default: System.out.println("Неверный выбор\n");
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println("Выберите действие:");
        System.out.println("1. Загрузить граф из файла");
        System.out.println("2. Вывести граф");
        System.out.println("3. Проверить на равенство");
        System.out.println("4. Добавить вершину");
        System.out.println("5. Удалить вершину");
        System.out.println("6. Добавить ребро");
        System.out.println("7. Удалить ребро");
        System.out.println("8. Получить соседей вершины");
        System.out.println("9. Топологическая сортировка");
        System.out.println("0. Выход");
    }

    private void loadGraphFromFile() {
        System.out.println("Выберите тип представления графа:");
        System.out.println("1. Матрица смежности");
        System.out.println("2. Список смежности");
        System.out.println("3. Матрица инцидентности");
        
        int choice = readInt("Ваш выбор: ");
        graph = createGraph(choice);
        
        if (graph == null) {
            System.out.println("Неверный выбор\n");
            return;
        }
        
        System.out.print("Введите имя файла: ");
        String filename = scanner.nextLine();
        
        try {
            vertexCache.clear();
            GraphReader.readFromFile(graph, filename);
            updateVertexCache();
            System.out.println("Граф загружен из файла " + filename + "\n");
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage() + "\n");
        }
    }

    private void printGraph() {
        if (!checkGraphExists()) return;
        System.out.println(graph);
    }

    private void compareGraphs() {
        if (!checkGraphExists()) return;
        
        System.out.println("Выберите тип второго графа:");
        System.out.println("1. Матрица смежности");
        System.out.println("2. Список смежности");
        System.out.println("3. Матрица инцидентности");
        
        Graph graph2 = createGraph(readInt("Ваш выбор: "));
        if (graph2 == null) {
            System.out.println("Неверный выбор\n");
            return;
        }
        
        System.out.print("Введите имя файла: ");
        String filename = scanner.nextLine();
        
        try {
            GraphReader.readFromFile(graph2, filename);
            System.out.println("\nГраф 1:\n" + graph);
            System.out.println("Граф 2:\n" + graph2);
            System.out.println("Графы " + (graph.equals(graph2) ? "РАВНЫ" : "НЕ РАВНЫ") + "\n");
        } catch (IOException e) {
            System.out.println("Ошибка: " + e.getMessage() + "\n");
        }
    }

    private void addVertex() {
        if (!checkGraphExists()) return;
        int value = readInt("Введите значение вершины: ");
        Vertex vertex = vertexCache.computeIfAbsent(value, Vertex::new);
        graph.addVertex(vertex);
        System.out.println("Вершина " + value + " добавлена\n");
    }

    private void removeVertex() {
        if (!checkGraphExists()) return;
        int value = readInt("Введите значение вершины для удаления: ");
        Vertex vertex = vertexCache.get(value);
        
        if (vertex != null) {
            graph.removeVertex(vertex);
            vertexCache.remove(value);
            System.out.println("Вершина " + value + " удалена\n");
        } else {
            System.out.println("Вершина не найдена\n");
        }
    }

    private void addEdge() {
        if (!checkGraphExists()) return;
        int from = readInt("Введите начальную вершину: ");
        int to = readInt("Введите конечную вершину: ");
        
        Vertex vFrom = vertexCache.computeIfAbsent(from, Vertex::new);
        Vertex vTo = vertexCache.computeIfAbsent(to, Vertex::new);
        
        graph.addVertex(vFrom);
        graph.addVertex(vTo);
        graph.addEdge(vFrom, vTo);
        System.out.println("Ребро " + from + " -> " + to + " добавлено\n");
    }

    private void removeEdge() {
        if (!checkGraphExists()) return;
        int from = readInt("Введите начальную вершину: ");
        int to = readInt("Введите конечную вершину: ");
        
        Vertex vFrom = vertexCache.get(from);
        Vertex vTo = vertexCache.get(to);
        
        if (vFrom != null && vTo != null) {
            graph.removeEdge(vFrom, vTo);
            System.out.println("Ребро " + from + " -> " + to + " удалено\n");
        } else {
            System.out.println("Одна или обе вершины не найдены\n");
        }
    }

    private void getNeighbors() {
        if (!checkGraphExists()) return;
        Vertex vertex = vertexCache.get(readInt("Введите значение вершины: "));
        
        if (vertex == null) {
            System.out.println("Вершина не найдена\n");
            return;
        }
        
        List<Vertex> neighbors = graph.getNeighbors(vertex);
        System.out.print("Соседи: ");
        if (neighbors.isEmpty()) {
            System.out.println("нет");
        } else {
            for (int i = 0; i < neighbors.size(); i++) {
                System.out.print(neighbors.get(i).getValue());
                if (i < neighbors.size() - 1) System.out.print(", ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void topologicalSort() {
        if (!checkGraphExists()) return;
        
        try {
            List<Vertex> sorted = TopologicalSort.kahnSort(graph);
            System.out.print("Топологическая сортировка: ");
            for (int i = 0; i < sorted.size(); i++) {
                System.out.print(sorted.get(i).getValue());
                if (i < sorted.size() - 1) System.out.print(" -> ");
            }
            System.out.println("\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage() + "\n");
        }
    }

    private Graph createGraph(int choice) {
        switch (choice) {
            case 1: return new AdjacencyMatrix();
            case 2: return new AdjacencyList();
            case 3: return new IncidenceMatrix();
            default: return null;
        }
    }

    private void updateVertexCache() {
        for (Vertex v : graph.getAllVertices()) {
            vertexCache.put(v.getValue(), v);
        }
    }

    private boolean checkGraphExists() {
        if (graph == null) {
            System.out.println("Граф не загружен. Сначала загрузите граф (пункт 1)\n");
            return false;
        }
        return true;
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Ошибка! Введите целое число: ");
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }
}
