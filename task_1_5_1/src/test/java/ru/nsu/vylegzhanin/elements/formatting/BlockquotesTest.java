package ru.nsu.vylegzhanin.elements.formatting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.vylegzhanin.elements.Element;

class BlockquotesTest {
    
    @Test
    void testToMarkdown_Level1() {
        Blockquotes quote = new Blockquotes(new Element("Quote"), 1);
        assertEquals("> Quote", quote.toMarkdown());
    }
    
    @Test
    void testToMarkdown_Level3() {
        Blockquotes quote = new Blockquotes(new Element("Nested quote"), 3);
        assertEquals(">>> Nested quote", quote.toMarkdown());
    }
    
    @Test
    void testEquals_SameTextAndLevel() {
        Blockquotes q1 = new Blockquotes(new Element("Quote"), 2);
        Blockquotes q2 = new Blockquotes(new Element("Quote"), 2);
        assertEquals(q1, q2);
    }
    
    @Test
    void testEquals_DifferentLevel() {
        Blockquotes q1 = new Blockquotes(new Element("Quote"), 1);
        Blockquotes q2 = new Blockquotes(new Element("Quote"), 2);
        assertNotEquals(q1, q2);
    }
}
