package ru.nsu.vylegzhanin.elements.tasksformatting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.vylegzhanin.elements.Text;

class TaskItemTest {
    
    @Test
    void testToMarkdown_Done() {
        TaskItem task = new TaskItem(new Text("Task"), true);
        assertEquals("[x] Task", task.toMarkdown());
    }
    
    @Test
    void testToMarkdown_NotDone() {
        TaskItem task = new TaskItem(new Text("Task"), false);
        assertEquals("[ ] Task", task.toMarkdown());
    }
    
    @Test
    void testEquals_SameTextAndStatus() {
        TaskItem t1 = new TaskItem(new Text("Task"), true);
        TaskItem t2 = new TaskItem(new Text("Task"), true);
        assertEquals(t1, t2);
    }
    
    @Test
    void testEquals_DifferentStatus() {
        TaskItem t1 = new TaskItem(new Text("Task"), true);
        TaskItem t2 = new TaskItem(new Text("Task"), false);
        assertNotEquals(t1, t2);
    }

    @Test
    void testEquals_DifferentText() {
        TaskItem t1 = new TaskItem(new Text("Task"), true);
        TaskItem t2 = new TaskItem(new Text("Other"), true);
        assertNotEquals(t1, t2);
    }

    @Test
    void testHashCode_MatchesForEqualObjects() {
        TaskItem t1 = new TaskItem(new Text("Task"), false);
        TaskItem t2 = new TaskItem(new Text("Task"), false);
        assertEquals(t1.hashCode(), t2.hashCode());
    }
}
