package ru.nsu.vylegzhanin.elements.textformatting;

import ru.nsu.vylegzhanin.elements.Element;

public class Bold extends Element {
    public Bold(Element text) {
        super(text.toMarkdown());
    }

    @Override
    public String toMarkdown() {
        return "**" + text + "**";
    }
}
