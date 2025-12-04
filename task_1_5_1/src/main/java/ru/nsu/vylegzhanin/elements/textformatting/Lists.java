package ru.nsu.vylegzhanin.elements.textformatting;

import java.util.ArrayList;
import java.util.Objects;
import ru.nsu.vylegzhanin.elements.Element;

/**
 * Represents an unordered list in Markdown.
 */
public class Lists extends Element {
    private final ArrayList<Element> items;

    public Lists(ArrayList<Element> items) {
        super(null);
        this.items = items;
    }

    /**
     * Converts the list to Markdown format.
     *
     * @return Markdown representation of the list
     */
    public String toMarkdown() {
        StringBuilder sb = new StringBuilder();
        for (Element item : items) {
            sb.append("- ").append(item.toMarkdown()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Lists)) {
            return false;
        }
        Lists lists = (Lists) obj;
        return Objects.equals(items, lists.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }
}
