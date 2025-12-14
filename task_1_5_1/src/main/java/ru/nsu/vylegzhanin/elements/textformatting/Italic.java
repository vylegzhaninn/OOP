package ru.nsu.vylegzhanin.elements.textformatting;

import static java.util.Objects.requireNonNull;

import ru.nsu.vylegzhanin.elements.Element;

/**
 * Represents italic text element in Markdown.
 */
public class Italic extends Element {

    public Italic(Element text) {
        super(requireNonNull(text).toMarkdown());
    }

    @Override
    public String toMarkdown() {
        return "*" + text + "*";
    }
}
