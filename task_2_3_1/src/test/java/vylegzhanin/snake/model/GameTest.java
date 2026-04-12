package vylegzhanin.snake.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vylegzhanin.snake.model.items.Apple;
import vylegzhanin.snake.model.items.Snake;

class GameTest {
    private Game game;
    private Level testLevel;

    @BeforeEach
    void setUp() {
        game = new Game();
        testLevel = new Level(1, 10, 10, 3, 1000, 1);
        game.loadLevel(testLevel);
    }

    @Test
    void gameShouldInitializeCorrectly() {
        assertFalse(game.isGameOver());
        assertFalse(game.isWon());
        assertFalse(game.isLevelCompleted());
        assertEquals(1, game.getSnake().getBody().size());
        assertEquals(1, game.getItems().size());
    }

    @Test
    void gameShouldDetectWallCollision() {
        Snake snake = game.getSnake();

        for (int i = 0; i < 5; i++) {
            game.update();
        }

        assertTrue(game.isGameOver(), "Game should be over after hitting the right wall.");
    }

    @Test
    void observerPatternShouldWork() {
        final boolean[] notified = {false};
        GameObserver observer = (dto) -> notified[0] = true;

        game.addObserver(observer);
        game.stop();
        assertTrue(notified[0]);
    }

    @Test
    void testSpawnItem() {
        int initialItems = game.getItems().size();
        game.spawnItem(new Apple(new Point(0, 0)));
        assertEquals(initialItems + 1, game.getItems().size());
    }

    @Test
    void testWinCondition() {
        assertFalse(game.isLevelCompleted());
        game.getSnake().grow();
        game.getSnake().grow();
        game.checkWinCondition();
        assertTrue(game.isLevelCompleted());
    }

    @Test
    void testSetWon() {
        game.setWon(true);
        assertTrue(game.isWon());
    }

    @Test
    void testFindFreeSpotThrowsExceptionIfBoardFull() {
        Level smallLevel = new Level(1, 1, 1, 3, 1000, 0);
        game.loadLevel(smallLevel);
        assertThrows(IllegalStateException.class, () -> game.findFreeSpot());
    }

    @Test
    void updateEarlyReturnIfGameEnded() {
        game.setWon(true);
        Snake snake = game.getSnake();
        Point p = snake.getHead();
        game.update();
        assertEquals(p, snake.getHead());
    }

    @Test
    void updateSelfCollision() {
        Level bigLevel = new Level(1, 20, 20, 10, 1000, 0);
        game.loadLevel(bigLevel);

        for (int i = 0; i < 4; i++) {
            game.getSnake().grow();
            game.update();
        }

        game.getSnake().setDirection(Direction.DOWN);
        game.update();

        game.getSnake().setDirection(Direction.LEFT);
        game.update();

        game.getSnake().setDirection(Direction.UP);
        game.update();

        assertTrue(game.isGameOver(), "Should be game over from self collision");
    }

    @Test
    void testItemConsumptionOnUpdate() {
        game.getItems().clear();
        Snake snake = game.getSnake();
        snake.setDirection(Direction.RIGHT);

        Point nextHeadPos = new Point(snake.getHead().x() + 1, snake.getHead().y());
        Apple fixedApple = new Apple(nextHeadPos);
        game.spawnItem(fixedApple);

        int initialLen = snake.getBody().size();
        game.update();

        assertEquals(initialLen + 1, snake.getBody().size());
        assertFalse(game.getItems().contains(fixedApple));
    }
}
