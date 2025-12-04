package ru.nsu.vylegzhanin.elements.textformatting;

import ru.nsu.vylegzhanin.elements.Element;

/**
 * Represents a code block element in Markdown.
 */
public class CodeBlock extends Element {
    public CodeBlock(Element text) {
        super(text.toMarkdown());
    }

    @Override
    public String toMarkdown() {
        return "```\n" + text + "\n```";
    }
}
