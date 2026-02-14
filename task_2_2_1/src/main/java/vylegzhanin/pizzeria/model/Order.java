package vylegzhanin.pizzeria.model;

public record Order(long id, Long reservationTime, int size) {
    public Order(Long id, int size) {
        this(id, System.currentTimeMillis(), size);
    }
}
