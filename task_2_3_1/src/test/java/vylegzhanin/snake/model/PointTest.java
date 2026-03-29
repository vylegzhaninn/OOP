package vylegzhanin.snake.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SmokeTest {

    @Test
    void pointShouldStoreCoordinatesCorrectly() {
        Point p = new Point(5, 10);
        assertEquals(5, p.x());
        assertEquals(10, p.y());
    }

    @Test
    void pointEqualityShouldWorkBasedOnCoordinates() {
        Point p1 = new Point(3, 4);
        Point p2 = new Point(3, 4);
        Point p3 = new Point(4, 3);

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
    }
}

