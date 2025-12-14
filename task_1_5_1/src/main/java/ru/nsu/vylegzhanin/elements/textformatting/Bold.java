package ru.nsu.vylegzhanin.elements.textformatting;

import static java.util.Objects.requireNonNull;

import ru.nsu.vylegzhanin.elements.Element;

/**
 * Represents bold text element in Markdown.
 */
public class Bold extends Element {
    public Bold(Element text) {
        super(requireNonNull(text).toMarkdown());
    }

    @Override
    public String toMarkdown() {
        return "**" + text + "**";
    }
}
