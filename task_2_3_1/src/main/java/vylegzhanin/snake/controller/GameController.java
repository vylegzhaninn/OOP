package vylegzhanin.snake.controller;

import javafx.animation.AnimationTimer;
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
    private final AnimationTimer timer;
    private boolean isRunning = false;

    /**
     * Инициализация обработчика.
     */
    public GameController(Game game, Canvas canvas, Runnable onUpdate) {
        this.game = game;

        this.timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (!isRunning || game.getCurrentLevel() == null) {
                    return;
                }

                long tickDelay = game.getCurrentLevel().tickDelayMs();
                if (now - lastUpdate >= tickDelay) {
                    game.update();
                    if (onUpdate != null) {
                        onUpdate.run();
                    }
                    lastUpdate = now;
                }
            }
        };

        Platform.runLater(() -> canvas.getScene().setOnKeyPressed(this::handleKeyPressed));
    }

    public void start() {
        isRunning = true;
        timer.start();
    }

    public void stop() {
        isRunning = false;
        timer.stop();
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
