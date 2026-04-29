package vylegzhanin.task241.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import vylegzhanin.task241.domain.GradeBound;

class GradeServiceTest {
    private final GradeService service = new GradeService();
    private final List<GradeBound> bounds = List.of(
        new GradeBound("A", 85),
        new GradeBound("B", 70),
        new GradeBound("C", 55),
        new GradeBound("D", 40),
        new GradeBound("F", 0)
    );

    @Test
    void exactBoundaryReturnsCorrectGrade() {
        assertEquals("B", service.resolve(70, 100, bounds));
    }

    @Test
    void bonusPointsAbove100Percent() {
        assertEquals("A", service.resolve(110, 100, bounds));
    }

    @Test
    void emptyBoundsReturnsNA() {
        assertEquals("N/A", service.resolve(50, 100, List.of()));
    }

    @Test
    void zeroMaxPointsReturnsNA() {
        assertEquals("N/A", service.resolve(10, 0, bounds));
    }

    @Test
    void unsortedBoundsStillReturnsCorrectGrade() {
        List<GradeBound> unsorted = List.of(
            new GradeBound("F", 0),
            new GradeBound("D", 40),
            new GradeBound("C", 55),
            new GradeBound("B", 70),
            new GradeBound("A", 85)
        );
        assertEquals("C", service.resolve(60, 100, unsorted));
    }
}
