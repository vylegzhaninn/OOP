package ru.nsu.vylegzhanin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import ru.nsu.vylegzhanin.elements.Element;
import ru.nsu.vylegzhanin.elements.formatting.Blockquotes;
import ru.nsu.vylegzhanin.elements.formatting.Header;
import ru.nsu.vylegzhanin.elements.formatting.Image;
import ru.nsu.vylegzhanin.elements.formatting.Link;
import ru.nsu.vylegzhanin.elements.tableformatting.Table;
import ru.nsu.vylegzhanin.elements.tasksformatting.TaskItem;
import ru.nsu.vylegzhanin.elements.tasksformatting.TaskList;
import ru.nsu.vylegzhanin.elements.textformatting.Bold;
import ru.nsu.vylegzhanin.elements.textformatting.CodeBlock;
import ru.nsu.vylegzhanin.elements.textformatting.InlineCode;
import ru.nsu.vylegzhanin.elements.textformatting.Italic;
import ru.nsu.vylegzhanin.elements.textformatting.Lists;
import ru.nsu.vylegzhanin.elements.textformatting.Strike;

/**
 * Builder class for constructing Markdown documents.
 * Provides fluent API for adding various Markdown elements.
 */
public class MarkdownBuilder {
    private final List<Element> elements = new ArrayList<>();

    /**
     * Adds a custom element to the document.
     *
     * @param element the element to add
     * @throws NullPointerException if {@code element} is null
     * @return this builder for method chaining
     */
    public MarkdownBuilder addelement(Element element) {
        elements.add(Objects.requireNonNull(element));
        return this;
    }

    /**
     * Adds a blockquote element.
     *
     * @param element the content to quote
     * @param level the nesting level of the blockquote
     * @throws NullPointerException if {@code element} is null
     * @throws IllegalArgumentException if {@code level} is outside [1,6]
     * @return this builder for method chaining
     */
    public MarkdownBuilder blockquotes(Element element, int level) {
        elements.add(new Blockquotes(Objects.requireNonNull(element), level));
        return this;
    }

    /**
     * Adds a header element.
     *
     * @param element the header text
     * @param level the header level (1-6)
     * @throws NullPointerException if {@code element} is null
     * @throws IllegalArgumentException if {@code level} is outside [1,6]
     * @return this builder for method chaining
     */
    public MarkdownBuilder header(Element element, int level) {
        elements.add(new Header(Objects.requireNonNull(element), level));
        return this;
    }

    /**
     * Adds an image element.
     *
     * @param element the alt text for the image
     * @param url the image URL
     * @throws NullPointerException if {@code element} is null
     * @return this builder for method chaining
     */
    public MarkdownBuilder image(Element element, String url) {
        elements.add(new Image(Objects.requireNonNull(element), url));
        return this;
    }

    /**
     * Adds a link element.
     *
     * @param element the link text
     * @throws NullPointerException if {@code element} is null
     * @return this builder for method chaining
     */
    public MarkdownBuilder link(Element element) {
        elements.add(new Link(Objects.requireNonNull(element)));
        return this;
    }

    /**
     * Adds a table element.
     *
     * @param element the table rows (first row is header)
     * @throws NullPointerException if {@code element} is null
     * @return this builder for method chaining
     */
    public MarkdownBuilder table(List<List<Element>> element) {
        elements.add(new Table(Objects.requireNonNull(element)));
        return this;
    }

    /**
     * Adds a task list element.
     *
     * @param items the list of task items
     * @throws NullPointerException if {@code items} is null
     * @return this builder for method chaining
     */
    public MarkdownBuilder tasklist(ArrayList<TaskItem> items) {
        elements.add(new TaskList(Objects.requireNonNull(items)));
        return this;
    }

    /**
     * Adds a bold text element.
     *
     * @param element the text to make bold
     * @throws NullPointerException if {@code element} is null
     * @return this builder for method chaining
     */
    public MarkdownBuilder bold(Element element) {
        elements.add(new Bold(Objects.requireNonNull(element)));
        return this;
    }

    /**
     * Adds a code block element.
     *
     * @param element the code content
     * @throws NullPointerException if {@code element} is null
     * @return this builder for method chaining
     */
    public MarkdownBuilder codeblock(Element element) {
        elements.add(new CodeBlock(Objects.requireNonNull(element)));
        return this;
    }

    /**
     * Adds an inline code element.
     *
     * @param element the code content
     * @throws NullPointerException if {@code element} is null
     * @return this builder for method chaining
     */
    public MarkdownBuilder inlinecode(Element element) {
        elements.add(new InlineCode(Objects.requireNonNull(element)));
        return this;
    }

    /**
     * Adds an italic text element.
     *
     * @param element the text to make italic
     * @throws NullPointerException if {@code element} is null
     * @return this builder for method chaining
     */
    public MarkdownBuilder italic(Element element) {
        elements.add(new Italic(Objects.requireNonNull(element)));
        return this;
    }

    /**
     * Adds a strikethrough text element.
     *
     * @param element the text to strike through
     * @throws NullPointerException if {@code element} is null
     * @return this builder for method chaining
     */
    public MarkdownBuilder strike(Element element) {
        elements.add(new Strike(Objects.requireNonNull(element)));
        return this;
    }

    /**
     * Adds an unordered list element.
     *
     * @param items the list items
     * @throws NullPointerException if {@code items} is null
     * @return this builder for method chaining
     */
    public MarkdownBuilder list(ArrayList<Element> items) {
        elements.add(new Lists(Objects.requireNonNull(items)));
        return this;
    }

    /**
     * Builds the final Markdown document.
     *
     * @return the complete Markdown string
     */
    public String build() {
        StringBuilder markdown = new StringBuilder();
        for (Element element : elements) {
            markdown.append(element.toMarkdown()).append("\n\n");
        }
        return markdown.toString();
    }
}
