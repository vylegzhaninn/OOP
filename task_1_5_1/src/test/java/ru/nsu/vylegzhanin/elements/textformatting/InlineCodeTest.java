package ru.nsu.vylegzhanin.elements.textformatting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.vylegzhanin.elements.Text;

class InlineCodeTest {

    @Test
    void toMarkdown_wrapsWithBackticks() {
        InlineCode inlineCode = new InlineCode(new Text("code"));
        assertEquals("`code`", inlineCode.toMarkdown());
    }
}
