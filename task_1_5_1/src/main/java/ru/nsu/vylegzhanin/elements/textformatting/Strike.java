package ru.nsu.vylegzhanin.elements.textformatting;

import static java.util.Objects.requireNonNull;

import ru.nsu.vylegzhanin.elements.Element;

/**
 * Represents strikethrough text element in Markdown.
 */
public class Strike extends Element {
    public Strike(Element text) {
        super(requireNonNull(text).toMarkdown());
    }
    
    @Override
    public String toMarkdown() {
        return "~~" + text + "~~";
    }
}
