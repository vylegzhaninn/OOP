package ru.nsu.vylegzhanin.elements.tasksformatting;

import java.util.Objects;

import ru.nsu.vylegzhanin.elements.Element;

public class TaskItem extends Element {

    private final boolean done;

    public TaskItem(Element text, boolean done) {
        super(text.toMarkdown());
        this.done = done;
    }

    @Override
    public String toMarkdown() {
        return done ? "[x] " + text : "[ ] " + text;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TaskItem)) return false;
        if (!super.equals(obj)) return false;
        TaskItem taskItem = (TaskItem) obj;
        return done == taskItem.done;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), done);
    }
}
