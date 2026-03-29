package vylegzhanin.snake.controller;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import vylegzhanin.snake.model.Apple;
import vylegzhanin.snake.model.Direction;
import vylegzhanin.snake.model.Game;
import vylegzhanin.snake.model.Item;
import vylegzhanin.snake.model.Level;
import vylegzhanin.snake.model.Point;

/**
 * Контроллер игрового интерфейса. Управляет отрисовкой и взаимодействием пользователя с игрой.
 */
public class GameController {
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
    private long lastUpdate = 0;
    private AnimationTimer timer;

    @FXML
    /** Инициализация контроллера: создаёт уровни, загружает текущий уровень и запускает таймер. */
    public void initialize() {
        game = new Game();
        levels = new ArrayList<>();
        // Уровень 1: Поле 15x15, нужно 10 яблок длинны, скорость 150мс, 2 яблока на поле
        levels.add(new Level(1, 15, 15, 10, 150_000_000L, 2));
        // Уровень 2: Поле 15x15, нужно 20 яблок длинны, скорость выше (120мс), 3 яблока
        levels.add(new Level(2, 15, 15, 20, 120_000_000L, 3));
        // Уровень 3: Еще быстрее (90мс), нужно 30 яблок, 4 яблока сразу
        levels.add(new Level(3, 15, 15, 30, 90_000_000L, 4));

        loadCurrentLevel();

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isRunning && now - lastUpdate >= game.getCurrentLevel().getTickDelayMs()) {
                    game.update();
                    updateUi();
                    lastUpdate = now;
                }
            }
        };

        Platform.runLater(() -> {
            canvas.getScene().setOnKeyPressed(this::handleKeyPressed);
            timer.start();
        });
    }

    /** Загружает текущий уровень или отмечает победу при отсутствии уровней. */
    private void loadCurrentLevel() {
        if (currentLevelIndex < levels.size()) {
            game.loadLevel(levels.get(currentLevelIndex));
        } else {
            game.setWon(true); // Прошли все уровни!
        }
        updateUi();
        draw();
    }

    @FXML
    /** Обработка нажатия кнопки действия (старт/рестарт/следующий уровень). */
    private void handleAction() {
        if (!isRunning && !game.isGameOver() && !game.isWon() && !game.isLevelCompleted()) {
            isRunning = true;
            actionBtn.setText("Restart");
            lastUpdate = System.nanoTime();
            updateUi();
        } else {
            if (game.isGameOver() || game.isWon()) {
                currentLevelIndex = 0; // Начинаем сначала
                loadCurrentLevel();
            } else if (game.isLevelCompleted()) {
                currentLevelIndex++; // Следующий уровень
                loadCurrentLevel();
            } else {
                game.loadLevel(levels.get(currentLevelIndex)); // Обычный рестарт
            }
            isRunning = true;
            actionBtn.setText("Restart");
            lastUpdate = System.nanoTime();
            updateUi();
        }
    }

    /** Обработка нажатий клавиш движения змейки. */
    private void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case UP, W ->
                game.getSnake().setDirection(Direction.UP);
            case DOWN, S ->
                game.getSnake().setDirection(Direction.DOWN);
            case LEFT, A ->
                game.getSnake().setDirection(Direction.LEFT);
            case RIGHT, D ->
                game.getSnake().setDirection(Direction.RIGHT);
            default -> {
                // ничего
            }
        }
    }

    /** Обновляет элементы UI и перерисовывает сцену. */
    private void updateUi() {
        if (game.getCurrentLevel() != null) {
            levelLabel.setText("Level: " + game.getCurrentLevel().getLevelNumber());
        }
        scoreLabel.setText("Score: " + game.getScore()
            + "/" + (game.getCurrentLevel() == null ? "-" : game.getCurrentLevel().getWinLength()));

        if (game.isWon()) {
            statusLabel.setText("You Beat The Game!");
            statusLabel.setTextFill(Color.GREEN);
            actionBtn.setText("Play Again");
            isRunning = false;
        } else if (game.isGameOver()) {
            statusLabel.setText("Game Over!");
            statusLabel.setTextFill(Color.RED);
            actionBtn.setText("Try Again");
            isRunning = false;
        } else if (game.isLevelCompleted()) {
            statusLabel.setText("Level Passed!");
            statusLabel.setTextFill(Color.ORANGE);
            actionBtn.setText("Next Level");
            isRunning = false;
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

    /** Перерисовывает игровое поле. */
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
                gc.setFill(Color.YELLOW); // на случай новых предметов
            }
            Point p = item.getPosition();
            gc.fillRect(p.x() * TILE_SIZE, p.y() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        gc.setFill(Color.GREEN);
        if (game.getSnake() != null) {
            for (Point p : game.getSnake().getBody()) {
                gc.fillRect(p.x() * TILE_SIZE, p.y() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }
}
