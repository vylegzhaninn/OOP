package ru.nsu.vylegzhanin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.vylegzhanin.elements.Element;

class ElementTest {
    
    @Test
    void testToMarkdown() {
        Element element = new Element("Simple text");
        assertEquals("Simple text", element.toMarkdown());
    }
    
    @Test
    void testEquals_SameText() {
        Element e1 = new Element("text");
        Element e2 = new Element("text");
        assertEquals(e1, e2);
    }
    
    @Test
    void testEquals_DifferentText() {
        Element e1 = new Element("text1");
        Element e2 = new Element("text2");
        assertNotEquals(e1, e2);
    }
    
    @Test
    void testEquals_Null() {
        Element e1 = new Element("text");
        assertNotEquals(e1, null);
    }
    
    @Test
    void testHashCode_SameText() {
        Element e1 = new Element("text");
        Element e2 = new Element("text");
        assertEquals(e1.hashCode(), e2.hashCode());
    }
}
