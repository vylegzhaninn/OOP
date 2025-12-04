package ru.nsu.vylegzhanin.elements.formatting;

import org.junit.jupiter.api.Test;
import ru.nsu.vylegzhanin.elements.Element;
import static org.junit.jupiter.api.Assertions.*;

class HeaderTest {
    
    @Test
    void testToMarkdown_Level1() {
        Header header = new Header(new Element("Title"), 1);
        assertEquals("# Title", header.toMarkdown());
    }
    
    @Test
    void testToMarkdown_Level3() {
        Header header = new Header(new Element("Subtitle"), 3);
        assertEquals("### Subtitle", header.toMarkdown());
    }
    
    @Test
    void testEquals_SameTextAndLevel() {
        Header h1 = new Header(new Element("Title"), 2);
        Header h2 = new Header(new Element("Title"), 2);
        assertEquals(h1, h2);
    }
    
    @Test
    void testEquals_DifferentLevel() {
        Header h1 = new Header(new Element("Title"), 1);
        Header h2 = new Header(new Element("Title"), 2);
        assertNotEquals(h1, h2);
    }
    
    @Test
    void testEquals_DifferentText() {
        Header h1 = new Header(new Element("Title1"), 1);
        Header h2 = new Header(new Element("Title2"), 1);
        assertNotEquals(h1, h2);
    }
    
    @Test
    void testHashCode_SameTextAndLevel() {
        Header h1 = new Header(new Element("Title"), 2);
        Header h2 = new Header(new Element("Title"), 2);
        assertEquals(h1.hashCode(), h2.hashCode());
    }
}
