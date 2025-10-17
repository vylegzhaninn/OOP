package ru.nsu.vylegzhanin.model;

import java.util.Objects;

public class Vertex {
    int znach;
    String name;

    public Vertex(int znach) {
        this.znach = znach;
    }

    public int getZnach() {
        return znach;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vertex vertex = (Vertex) obj;
        return znach == vertex.znach;
    }

    @Override
    public int hashCode() {
        return Objects.hash(znach);
    }
}
