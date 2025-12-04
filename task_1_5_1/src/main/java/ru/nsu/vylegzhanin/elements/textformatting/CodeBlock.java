package ru.nsu.vylegzhanin.elements.textformatting;

import ru.nsu.vylegzhanin.elements.Element;

public class CodeBlock extends Element {
    public CodeBlock(Element text) {
        super(text.toMarkdown());
    }

    @Override
    public String toMarkdown() {
        return "```\n" + text + "\n```";
    }
}
