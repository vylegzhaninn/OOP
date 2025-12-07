package ru.nsu.vylegzhanin.elements.formatting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.vylegzhanin.elements.Text;

class HeaderTest {
    
    @Test
    void testToMarkdown_Level1() {
        Header header = new Header(new Text("Title"), 1);
        assertEquals("# Title", header.toMarkdown());
    }
    
    @Test
    void testToMarkdown_Level3() {
        Header header = new Header(new Text("Subtitle"), 3);
        assertEquals("### Subtitle", header.toMarkdown());
    }
    
    @Test
    void testEquals_SameTextAndLevel() {
        Header h1 = new Header(new Text("Title"), 2);
        Header h2 = new Header(new Text("Title"), 2);
        assertEquals(h1, h2);
    }
    
    @Test
    void testEquals_DifferentLevel() {
        Header h1 = new Header(new Text("Title"), 1);
        Header h2 = new Header(new Text("Title"), 2);
        assertNotEquals(h1, h2);
    }
    
    @Test
    void testEquals_DifferentText() {
        Header h1 = new Header(new Text("Title1"), 1);
        Header h2 = new Header(new Text("Title2"), 1);
        assertNotEquals(h1, h2);
    }
    
    @Test
    void testHashCode_SameTextAndLevel() {
        Header h1 = new Header(new Text("Title"), 2);
        Header h2 = new Header(new Text("Title"), 2);
        assertEquals(h1.hashCode(), h2.hashCode());
    }
}
