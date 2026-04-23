package vylegzhanin.snake.model;

import java.util.List;
import vylegzhanin.snake.model.items.Item;
import vylegzhanin.snake.model.items.Snake;

public record GameDTO(
    Level currentLevel,
    Snake snake,
    List<Item> items,
    boolean isGameOver,
    boolean isWon,
    boolean levelCompleted,
    boolean isRunning
) {
}
