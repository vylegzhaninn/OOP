package vylegzhanin.snake.controller;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import java.util.List;
import vylegzhanin.snake.model.Direction;
import vylegzhanin.snake.model.Game;
import vylegzhanin.snake.model.Level;
import vylegzhanin.snake.view.GameView;

/**
 * Контроллер игрового интерфейса. Управляет отрисовкой и взаимодействием пользователя с игрой.
 */
public class GameController {
    private final Game game;
    private final List<Level> levels;
    private int currentLevelIndex = 0;
    private final AnimationTimer timer;

    /**
     * Инициализация обработчика.
     */
    public GameController(Canvas canvas, GameView gameView) {
        game = new Game();
        game.addObserver(gameView);

        levels = List.of(
            new Level(1, 15, 15, 5, 200_000_000L, 1),
            new Level(2, 15, 15, 10, 150_000_000L, 2),
            new Level(3, 15, 15, 15, 100_000_000L, 3)
        );

        timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (!game.isRunning() || game.getCurrentLevel() == null) {
                    return;
                }

                long tickDelay = game.getCurrentLevel().tickDelayMs();
                if (now - lastUpdate >= tickDelay) {
                    game.update();
                    game.notifyObservers();
                    lastUpdate = now;
                }
            }
        };

        gameView.setActionHandler(this::handleAction);

        Platform.runLater(() -> {
            if (canvas.getScene() != null) {
                canvas.getScene().setOnKeyPressed(this::handleKeyPressed);
            }
        });
        
        // Отправляем первоначальное состояние
        game.notifyObservers();
    }

    private void handleAction() {
        if (game.getCurrentLevel() == null) {
            loadCurrentLevel();
        } else if (game.isRunning() || game.isGameOver() || game.isWon() || game.isLevelCompleted()) {
            if (game.isGameOver() || game.isWon()) {
                currentLevelIndex = 0;
                loadCurrentLevel();
            } else if (game.isLevelCompleted()) {
                currentLevelIndex++;
                loadCurrentLevel();
            } else {
                game.loadLevel(levels.get(currentLevelIndex));
            }
        }
        
        game.start();
        timer.start();
        game.notifyObservers();
    }

    private void loadCurrentLevel() {
        if (currentLevelIndex < levels.size()) {
            game.loadLevel(levels.get(currentLevelIndex));
        } else {
            game.setWon(true);
        }
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
