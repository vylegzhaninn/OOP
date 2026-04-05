package vylegzhanin.snake.controller;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import vylegzhanin.snake.model.Direction;
import vylegzhanin.snake.model.Game;

/**
 * Контроллер игрового интерфейса. Управляет отрисовкой и взаимодействием пользователя с игрой.
 */
public class GameController {
    private final Game game;

    /**
     * Инициализация обработчика.
     */
    public GameController(Game game, Canvas canvas) {
        this.game = game;

        Platform.runLater(() -> {
            if (canvas.getScene() != null) {
                canvas.getScene().setOnKeyPressed(this::handleKeyPressed);
            }
        });
    }

    /**
     * Обработка нажатий клавиш движения змейки.
     */
    private void handleKeyPressed(KeyEvent event) {
        if (game.getSnake() == null) {
            return;
        }

        switch (event.getCode()) {
            case UP, W -> game.getSnake().setDirection(Direction.UP);
            case DOWN, S -> game.getSnake().setDirection(Direction.DOWN);
            case LEFT, A -> game.getSnake().setDirection(Direction.LEFT);
            case RIGHT, D -> game.getSnake().setDirection(Direction.RIGHT);
            default -> {
                // ничего
            }
        }
    }
}
