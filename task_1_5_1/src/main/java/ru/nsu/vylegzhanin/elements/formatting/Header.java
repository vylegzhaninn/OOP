package ru.nsu.vylegzhanin.elements.formatting;

import java.util.Objects;
import ru.nsu.vylegzhanin.elements.Element;

/**
 * Represents a header element in Markdown.
 */
public class Header extends Element {
    private final int level;

    /**
     * Creates a header element.
     *
     * @param text the header content
     * @param level the header level in [1,6]
     * @throws IllegalArgumentException if {@code level} is outside [1,6]
     */
    public Header(Element text, int level) {
        super(text.toMarkdown());
        if (level < 1 || level > 6) {
            throw new IllegalArgumentException("level must be in [1,6]");
        }
        this.level = level;
    }

    @Override
    public String toMarkdown() {
        return "#".repeat(level) + " " + text;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Header)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        Header header = (Header) obj;
        return level == header.level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), level);
    }
}
