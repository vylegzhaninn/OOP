package vylegzhanin.snake.model.items;

import java.util.LinkedList;
import java.util.List;
import vylegzhanin.snake.model.Direction;
import vylegzhanin.snake.model.Point;

/**
 * Класс, представляющий змейку на игровом поле.
 * Самостоятельно управляет своим телом (координатами звеньев), направлением движения
 * и процессами роста.
 */
public class Snake {
    private final LinkedList<Point> body;
    private Direction currentDirection;
    private Direction nextDirection;

    /**
     * Создает новую змейку длиной в одну клетку с заданным начальным положением.
     * По умолчанию змейка двигается вправо.
     *
     * @param startPosition координаты стартовой точки (головы).
     */
    public Snake(Point startPosition) {
        this.body = new LinkedList<>();
        this.body.add(startPosition);
        this.currentDirection = Direction.RIGHT;
        this.nextDirection = Direction.RIGHT;
    }

    /**
     * Устанавливает новое направление для следующего хода змейки.
     * Змейка не может мгновенно развернуться в противоположную сторону.
     *
     * @param direction новое направление движения.
     */
    public void setDirection(Direction direction) {
        if (currentDirection.opposite() != direction && direction != null) {
            this.nextDirection = direction;
        }
    }

    /**
     * Перемещает змейку на одну клетку в текущем направлении (nextDirection).
     * Добавляет новую голову и удаляет хвост.
     */
    public void move() {
        currentDirection = nextDirection;
        Point head = body.getFirst();
        Point newHead =
            new Point(head.x() + currentDirection.getDx(), head.y() + currentDirection.getDy());
        body.addFirst(newHead);
        body.removeLast();
    }

    /**
     * Увеличивает длину змейки, добавляя звено в текущую позицию хвоста.
     * Вызывается, когда змейка съедает еду.
     */
    public void grow() {
        Point tail = body.getLast();
        body.addLast(new Point(tail.x(), tail.y()));
    }

    /**
     * Возвращает позицию головы змейки (первое звено).
     *
     * @return позиция головы в виде объекта Point.
     */
    public Point getHead() {
        return body.getFirst();
    }

    /**
     * Возвращает список координат всех звеньев тела змейки.
     *
     * @return список объектов Point, соответствующих телу змейки (от головы к хвосту).
     */
    public List<Point> getBody() {
        return body;
    }

    /**
     * Проверяет, не пересекается ли голова змейки с её собственным телом.
     *
     * @return true, если произошло самопересечение (поражение), иначе false.
     */
    public boolean checkSelfCollision() {
        Point head = getHead();
        for (int i = 1; i < body.size(); i++) {
            if (head.equals(body.get(i))) {
                return true;
            }
        }
        return false;
    }
}
