package ru.nsu.vylegzhanin.elements.textformatting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.vylegzhanin.elements.Text;

class CodeBlockTest {

    @Test
    void toMarkdown_wrapsWithFenceAndNewlines() {
        CodeBlock codeBlock = new CodeBlock(new Text("line1"));
        assertEquals("```\nline1\n```", codeBlock.toMarkdown());
    }
}
