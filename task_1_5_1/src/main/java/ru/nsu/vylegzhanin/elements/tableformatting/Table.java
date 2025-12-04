package ru.nsu.vylegzhanin.elements.tableformatting;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import ru.nsu.vylegzhanin.elements.Element;

/**
 * Represents a table in Markdown.
 */
public class Table extends Element {
    private final List<List<Element>> rows;

    public Table(List<List<Element>> rows) {
        super(null);
        this.rows = rows;
    }

    @Override
    public String toMarkdown() {
        StringBuilder sb = new StringBuilder();

        sb.append("| ")
            .append(rows.get(0).stream()
                .map(Element::toMarkdown)
                .collect(Collectors.joining(" | ")))
            .append(" |\n");

        sb.append("| ")
            .append(rows.get(0).stream()
                .map(e -> "---")
                .collect(Collectors.joining(" | ")))
            .append(" |\n");

        for (int i = 1; i < rows.size(); i++) {
            sb.append("| ")
                .append(rows.get(i).stream()
                    .map(Element::toMarkdown)
                    .collect(Collectors.joining(" | ")))
                .append(" |\n");
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Table)) {
            return false;
        }
        Table table = (Table) obj;
        return Objects.equals(rows, table.rows);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rows);
    }
}
