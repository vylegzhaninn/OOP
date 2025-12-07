package ru.nsu.vylegzhanin.elements.formatting;

import java.util.Objects;
import ru.nsu.vylegzhanin.elements.Element;

/**
 * Represents a blockquote element in Markdown.
 */
public class Blockquotes extends Element {
    private final int level;

    /**
     * Creates a blockquote element with a nesting level.
     *
     * @param text the content to quote
     * @param level the quote level in [1,6]
     * @throws IllegalArgumentException if {@code level} is outside [1,6]
     */
    public Blockquotes(Element text, int level) {
        super(text.toMarkdown());
        if (level < 1 || level > 6) {
            throw new IllegalArgumentException("level must be in [1,6]");
        }
        this.level = level;
    }

    @Override
    public String toMarkdown() {
        return ">".repeat(level) + " " + text;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Blockquotes)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        Blockquotes header = (Blockquotes) obj;
        return level == header.level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), level);
    }
}