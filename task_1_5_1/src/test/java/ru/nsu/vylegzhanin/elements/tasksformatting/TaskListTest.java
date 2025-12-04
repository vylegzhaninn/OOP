package ru.nsu.vylegzhanin.elements.tasksformatting;

import org.junit.jupiter.api.Test;
import ru.nsu.vylegzhanin.elements.Element;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class TaskListTest {
    
    @Test
    void testToMarkdown() {
        ArrayList<TaskItem> tasks = new ArrayList<>();
        tasks.add(new TaskItem(new Element("Task 1"), true));
        tasks.add(new TaskItem(new Element("Task 2"), false));
        
        TaskList taskList = new TaskList(tasks);
        String expected = "- [x] Task 1\n- [ ] Task 2\n";
        assertEquals(expected, taskList.toMarkdown());
    }
    
    @Test
    void testEquals_SameTasks() {
        ArrayList<TaskItem> tasks1 = new ArrayList<>();
        tasks1.add(new TaskItem(new Element("Task"), true));
        
        ArrayList<TaskItem> tasks2 = new ArrayList<>();
        tasks2.add(new TaskItem(new Element("Task"), true));
        
        TaskList tl1 = new TaskList(tasks1);
        TaskList tl2 = new TaskList(tasks2);
        assertEquals(tl1, tl2);
    }
}
