package vylegzhanin.task241.service;

/**
 * Утилита для округления числовых значений баллов.
 */
public class Numbers {
    private static final double SCALE = 100.0;

    private Numbers() {
    }

    /**
     * Округляет значение до двух знаков после запятой.
     *
     * @param value исходное значение
     * @return значение, округлённое до двух знаков
     */
    public static double round2(double value) {
        return Math.round(value * SCALE) / SCALE;
    }
}
