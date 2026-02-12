package vylegzhanin.pizzeria.supportive;

public record Order(long id, Long reservationTime) {
    public Order(Long id){
        this(id, System.currentTimeMillis());
    }
}
