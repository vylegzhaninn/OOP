package vylegzhanin.snake.view;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vylegzhanin.snake.model.Game;

class GameViewTest {

    @BeforeAll
    static void initJFX() {
        try {
            Platform.startup(() -> {
            });
        } catch (IllegalStateException e) {
        }
    }

    @Test
    void testInitializeAndHandleAction() throws Exception {
        GameView gameView = new GameView();

        injectField(gameView, "levelLabel", new Label());
        injectField(gameView, "scoreLabel", new Label());
        injectField(gameView, "statusLabel", new Label());
        injectField(gameView, "actionBtn", new Button());
        Canvas mockCanvas = new Canvas(100, 100);
        injectField(gameView, "canvas", mockCanvas);

        assertDoesNotThrow(gameView::initialize);

        Field gameField = GameView.class.getDeclaredField("game");
        gameField.setAccessible(true);
        Game game = (Game) gameField.get(gameView);
        assertNotNull(game);

        assertDoesNotThrow(gameView::onGameStateChanged);

        Method handleAction = GameView.class.getDeclaredMethod("handleAction");
        handleAction.setAccessible(true);

        assertDoesNotThrow(() -> handleAction.invoke(gameView));

        game.getSnake().grow();
        game.getSnake().grow();
        game.getSnake().grow();
        game.getSnake().grow();
        game.checkWinCondition();

        assertDoesNotThrow(() -> handleAction.invoke(gameView));

        game.update();
        for (int i = 0; i < 20; i++) {
            game.update();
        }

        assertDoesNotThrow(() -> handleAction.invoke(gameView));

        for (int i = 0; i < 10; i++) {
            game.setWon(true);
            assertDoesNotThrow(() -> handleAction.invoke(gameView));
        }
    }

    private void injectField(Object target, String name, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(target, value);
    }
}
