package vylegzhanin.pizzeria.supportive;

public record Order(Long id, Long reservationTime) {
    public Order(Long id){
        this(id, System.currentTimeMillis());
    }
}
