package vylegzhanin.snake.view;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import vylegzhanin.snake.model.GameDTO;
import vylegzhanin.snake.model.GameObserver;
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

    private Runnable actionHandler;

    /**
     * Метод инициализации view.
     */
    @FXML
    public void initialize() {
        new vylegzhanin.snake.controller.GameController(canvas, this);
    }

    public void setActionHandler(Runnable actionHandler) {
        this.actionHandler = actionHandler;
    }

    @Override
    public void onGameStateChanged(GameDTO gameDTO) {
        javafx.application.Platform.runLater(() -> updateUi(gameDTO));
    }

    /**
     * Обработка нажатия кнопки действия (старт/рестарт/следующий уровень).
     */
    @FXML
    private void handleAction() {
        if (actionHandler != null) {
            actionHandler.run();
        }
    }

    /**
     * Обновляет элементы UI и перерисовывает сцену.
     */
    private void updateUi(GameDTO gameDTO) {
        if (gameDTO.currentLevel() != null) {
            levelLabel.setText("Level: " + gameDTO.currentLevel().levelNumber());
        }
        int score = gameDTO.snake() == null ? 0 : gameDTO.snake().getBody().size();
        scoreLabel.setText("Score: " + score
            + "/" + (gameDTO.currentLevel() == null ? "-" : gameDTO.currentLevel().winLength()));

        if (gameDTO.isWon()) {
            statusLabel.setText("You Beat The Game!");
            statusLabel.setTextFill(Color.GREEN);
            actionBtn.setText("Play Again");
        } else if (gameDTO.isGameOver()) {
            statusLabel.setText("Game Over!");
            statusLabel.setTextFill(Color.RED);
            actionBtn.setText("Try Again");
        } else if (gameDTO.levelCompleted()) {
            statusLabel.setText("Level Passed!");
            statusLabel.setTextFill(Color.ORANGE);
            actionBtn.setText("Next Level");
        } else {
            if (gameDTO.isRunning()) {
                statusLabel.setText("Playing...");
                statusLabel.setTextFill(Color.BLACK);
                actionBtn.setText("Restart");
            } else {
                statusLabel.setText("Ready to start");
                statusLabel.setTextFill(Color.BLUE);
                actionBtn.setText("Start");
            }
        }
        draw(gameDTO);
    }

    /**
     * Перерисовывает игровое поле.
     */
    private void draw(GameDTO gameDTO) {
        if (gameDTO == null || gameDTO.currentLevel() == null) {
            return;
        }

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Item item : gameDTO.items()) {
            if (item instanceof Apple) {
                gc.setFill(Color.RED);
            } else {
                gc.setFill(Color.YELLOW);
            }
            Point p = item.position();
            gc.fillRect(p.x() * TILE_SIZE, p.y() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        gc.setFill(Color.GREEN);
        if (gameDTO.snake() != null) {
            for (Point p : gameDTO.snake().getBody()) {
                gc.fillRect(p.x() * TILE_SIZE, p.y() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }
}
