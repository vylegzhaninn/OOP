package vylegzhanin.snake.view;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
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
    private static final int TARGET_FPS = 120;
    private static final long FRAME_INTERVAL_NS = 1_000_000_000L / TARGET_FPS;

    @FXML
    private Label levelLabel;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Button actionBtn;
    @FXML
    private Pane canvasPane;
    @FXML
    private Canvas canvas;

    private Runnable actionHandler;

    private double tileWidth = TILE_SIZE;
    private double tileHeight = TILE_SIZE;
    private GameDTO lastDto;
    private final AnimationTimer renderTimer = new AnimationTimer() {
        private long lastRenderNs;

        @Override
        public void handle(long now) {
            if (now - lastRenderNs < FRAME_INTERVAL_NS) {
                return;
            }
            lastRenderNs = now;
            if (lastDto != null) {
                draw(lastDto);
            }
        }
    };

    /**
     * Метод инициализации view.
     */
    @FXML
    public void initialize() {
        canvas.widthProperty().bind(canvasPane.widthProperty());
        canvas.heightProperty().bind(canvasPane.heightProperty());

        canvas.widthProperty().addListener((obs, oldVal, newVal) -> redrawOnResize());
        canvas.heightProperty().addListener((obs, oldVal, newVal) -> redrawOnResize());

        new vylegzhanin.snake.controller.GameController(canvas, this);
        javafx.application.Platform.runLater(renderTimer::start);
    }

    private void redrawOnResize() {
        if (lastDto != null && lastDto.currentLevel() != null) {
            tileWidth = canvas.getWidth() / lastDto.currentLevel().width();
            tileHeight = canvas.getHeight() / lastDto.currentLevel().height();
            draw(lastDto);
        }
    }

    public void setActionHandler(Runnable actionHandler) {
        this.actionHandler = actionHandler;
    }

    @Override
    public void onGameStateChanged(GameDTO gameDTO) {
        javafx.application.Platform.runLater(() -> {
            lastDto = gameDTO;
            redrawOnResize();
            updateUi(gameDTO);
        });
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
            gc.fillRect(p.x() * tileWidth, p.y() * tileHeight, tileWidth, tileHeight);
        }

        gc.setFill(Color.GREEN);
        if (gameDTO.snake() != null) {
            for (Point p : gameDTO.snake().getBody()) {
                gc.fillRect(p.x() * tileWidth, p.y() * tileHeight, tileWidth, tileHeight);
            }
        }
    }
}
