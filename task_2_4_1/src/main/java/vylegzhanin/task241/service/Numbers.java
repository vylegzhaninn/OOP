package vylegzhanin.task241.service;

public class Numbers {
    private static final double SCALE = 100.0;

    private Numbers() {}

    public static double round2(double value) {
        return Math.round(value * SCALE) / SCALE;
    }
}
