package vylegzhanin.pizzeria.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Order — модель заказа")
class OrderTest {

    @Test
    @DisplayName("Полный конструктор сохраняет все три поля")
    void fullConstructor_storesAllFields() {
        long time = System.currentTimeMillis();
        Order order = new Order(10L, time, 25);

        assertEquals(10L, order.id());
        assertEquals(time, order.reservationTime());
        assertEquals(25, order.size());
    }

    @Test
    @DisplayName("Сокращённый конструктор автоматически проставляет reservationTime")
    void shortConstructor_setsReservationTimeAutomatically() {
        final long before = System.currentTimeMillis();
        final Order order = new Order(5L, 30);
        final long after = System.currentTimeMillis();

        assertEquals(5L, order.id());
        assertEquals(30, order.size());
        assertNotNull(order.reservationTime());
        assertTrue(order.reservationTime() >= before && order.reservationTime() <= after,
                "reservationTime должно быть в диапазоне [before, after]");
    }

    @Test
    @DisplayName("Два заказа с одинаковыми полями равны (record equals)")
    void twoOrdersWithSameFields_areEqual() {
        long time = 123456789L;
        Order a = new Order(1L, time, 10);
        Order b = new Order(1L, time, 10);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    @DisplayName("Два заказа с разными id не равны")
    void twoOrdersWithDifferentIds_areNotEqual() {
        long time = 123456789L;
        Order a = new Order(1L, time, 10);
        Order b = new Order(2L, time, 10);

        assertNotEquals(a, b);
    }

    @Test
    @DisplayName("Два заказа с разными size не равны")
    void twoOrdersWithDifferentSizes_areNotEqual() {
        long time = 123456789L;
        Order a = new Order(1L, time, 10);
        Order b = new Order(1L, time, 99);

        assertNotEquals(a, b);
    }

    @Test
    @DisplayName("toString() содержит значения всех полей")
    void toString_containsAllFieldValues() {
        Order order = new Order(7L, 1000L, 55);
        String str = order.toString();

        assertTrue(str.contains("7"), "toString должен содержать id=7");
        assertTrue(str.contains("1000"), "toString должен содержать reservationTime=1000");
        assertTrue(str.contains("55"), "toString должен содержать size=55");
    }

    @Test
    @DisplayName("size() может быть нулём (граничный случай)")
    void size_canBeZero() {
        Order order = new Order(1L, 0);
        assertEquals(0, order.size());
    }
}
