package ru.nsu.vylegzhanin.elements.textformatting;

import org.junit.jupiter.api.Test;
import ru.nsu.vylegzhanin.elements.Element;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class ListsTest {
    
    @Test
    void testToMarkdown() {
        ArrayList<Element> items = new ArrayList<>();
        items.add(new Element("Item 1"));
        items.add(new Element("Item 2"));
        items.add(new Element("Item 3"));
        
        Lists list = new Lists(items);
        String expected = "- Item 1\n- Item 2\n- Item 3\n";
        assertEquals(expected, list.toMarkdown());
    }
    
    @Test
    void testEquals_SameItems() {
        ArrayList<Element> items1 = new ArrayList<>();
        items1.add(new Element("Item"));
        
        ArrayList<Element> items2 = new ArrayList<>();
        items2.add(new Element("Item"));
        
        Lists l1 = new Lists(items1);
        Lists l2 = new Lists(items2);
        assertEquals(l1, l2);
    }
}
