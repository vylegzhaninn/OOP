package ru.nsu.vylegzhanin.elements.textformatting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.vylegzhanin.elements.Text;

class ItalicTest {
    
    @Test
    void testToMarkdown() {
        Italic italic = new Italic(new Text("text"));
        assertEquals("*text*", italic.toMarkdown());
    }
    
    @Test
    void testEquals_SameText() {
        Italic i1 = new Italic(new Text("text"));
        Italic i2 = new Italic(new Text("text"));
        assertEquals(i1, i2);
    }
}
