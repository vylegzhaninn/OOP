package vylegzhanin.snake.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import vylegzhanin.snake.model.items.Apple;
import vylegzhanin.snake.model.items.Item;
import vylegzhanin.snake.model.items.Snake;

/**
 * Главный класс игровой логики (Движок).
 * Управляет состоянием змейки, предметами на поле, границами и правилами победы/поражения.
 */
public class Game {
    private Level currentLevel;
    private Snake snake;
    private final List<Item> items;

    private boolean isGameOver;
    private boolean isWon;
    private boolean levelCompleted;
    private final Random random;

    /**
     * Создает новый инстанс игры.
     */
    public Game() {
        this.items = new ArrayList<>();
        this.random = new Random();
    }

    /**
     * Загружает и инициализирует новый уровень.
     * Сбрасывает состояние змейки, предметы и статусы игры.
     *
     * @param level объект уровня (Level) с правилами генерации.
     */
    public void loadLevel(Level level) {
        this.currentLevel = level;
        this.snake = new Snake(new Point(level.width() / 2, level.height() / 2));
        this.items.clear();
        this.isGameOver = false;
        this.isWon = false;
        this.levelCompleted = false;

        for (int i = 0; i < level.initialApples(); i++) {
            Point p = findFreeSpot();
            if (p != null) {
                items.add(new Apple(p));
            }
        }
    }

    /**
     * Ищет случайную свободную точку на поле, не занятую змейкой или другими предметами.
     *
     * @return свободная точка (Point) или null, если за 100 попыток свободное место не найдено.
     */
    public Point findFreeSpot() throws IllegalStateException {
        for (int attempt = 0; attempt < 100; attempt++) {
            int x = random.nextInt(currentLevel.width());
            int y = random.nextInt(currentLevel.height());
            Point p = new Point(x, y);

            boolean collision = false;
            for (Point bodyPart : snake.getBody()) {
                if (bodyPart.equals(p)) {
                    collision = true;
                    break;
                }
            }
            for (Item item : items) {
                if (item.position().equals(p)) {
                    collision = true;
                    break;
                }
            }
            if (!collision) {
                return p;
            }
        }

        throw new IllegalStateException("findFreeSpot: Не получилось найти свободное место");
    }

    /**
     * Добавляет новый предмет на игровое поле.
     *
     * @param item предмет, реализующий интерфейс Item.
     */
    public void spawnItem(Item item) {
        if (item.position() != null) {
            items.add(item);
        }
    }

    /**
     * Проверяет, достигла ли змейка длины, необходимой для завершения текущего уровня.
     */
    public void checkWinCondition() {
        if (snake.getBody().size() >= currentLevel.winLength()) {
            levelCompleted = true;
        }
    }

    /**
     * Выполняет один игровой тик: перемещает змейку, проверяет коллизии со стенами,
     * собственным телом и предметами. Обновляет состояние игры (поражение/победа).
     */
    public void update() {
        if (isGameOver || isWon || levelCompleted) {
            return;
        }

        snake.move();
        Point head = snake.getHead();

        if (head.x() < 0
            || head.x() >= currentLevel.width()
            || head.y() < 0
            || head.y() >= currentLevel.height()) {
            isGameOver = true;
            return;
        }

        if (snake.checkSelfCollision()) {
            isGameOver = true;
            return;
        }

        Item eatenItem = null;
        for (Item item : items) {
            if (item.position().equals(head)) {
                eatenItem = item;
                break;
            }
        }

        if (eatenItem != null) {
            items.remove(eatenItem);
            eatenItem.onConsumed(this);
        }
    }


    public Snake getSnake() {
        return snake;
    }

    public List<Item> getItems() {
        return items;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isLevelCompleted() {
        return levelCompleted;
    }

    public void setWon(boolean won) {
        this.isWon = won;
    }

    public boolean isWon() {
        return isWon;
    }

    public int getScore() {
        return snake.getBody().size();
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }
}
