package vylegzhanin.snake.model;

/**
 * Перечисление для возможных направлений движения змейки.
 * Содержит смещения по осям X и Y для каждого направления,
 * а также логику определения противоположного направления.
 */
public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    /**
     * Возвращает направление, противоположное текущему.
     * Используется для предотвращения мгновенного разворота змейки на 180 градусов.
     *
     * @return противоположное направление.
     */
    public Direction opposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }
}
