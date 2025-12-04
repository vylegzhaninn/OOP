package ru.nsu.vylegzhanin.elements.textformatting;

import org.junit.jupiter.api.Test;
import ru.nsu.vylegzhanin.elements.Element;
import static org.junit.jupiter.api.Assertions.*;

class BoldTest {
    
    @Test
    void testToMarkdown() {
        Bold bold = new Bold(new Element("text"));
        assertEquals("**text**", bold.toMarkdown());
    }
    
    @Test
    void testEquals_SameText() {
        Bold b1 = new Bold(new Element("text"));
        Bold b2 = new Bold(new Element("text"));
        assertEquals(b1, b2);
    }
}
