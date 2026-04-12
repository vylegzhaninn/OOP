package vylegzhanin.snake.model;

/**
 * Интерфейс для реализации паттерна Наблюдатель (Observer).
 * Позволяет View подписываться на изменения в Модели (Game).
 */
public interface GameObserver {
    /**
     * Вызывается, когда состояние игры обновилось (тик таймера или счет).
     */
    void onGameStateChanged(GameDTO gameDto);
}
