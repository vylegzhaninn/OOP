
package ru.nsu.vylegzhanin.elements.formatting;

import java.util.Objects;

import ru.nsu.vylegzhanin.elements.Element;

public class Blockquotes extends Element {
    private final int level;

    public Blockquotes(Element text, int level) {
        super(text.toMarkdown());
        this.level = level;
    }

    @Override
    public String toMarkdown() {
        return ">".repeat(level) + " " + text;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Blockquotes)) return false;
        if (!super.equals(obj)) return false;
        Blockquotes header = (Blockquotes) obj;
        return level == header.level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), level);
    }
}