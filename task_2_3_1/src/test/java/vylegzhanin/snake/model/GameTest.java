package vylegzhanin.snake.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;
    private Level testLevel;

    @BeforeEach
    void setUp() {
        game = new Game();
        // Уровень: поле 10х10, победа при длине 3, быстрая задержка, 1 яблоко на старте
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
        // Змейка стартует в (5,5), идет вправо
        Snake snake = game.getSnake();
        
        // Делаем 5 шагов вправо, чтобы удариться о правую стену (x=10)
        for(int i = 0; i < 5; i++) {
            game.update();
        }
        
        assertTrue(game.isGameOver(), "Game should be over after hitting the right wall.");
    }
}
