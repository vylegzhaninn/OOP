package ru.nsu.vylegzhanin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.vylegzhanin.elements.Element;

class MarkdownBuilderTest {
    
    @Test
    void testBuild_Empty() {
        MarkdownBuilder builder = new MarkdownBuilder();
        assertEquals("", builder.build());
    }
    
    @Test
    void testBuild_SingleElement() {
        MarkdownBuilder builder = new MarkdownBuilder();
        builder.addelement(new Element("Test"));
        assertEquals("Test\n\n", builder.build());
    }
    
    @Test
    void testBuild_MultipleElements() {
        MarkdownBuilder builder = new MarkdownBuilder();
        builder.addelement(new Element("First"))
               .addelement(new Element("Second"));
        assertEquals("First\n\nSecond\n\n", builder.build());
    }
    
    @Test
    void testBold() {
        MarkdownBuilder builder = new MarkdownBuilder();
        builder.bold(new Element("bold"));
        assertTrue(builder.build().contains("**bold**"));
    }
    
    @Test
    void testItalic() {
        MarkdownBuilder builder = new MarkdownBuilder();
        builder.italic(new Element("italic"));
        assertTrue(builder.build().contains("*italic*"));
    }
    
    @Test
    void testHeader() {
        MarkdownBuilder builder = new MarkdownBuilder();
        builder.header(new Element("Title"), 2);
        assertEquals("## Title\n\n", builder.build());
    }
}
