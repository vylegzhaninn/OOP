package vylegzhanin.snake.model;

/**
 * Класс, описывающий уровень игры.
 * Хранит настройки размера поля, условия победы и параметры сложности (скорость и количество еды).
 */
public record Level(int levelNumber, int width, int height, int winLength, long tickDelayMs,
                    int initialApples) {
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
    public Level {
    }
}
