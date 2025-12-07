package ru.nsu.vylegzhanin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import ru.nsu.vylegzhanin.elements.Element;
import ru.nsu.vylegzhanin.elements.Text;
import ru.nsu.vylegzhanin.elements.tasksformatting.TaskItem;

class MarkdownBuilderTest {
    
    @Test
    void testBuild_Empty() {
        MarkdownBuilder builder = new MarkdownBuilder();
        assertEquals("", builder.build());
    }
    
    @Test
    void testBuild_SingleElement() {
        MarkdownBuilder builder = new MarkdownBuilder();
        builder.addelement(new Text("Test"));
        assertEquals("Test\n\n", builder.build());
    }
    
    @Test
    void testBuild_MultipleElements() {
        MarkdownBuilder builder = new MarkdownBuilder();
        builder.addelement(new Text("First"))
            .addelement(new Text("Second"));
        assertEquals("First\n\nSecond\n\n", builder.build());
    }
    
    @Test
    void testBold() {
        MarkdownBuilder builder = new MarkdownBuilder();
        builder.bold(new Text("bold"));
        assertTrue(builder.build().contains("**bold**"));
    }
    
    @Test
    void testItalic() {
        MarkdownBuilder builder = new MarkdownBuilder();
        builder.italic(new Text("italic"));
        assertTrue(builder.build().contains("*italic*"));
    }
    
    @Test
    void testHeader() {
        MarkdownBuilder builder = new MarkdownBuilder();
        builder.header(new Text("Title"), 2);
        assertEquals("## Title\n\n", builder.build());
    }

    @Test
    void testBuild_WithVariousElements() {
        ArrayList<Element> listItems = new ArrayList<>();
        listItems.add(new Text("one"));
        listItems.add(new Text("two"));

        ArrayList<TaskItem> tasks = new ArrayList<>();
        tasks.add(new TaskItem(new Text("do"), true));
        tasks.add(new TaskItem(new Text("wait"), false));

        List<List<Element>> table = new ArrayList<>();
        List<Element> headerRow = new ArrayList<>();
        headerRow.add(new Text("H1"));
        headerRow.add(new Text("H2"));
        table.add(headerRow);

        List<Element> dataRow = new ArrayList<>();
        dataRow.add(new Text("A"));
        dataRow.add(new Text("B"));
        table.add(dataRow);

        MarkdownBuilder builder = new MarkdownBuilder();
        String markdown = builder
            .blockquotes(new Text("Quote"), 2)
            .link(new Text("https://example.com"))
            .image(new Text("alt"), "url")
            .inlinecode(new Text("code"))
            .codeblock(new Text("block"))
            .strike(new Text("strike"))
            .list(listItems)
            .tasklist(tasks)
            .table(table)
            .build();

        String expected = ">> Quote\n\n"
            + "<https://example.com>\n\n"
            + "![alt](url)\n\n"
            + "`code`\n\n"
            + "```\nblock\n```\n\n"
            + "~~strike~~\n\n"
            + "- one\n- two\n\n\n"
            + "- [x] do\n- [ ] wait\n\n\n"
            + "| H1 | H2 |\n| --- | --- |\n| A | B |\n\n\n";

        assertEquals(expected, markdown);
    }
}
