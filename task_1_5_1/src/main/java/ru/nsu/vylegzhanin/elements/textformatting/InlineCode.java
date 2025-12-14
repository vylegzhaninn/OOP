package ru.nsu.vylegzhanin.elements.textformatting;

import static java.util.Objects.requireNonNull;

import ru.nsu.vylegzhanin.elements.Element;

/**
 * Represents inline code element in Markdown.
 */
public class InlineCode extends Element {

    public InlineCode(Element text) {
        super(requireNonNull(text).toMarkdown());
    }

    @Override
    public String toMarkdown() {
        return "`" + text + "`";
    }
}
