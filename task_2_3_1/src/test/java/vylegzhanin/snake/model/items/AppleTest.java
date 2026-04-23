package vylegzhanin.snake.model.items;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import vylegzhanin.snake.model.Game;
import vylegzhanin.snake.model.Level;
import vylegzhanin.snake.model.Point;

class AppleTest {

    @Test
    void testAppleCreation() {
        Point p = new Point(5, 5);
        Apple apple = new Apple(p);
        assertEquals(p, apple.position());
    }

    @Test
    void testAppleConsumed() {
        Game game = new Game();
        Level level = new Level(1, 10, 10, 3, 100L, 1);
        game.loadLevel(level);

        Snake snake = game.getSnake();
        int initialLength = snake.getBody().size();

        Apple apple = new Apple(new Point(0, 0));
        apple.onConsumed(game);

        assertEquals(initialLength + 1, snake.getBody().size());
        assertTrue(game.getItems().stream().anyMatch(item -> item instanceof Apple));
    }
}

