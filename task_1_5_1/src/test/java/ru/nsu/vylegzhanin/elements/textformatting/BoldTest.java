package ru.nsu.vylegzhanin.elements.textformatting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.vylegzhanin.elements.Text;

class BoldTest {
    
    @Test
    void testToMarkdown() {
        Bold bold = new Bold(new Text("text"));
        assertEquals("**text**", bold.toMarkdown());
    }
    
    @Test
    void testEquals_SameText() {
        Bold b1 = new Bold(new Text("text"));
        Bold b2 = new Bold(new Text("text"));
        assertEquals(b1, b2);
    }
}
