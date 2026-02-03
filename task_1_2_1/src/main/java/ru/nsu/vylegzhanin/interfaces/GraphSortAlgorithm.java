package ru.nsu.vylegzhanin.interfaces;

import java.util.List;
import ru.nsu.vylegzhanin.model.Vertex;

/**
 * Интерфейс для алгоритмов топологической сортировки графа.
 * Реализации этого интерфейса предоставляют различные алгоритмы
 * для получения линейного упорядочивания вершин направленного графа.
 */
@FunctionalInterface
public interface GraphSortAlgorithm {
    /**
     * Выполняет топологическую сортировку графа.
     *
     * @param graph граф для сортировки
     * @return список вершин в топологическом порядке
     * @throws IllegalArgumentException если граф содержит цикл
     */
    List<Vertex> sort(Graph graph);
}
