package vylegzhanin.snake.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import vylegzhanin.snake.model.items.Snake;

class SnakeTest {

    @Test
    void snakeShouldInitializeWithOneSegment() {
        Snake snake = new Snake(new Point(5, 5));
        assertEquals(1, snake.getBody().size());
        assertEquals(new Point(5, 5), snake.getHead());
    }

    @Test
    void snakeShouldMoveCorrectly() {
        Snake snake = new Snake(new Point(5, 5));
        // По умолчанию змейка смотрит RIGHT. dx=1, dy=0
        snake.move();

        assertEquals(new Point(6, 5), snake.getHead());
        assertEquals(1, snake.getBody().size()); // Длина не изменилась
    }

    @Test
    void snakeShouldGrowCorrectly() {
        Snake snake = new Snake(new Point(5, 5));
        snake.grow();

        assertEquals(2, snake.getBody().size());
        // Хвост добавлен в ту же позицию
        assertEquals(new Point(5, 5), snake.getBody().get(1));

        snake.move();
        // Теперь голова на (6,5), а хвост подтянулся на (5,5)
        assertEquals(new Point(6, 5), snake.getHead());
        assertEquals(new Point(5, 5), snake.getBody().get(1));
    }

    @Test
    void snakeShouldChangeDirectionButPrevent180Turn() {
        Snake snake = new Snake(new Point(5, 5));

        // Попытка повернуть налево (противоположно текущему RIGHT)
        snake.setDirection(Direction.LEFT);
        snake.move();

        // Должна продолжить идти вправо
        assertEquals(new Point(6, 5), snake.getHead());

        // Поворот вниз разрешен
        snake.setDirection(Direction.DOWN);
        snake.move();
        assertEquals(new Point(6, 6), snake.getHead());
    }
}

