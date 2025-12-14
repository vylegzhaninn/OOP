package ru.nsu.vylegzhanin.elements.tableformatting;

import java.util.List;
import java.util.Objects;
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
            sb.append("---");
        }
        sb.append(" |\n");

        for (int i = 1; i < rows.size(); i++) {
            List<Element> row = rows.get(i);
            sb.append("| ");
            for (int j = 0; j < row.size(); j++) {
                if (j > 0) {
                    sb.append(" | ");
                }
                sb.append(row.get(j).toMarkdown());
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
        return Objects.equals(rows, table.rows);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rows);
    }
}
