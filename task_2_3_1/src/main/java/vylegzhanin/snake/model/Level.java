package vylegzhanin.snake.model;

/**
 * Класс, описывающий уровень игры.
 * Хранит настройки размера поля, условия победы и параметры сложности (скорость и количество еды).
 */
public class Level {
    private final int levelNumber;
    private final int width;
    private final int height;
    private final int winLength;
    private final long tickDelayMs;
    private final int initialApples;

    /**
     * Создает новый уровень с заданными параметрами.
     *
     * @param levelNumber   номер уровня.
     * @param width         ширина игрового поля (в клетках).
     * @param height        высота игрового поля (в клетках).
     * @param winLength     длина змейки, необходимая для прохождения уровня.
     * @param tickDelayMs   задержка между шагами змейки в наносекундах (влияет на скорость игры).
     * @param initialApples количество яблок, которое должно одновременно находиться на поле.
     */
    public Level(int levelNumber, int width, int height, int winLength, long tickDelayMs,
                 int initialApples) {
        this.levelNumber = levelNumber;
        this.width = width;
        this.height = height;
        this.winLength = winLength;
        this.tickDelayMs = tickDelayMs;
        this.initialApples = initialApples;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getWinLength() {
        return winLength;
    }

    public long getTickDelayMs() {
        return tickDelayMs;
    }

    public int getInitialApples() {
        return initialApples;
    }
}
