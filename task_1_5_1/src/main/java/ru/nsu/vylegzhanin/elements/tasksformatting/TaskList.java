package ru.nsu.vylegzhanin.elements.tasksformatting;

import java.util.ArrayList;
import java.util.Objects;
import ru.nsu.vylegzhanin.elements.Element;

/**
 * Represents a task list in Markdown.
 */
public class TaskList extends Element {

    private final ArrayList<TaskItem> items;

    public TaskList(ArrayList<TaskItem> items) {
        super(null);
        this.items = items;
    }

    @Override
    public String toMarkdown() {
        StringBuilder sb = new StringBuilder();
        for (TaskItem item : items) {
            sb.append("- ").append(item.toMarkdown()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TaskList)) {
            return false;
        }
        TaskList taskList = (TaskList) obj;
        return Objects.equals(items, taskList.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }
}
