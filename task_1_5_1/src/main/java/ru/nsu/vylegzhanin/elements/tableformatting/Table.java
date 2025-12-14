package ru.nsu.vylegzhanin.elements.tableformatting;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;
import ru.nsu.vylegzhanin.elements.Element;

/**
 * Represents a table in Markdown.
 */
public class Table extends Element {
    private final List<List<Element>> rows;
    private final List<Alignment> alignments;

    /**
     * Column alignment options for a table.
     */
    public enum Alignment {
        LEFT, CENTER, RIGHT
    }

    public Table(List<List<Element>> rows) {
        this(rows, null);
    }

    /**
     * Creates a table with optional column alignments. If {@code alignments} is null
     * or shorter than the number of header columns, missing alignments default to LEFT.
     *
     * @param rows table rows (first row is header)
     * @param alignments optional per-column alignments
     */
    public Table(List<List<Element>> rows, List<Alignment> alignments) {
        super(null);
        this.rows = requireNonNull(rows);
        this.alignments = alignments;
    }

    @Override
    public String toMarkdown() {
        StringBuilder sb = new StringBuilder();

        if (rows == null || rows.isEmpty()) {
            return "";
        }

        List<Element> header = rows.get(0);
        sb.append("| ");
        for (int j = 0; j < header.size(); j++) {
            if (j > 0) {
                sb.append(" | ");
            }
            sb.append(header.get(j).toMarkdown());
        }
        sb.append(" |\n");

        sb.append("| ");
        for (int j = 0; j < header.size(); j++) {
            if (j > 0) {
                sb.append(" | ");
            }
            Alignment a = null;
            if (alignments != null && j < alignments.size()) {
                a = alignments.get(j);
            }
            switch (a == null ? Alignment.LEFT : a) {
                case CENTER:
                    sb.append(":---:");
                    break;
                case RIGHT:
                    sb.append("---:");
                    break;
                default:
                    sb.append("---");
            }
        }
        sb.append(" |\n");

        for (int i = 1; i < rows.size(); i++) {
            List<Element> row = rows.get(i);
            sb.append("| ");
            for (int j = 0; j < header.size(); j++) {
                if (j > 0) {
                    sb.append(" | ");
                }
                if (j < row.size()) {
                    sb.append(row.get(j).toMarkdown());
                } else {
                    sb.append("");
                }
            }
            sb.append(" |\n");
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Table)) {
            return false;
        }
        Table table = (Table) obj;
        return Objects.equals(rows, table.rows) && Objects.equals(alignments, table.alignments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rows, alignments);
    }
}
