package ru.nsu.vylegzhanin.elements.formatting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import ru.nsu.vylegzhanin.elements.Text;

class BlockquotesTest {
    
    @Test
    void testToMarkdown_Level1() {
        Blockquotes quote = new Blockquotes(new Text("Quote"), 1);
        assertEquals("> Quote", quote.toMarkdown());
    }
    
    @Test
    void testToMarkdown_Level3() {
        Blockquotes quote = new Blockquotes(new Text("Nested quote"), 3);
        assertEquals(">>> Nested quote", quote.toMarkdown());
    }

    @Test
    void testToMarkdown_InvalidLevelThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Blockquotes(new Text("No prefix"), 0));
    }
    
    @Test
    void testEquals_SameTextAndLevel() {
        Blockquotes q1 = new Blockquotes(new Text("Quote"), 2);
        Blockquotes q2 = new Blockquotes(new Text("Quote"), 2);
        assertEquals(q1, q2);
    }
    
    @Test
    void testEquals_DifferentLevel() {
        Blockquotes q1 = new Blockquotes(new Text("Quote"), 1);
        Blockquotes q2 = new Blockquotes(new Text("Quote"), 2);
        assertNotEquals(q1, q2);
    }

    @Test
    void testEquals_DifferentText() {
        Blockquotes q1 = new Blockquotes(new Text("One"), 1);
        Blockquotes q2 = new Blockquotes(new Text("Two"), 1);
        assertNotEquals(q1, q2);
    }

    @Test
    void testHashCode_MatchesForEqualObjects() {
        Blockquotes q1 = new Blockquotes(new Text("Quote"), 2);
        Blockquotes q2 = new Blockquotes(new Text("Quote"), 2);
        assertEquals(q1.hashCode(), q2.hashCode());
    }
}
