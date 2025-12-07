package ru.nsu.vylegzhanin.elements.formatting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.vylegzhanin.elements.Text;

class LinkTest {

    @Test
    void toMarkdown_wrapsInAngleBrackets() {
        Link link = new Link(new Text("example.com"));
        assertEquals("<example.com>", link.toMarkdown());
    }
}
