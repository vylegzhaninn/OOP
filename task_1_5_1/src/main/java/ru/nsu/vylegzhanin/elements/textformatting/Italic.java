package ru.nsu.vylegzhanin.elements.textformatting;

import ru.nsu.vylegzhanin.elements.Element;

/**
 * Represents italic text element in Markdown.
 */
public class Italic extends Element {

    public Italic(Element text) {
        super(text.toMarkdown());
    }

    @Override
    public String toMarkdown() {
        return "*" + text + "*";
    }
}
