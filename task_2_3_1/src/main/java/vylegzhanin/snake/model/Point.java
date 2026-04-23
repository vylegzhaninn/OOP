package vylegzhanin.snake.model;

/**
 * Класс для представления точки (координат) на игровом поле.
 * Является неизменяемым (immutable) объектом.
 */
public record Point(int x, int y) {
    /**
     * Создает новую точку с указанными координатами.
     *
     * @param x координата X (по горизонтали).
     * @param y координата Y (по вертикали).
     */
    public Point {
    }

    /**
     * Возвращает координату X.
     *
     * @return координата X.
     */
    @Override
    public int x() {
        return x;
    }

    /**
     * Возвращает координату Y.
     *
     * @return координата Y.
     */
    @Override
    public int y() {
        return y;
    }

}
