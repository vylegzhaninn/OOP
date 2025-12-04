package ru.nsu.vylegzhanin.elements.textformatting;

import org.junit.jupiter.api.Test;
import ru.nsu.vylegzhanin.elements.Element;
import static org.junit.jupiter.api.Assertions.*;

class ItalicTest {
    
    @Test
    void testToMarkdown() {
        Italic italic = new Italic(new Element("text"));
        assertEquals("*text*", italic.toMarkdown());
    }
    
    @Test
    void testEquals_SameText() {
        Italic i1 = new Italic(new Element("text"));
        Italic i2 = new Italic(new Element("text"));
        assertEquals(i1, i2);
    }
}
