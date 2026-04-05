package vylegzhanin.snake.view;

import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import vylegzhanin.snake.controller.GameController;
import vylegzhanin.snake.model.Game;
import vylegzhanin.snake.model.GameObserver;
import vylegzhanin.snake.model.Level;
import vylegzhanin.snake.model.Point;
import vylegzhanin.snake.model.items.Apple;
import vylegzhanin.snake.model.items.Item;

/**
 * Основной класс логики view.
 */
public class GameView implements GameObserver {
    private static final int TILE_SIZE = 30;

    @FXML
    private Label levelLabel;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Button actionBtn;
    @FXML
    private Canvas canvas;

    private Game game;
    private List<Level> levels;
    private int currentLevelIndex = 0;

    private boolean isRunning = false;
    private AnimationTimer timer;

    /**
     * Метод инициализации view.
     */
    @FXML
    public void initialize() {
        this.game = new Game();
        this.game.addObserver(this);
        this.levels = List.of(
            new Level(1, 15, 15, 5, 200_000_000L, 1),
            new Level(2, 15, 15, 10, 150_000_000L, 2),
            new Level(3, 15, 15, 15, 100_000_000L, 3)
        );

        GameController gameController = new GameController(game, canvas);

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
                    game.notifyObservers();
                    lastUpdate = now;
                }
            }
        };

        javafx.application.Platform.runLater(this::draw);
    }

    @Override
    public void onGameStateChanged() {
        javafx.application.Platform.runLater(this::updateUi);
    }

    /**
     * Обработка нажатия кнопки действия (старт/рестарт/следующий уровень).
     */
    @FXML
    private void handleAction() {
        if (game.getCurrentLevel() == null) {
            loadCurrentLevel();
        } else if (isRunning || game.isGameOver() || game.isWon() || game.isLevelCompleted()) {
            if (game.isGameOver() || game.isWon()) {
                currentLevelIndex = 0;
                loadCurrentLevel();
            } else if (game.isLevelCompleted()) {
                currentLevelIndex++;
                loadCurrentLevel();
            } else {
                game.loadLevel(levels.get(currentLevelIndex)); // Обычный рестарт
            }
        }
        isRunning = true;
        actionBtn.setText("Restart");
        game.start();
        timer.start();
        updateUi();
    }

    /**
     * Обновляет элементы UI и перерисовывает сцену.
     */
    private void updateUi() {
        if (game.getCurrentLevel() != null) {
            levelLabel.setText("Level: " + game.getCurrentLevel().levelNumber());
        }
        scoreLabel.setText("Score: " + (game.getSnake() == null ? 0 : game.getScore())
            + "/" + (game.getCurrentLevel() == null ? "-" : game.getCurrentLevel().winLength()));

        if (game.isWon()) {
            statusLabel.setText("You Beat The Game!");
            statusLabel.setTextFill(Color.GREEN);
            actionBtn.setText("Play Again");
            isRunning = false;
            game.stop();
            timer.stop();
        } else if (game.isGameOver()) {
            statusLabel.setText("Game Over!");
            statusLabel.setTextFill(Color.RED);
            actionBtn.setText("Try Again");
            isRunning = false;
            game.stop();
            timer.stop();
        } else if (game.isLevelCompleted()) {
            statusLabel.setText("Level Passed!");
            statusLabel.setTextFill(Color.ORANGE);
            actionBtn.setText("Next Level");
            isRunning = false;
            game.stop();
            timer.stop();
        } else {
            if (isRunning) {
                statusLabel.setText("Playing...");
                statusLabel.setTextFill(Color.BLACK);
            } else {
                statusLabel.setText("Ready to start");
                statusLabel.setTextFill(Color.BLUE);
            }
        }
        draw();
    }

    /**
     * Перерисовывает игровое поле.
     */
    private void draw() {
        if (game.getCurrentLevel() == null) {
            return;
        }

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Item item : game.getItems()) {
            if (item instanceof Apple) {
                gc.setFill(Color.RED);
            } else {
                gc.setFill(Color.YELLOW);
            }
            Point p = item.position();
            gc.fillRect(p.x() * TILE_SIZE, p.y() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        gc.setFill(Color.GREEN);
        if (game.getSnake() != null) {
            for (Point p : game.getSnake().getBody()) {
                gc.fillRect(p.x() * TILE_SIZE, p.y() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    /**
     * Загружает текущий уровень или отмечает победу при отсутствии уровней.
     */
    private void loadCurrentLevel() {
        if (currentLevelIndex < levels.size()) {
            game.loadLevel(levels.get(currentLevelIndex));
        } else {
            game.setWon(true);
        }
        updateUi();
        draw();
    }
}
