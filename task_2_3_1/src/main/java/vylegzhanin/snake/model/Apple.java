package vylegzhanin.snake.model;

/**
 * Класс, представляющий классическое яблоко в игре.
 * Реализует интерфейс {@link Item}.
 * При съедании увеличивает длину змейки, проверяет условие победы и спавнит новое яблоко.
 */
public class Apple implements Item {
    private final Point position;

    /**
     * Создает новое яблоко в указанных координатах.
     *
     * @param position позиция яблока на поле.
     */
    public Apple(Point position) {
        this.position = position;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void onConsumed(Game game) {
        game.getSnake().grow();
        game.checkWinCondition();
        game.spawnItem(new Apple(game.findFreeSpot()));
    }
}
