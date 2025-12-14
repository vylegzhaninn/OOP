package ru.nsu.vylegzhanin.elements.formatting;

import java.util.Objects;
import ru.nsu.vylegzhanin.elements.Element;

/**
 * Represents a link element in Markdown.
 */
public class Link extends Element {
    public Link(Element text) {
        super(Objects.requireNonNull(text).toMarkdown());
    }
    
    @Override
    public String toMarkdown() {
        return "<" + text + ">";
    }
}
    