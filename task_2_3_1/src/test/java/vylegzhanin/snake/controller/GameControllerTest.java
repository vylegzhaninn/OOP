package vylegzhanin.snake.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vylegzhanin.snake.model.Direction;
import vylegzhanin.snake.model.Game;

class GameControllerTest {

    @BeforeAll
    static void initJFX() {
        try {
            Platform.startup(() -> {
            });
        } catch (IllegalStateException ignored) {
        }
    }

    @Test
    void testControllerHandlesInput() throws Exception {
        Canvas canvas = new Canvas();
        GameController controller = new GameController(canvas, new vylegzhanin.snake.view.GameView());

        java.lang.reflect.Field gameField = GameController.class.getDeclaredField("game");
        gameField.setAccessible(true);
        Game game = (Game) gameField.get(controller);

        Method handleKeyMethod =
            GameController.class.getDeclaredMethod("handleKeyPressed", KeyEvent.class);
        handleKeyMethod.setAccessible(true);

        KeyEvent wEvent =
            new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.W, false, false, false, false);
        handleKeyMethod.invoke(controller, wEvent);

        game.loadLevel(new vylegzhanin.snake.model.Level(1, 10, 10, 5, 100, 1));

        KeyEvent upEvent =
            new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.UP, false, false, false, false);
        handleKeyMethod.invoke(controller, upEvent);
        assertEquals(Direction.UP, getSnakeDirection(game));

        game.getSnake().move();

        KeyEvent leftEvent =
            new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.LEFT, false, false, false, false);
        handleKeyMethod.invoke(controller, leftEvent);
        assertEquals(Direction.LEFT, getSnakeDirection(game));

        game.getSnake().move();

        KeyEvent downEvent =
            new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.DOWN, false, false, false, false);
        handleKeyMethod.invoke(controller, downEvent);
        assertEquals(Direction.DOWN, getSnakeDirection(game));

        game.getSnake().move();

        KeyEvent rightEvent =
            new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.RIGHT, false, false, false, false);
        handleKeyMethod.invoke(controller, rightEvent);
        assertEquals(Direction.RIGHT, getSnakeDirection(game));

        KeyEvent spaceEvent =
            new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.SPACE, false, false, false, false);
        handleKeyMethod.invoke(controller, spaceEvent);
        assertEquals(Direction.RIGHT, getSnakeDirection(game));
    }

    private Direction getSnakeDirection(Game game) throws Exception {
        vylegzhanin.snake.model.items.Snake snake = game.getSnake();
        java.lang.reflect.Field field = snake.getClass().getDeclaredField("nextDirection");
        field.setAccessible(true);
        return (Direction) field.get(snake);
    }
}
