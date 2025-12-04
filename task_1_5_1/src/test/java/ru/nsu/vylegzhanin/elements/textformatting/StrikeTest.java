package ru.nsu.vylegzhanin.elements.textformatting;

import org.junit.jupiter.api.Test;
import ru.nsu.vylegzhanin.elements.Element;
import static org.junit.jupiter.api.Assertions.*;

class StrikeTest {
    
    @Test
    void testToMarkdown() {
        Strike strike = new Strike(new Element("text"));
        assertEquals("~~text~~", strike.toMarkdown());
    }
    
    @Test
    void testEquals_SameText() {
        Strike s1 = new Strike(new Element("text"));
        Strike s2 = new Strike(new Element("text"));
        assertEquals(s1, s2);
    }
}
