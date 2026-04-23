package vylegzhanin.snake.model.items;

import vylegzhanin.snake.model.Game;
import vylegzhanin.snake.model.Point;

/**
 * Класс, представляющий классическое яблоко в игре.
 * Реализует интерфейс {@link Item}.
 * При съедании увеличивает длину змейки, проверяет условие победы и спавнит новое яблоко.
 */
public record Apple(Point position) implements Item {
    /**
     * Создает новое яблоко в указанных координатах.
     *
     * @param position позиция яблока на поле.
     */
    public Apple {
    }

    @Override
    public void onConsumed(Game game) {
        game.getSnake().grow();
        game.checkWinCondition();
        game.spawnItem(new Apple(game.findFreeSpot()));
    }
}
