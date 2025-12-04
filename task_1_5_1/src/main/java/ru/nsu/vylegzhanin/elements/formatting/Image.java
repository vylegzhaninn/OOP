package ru.nsu.vylegzhanin.elements.formatting;

import java.util.Objects;
import ru.nsu.vylegzhanin.elements.Element;

/**
 * Represents an image element in Markdown.
 */
public class Image extends Element {
    private final String url;

    public Image(Element text, String url) {
        super(text.toMarkdown());
        this.url = url;
    }

    @Override
    public String toMarkdown() {
        return "![" + text + "](" + url + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Image)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        Image image = (Image) obj;
        return Objects.equals(url, image.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), url);
    }
}
