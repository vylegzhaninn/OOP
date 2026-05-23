package worker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkerTest {

    @Test
    void isComposite_returnsFalseForNumbersBelow4() {
        assertFalse(Worker.isComposite(-1));
        assertFalse(Worker.isComposite(0));
        assertFalse(Worker.isComposite(1));
        assertFalse(Worker.isComposite(2));
        assertFalse(Worker.isComposite(3));
    }

    @Test
    void isComposite_returnsTrueForSmallComposites() {
        assertTrue(Worker.isComposite(4));
        assertTrue(Worker.isComposite(6));
        assertTrue(Worker.isComposite(8));
        assertTrue(Worker.isComposite(9));
        assertTrue(Worker.isComposite(15));
    }

    @Test
    void isComposite_returnsFalseForPrimes() {
        assertFalse(Worker.isComposite(5));
        assertFalse(Worker.isComposite(7));
        assertFalse(Worker.isComposite(11));
        assertFalse(Worker.isComposite(13));
        assertFalse(Worker.isComposite(20319251));
        assertFalse(Worker.isComposite(6997901));
    }

    @Test
    void isComposite_handlesLargeComposites() {
        assertTrue(Worker.isComposite(20319252));
        assertTrue(Worker.isComposite(1_000_000));
    }
}
