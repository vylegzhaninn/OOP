package ru.nsu.vylegzhanin;

import java.util.ArrayList;
import java.util.List;
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
     * @return this builder for method chaining
    * @throws NullPointerException if {@code element} is null
     */
    public MarkdownBuilder addelement(Element element) {
        elements.add(element);
        return this;
    }

    /**
     * Adds a blockquote element.
     *
     * @param element the content to quote
     * @param level the nesting level of the blockquote
    * @return this builder for method chaining
     * @throws NullPointerException if {@code element} is null
     * @throws IllegalArgumentException if {@code level} is outside [1,6]
     */
    public MarkdownBuilder blockquotes(Element element, int level) {
        elements.add(new Blockquotes(element, level));
        return this;
    }

    /**
     * Adds a header element.
     *
     * @param element the header text
     * @param level the header level (1-6)
    * @return this builder for method chaining
     * @throws NullPointerException if {@code element} is null
     * @throws IllegalArgumentException if {@code level} is outside [1,6]
     */
    public MarkdownBuilder header(Element element, int level) {
        elements.add(new Header(element, level));
        return this;
    }

    /**
     * Adds an image element.
     *
     * @param element the alt text for the image
     * @param url the image URL
     * @return this builder for method chaining
    * @throws NullPointerException if {@code element} is null
     */
    public MarkdownBuilder image(Element element, String url) {
        elements.add(new Image(element, url));
        return this;
    }

    /**
     * Adds a link element.
     *
     * @param element the link text
     * @return this builder for method chaining
    * @throws NullPointerException if {@code element} is null
     */
    public MarkdownBuilder link(Element element) {
        elements.add(new Link(element));
        return this;
    }

    /**
     * Adds a table element.
     *
     * @param element the table rows (first row is header)
     * @return this builder for method chaining
    * @throws NullPointerException if {@code element} is null
     */
    public MarkdownBuilder table(List<List<Element>> element) {
        elements.add(new Table(element));
        return this;
    }

    /**
     * Adds a table element with column alignments.
     *
     * @param element the table rows (first row is header)
     * @param alignments per-column alignments (may be null or shorter than columns)
     * @return this builder for method chaining
     */
    public MarkdownBuilder table(List<List<Element>> element, List<Table.Alignment> alignments) {
        elements.add(new Table(element, alignments));
        return this;
    }

    /**
     * Adds a table element with column alignments specified as varargs.
     *
     * @param element the table rows (first row is header)
     * @param alignments per-column alignments as varargs
     * @return this builder for method chaining
     */
    public MarkdownBuilder table(List<List<Element>> element, Table.Alignment... alignments) {
        List<Table.Alignment> list = null;
        if (alignments != null) {
            list = new ArrayList<>();
            for (Table.Alignment a : alignments) {
                list.add(a);
            }
        }
        elements.add(new Table(element, list));
        return this;
    }

    /**
     * Adds a task list element.
     *
     * @param items the list of task items
     * @return this builder for method chaining
    * @throws NullPointerException if {@code items} is null
     */
    public MarkdownBuilder tasklist(ArrayList<TaskItem> items) {
        elements.add(new TaskList(items));
        return this;
    }

    /**
     * Adds a bold text element.
     *
     * @param element the text to make bold
     * @return this builder for method chaining
    * @throws NullPointerException if {@code element} is null
     */
    public MarkdownBuilder bold(Element element) {
        elements.add(new Bold(element));
        return this;
    }

    /**
     * Adds a code block element.
     *
     * @param element the code content
     * @return this builder for method chaining
    * @throws NullPointerException if {@code element} is null
     */
    public MarkdownBuilder codeblock(Element element) {
        elements.add(new CodeBlock(element));
        return this;
    }

    /**
     * Adds an inline code element.
     *
     * @param element the code content
     * @return this builder for method chaining
    * @throws NullPointerException if {@code element} is null
     */
    public MarkdownBuilder inlinecode(Element element) {
        elements.add(new InlineCode(element));
        return this;
    }

    /**
     * Adds an italic text element.
     *
     * @param element the text to make italic
     * @return this builder for method chaining
    * @throws NullPointerException if {@code element} is null
     */
    public MarkdownBuilder italic(Element element) {
        elements.add(new Italic(element));
        return this;
    }

    /**
     * Adds a strikethrough text element.
     *
     * @param element the text to strike through
     * @return this builder for method chaining
    * @throws NullPointerException if {@code element} is null
     */
    public MarkdownBuilder strike(Element element) {
        elements.add(new Strike(element));
        return this;
    }

    /**
     * Adds an unordered list element.
     *
     * @param items the list items
     * @return this builder for method chaining
    * @throws NullPointerException if {@code items} is null
     */
    public MarkdownBuilder list(ArrayList<Element> items) {
        elements.add(new Lists(items));
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
