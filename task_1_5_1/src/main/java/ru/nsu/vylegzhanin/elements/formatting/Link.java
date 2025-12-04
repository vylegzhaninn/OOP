package ru.nsu.vylegzhanin.elements.formatting;

import ru.nsu.vylegzhanin.elements.Element;

/**
 * Represents a link element in Markdown.
 */
public class Link extends Element {
    public Link(Element text) {
        super(text.toMarkdown());
    }

    @Override
    public String toMarkdown() {
        return "<" + text + ">";
    }
}
    