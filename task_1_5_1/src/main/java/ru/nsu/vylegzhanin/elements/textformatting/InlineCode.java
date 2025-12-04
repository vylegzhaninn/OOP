package ru.nsu.vylegzhanin.elements.textformatting;

import ru.nsu.vylegzhanin.elements.Element;

public class InlineCode extends Element {

    public InlineCode(Element text) {
        super(text.toMarkdown());
    }

    @Override
    public String toMarkdown() {
        return "`" + text + "`";
    }
}
