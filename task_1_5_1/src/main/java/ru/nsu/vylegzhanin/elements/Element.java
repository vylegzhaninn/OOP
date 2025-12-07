package ru.nsu.vylegzhanin.elements;

import java.util.Objects;

/**
 * Base class for all Markdown elements.
 * Contains text content and provides serialization to Markdown format.
 */
public abstract class Element {
    /** The text content of this element. */
    protected final String text;
    
    /**
     * Constructs an Element with the given text.
     *
     * @param text the text content
     */
    public Element(String text) {
        this.text = text;
    }
    
    /**
     * Converts this element to Markdown format.
     *
     * @return the Markdown representation of this element
     */
    public String toMarkdown() {
        return text;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Element)) {
            return false;
        }
        Element element = (Element) obj;
        return Objects.equals(text, element.text);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
