package vylegzhanin.snake.view;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vylegzhanin.snake.model.GameDTO;
import vylegzhanin.snake.model.Point;
import vylegzhanin.snake.model.items.Snake;

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

        GameDTO fakeDto =
            new GameDTO(null, new Snake(new Point(5, 5)), new ArrayList<>(), false, false, false,
                false);

        assertDoesNotThrow(() -> gameView.onGameStateChanged(fakeDto));

        Method handleAction = GameView.class.getDeclaredMethod("handleAction");
        handleAction.setAccessible(true);

        assertDoesNotThrow(() -> handleAction.invoke(gameView));

        GameDTO wonDto =
            new GameDTO(null, new Snake(new Point(5, 5)), new ArrayList<>(), false, true, false,
                false);
        assertDoesNotThrow(() -> handleAction.invoke(gameView));
    }

    private void injectField(Object target, String name, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(target, value);
    }
}
