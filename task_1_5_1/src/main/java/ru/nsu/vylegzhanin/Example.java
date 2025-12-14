package ru.nsu.vylegzhanin;

import java.util.ArrayList;
import java.util.List;
import ru.nsu.vylegzhanin.elements.tableformatting.Table;
import ru.nsu.vylegzhanin.elements.Element;
import ru.nsu.vylegzhanin.elements.Text;
import ru.nsu.vylegzhanin.elements.tasksformatting.TaskItem;

/**
 * Example class demonstrating the usage of MarkdownBuilder.
 */
public class Example {
    /**
     * Main method to demonstrate Markdown generation.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        //маркдаун
        ArrayList<TaskItem> tasks = new ArrayList<>();
        tasks.add(new TaskItem(new Text("Первая задача"), true));
        tasks.add(new TaskItem(new Text("Вторая задача"), false));
        tasks.add(new TaskItem(new Text("Третья задача"), false));
           
        ArrayList<Element> listItems = new ArrayList<>();
        listItems.add(new Text("Элемент списка 1"));
        listItems.add(new Text("Элемент списка 2"));
        listItems.add(new Text("Элемент списка 3"));
        
        final List<List<Element>> tableData = new ArrayList<>();
        
        List<Element> headers = new ArrayList<>();
        headers.add(new Text("Название"));
        headers.add(new Text("Количество"));
        headers.add(new Text("Цена"));
        tableData.add(headers);
        
        List<Element> row1 = new ArrayList<>();
        row1.add(new Text("Яблоки"));
        row1.add(new Text("5"));
        row1.add(new Text("100"));
        tableData.add(row1);
        
        List<Element> row2 = new ArrayList<>();
        row2.add(new Text("Бананы"));
        row2.add(new Text("3"));
        row2.add(new Text("75"));
        tableData.add(row2);
        
        MarkdownBuilder builder = new MarkdownBuilder();
        String markdown = builder.tasklist(tasks)
            .list(listItems)
            .table(tableData, Table.Alignment.LEFT, Table.Alignment.CENTER, Table.Alignment.RIGHT)
            .build();
        
        System.out.println(markdown);

        //иквалс
        Element e1 = new Text("Текст");
        Element e2 = new Text("Текст");
        Element e3 = new Text("Другой текст");
        
        System.out.println("Element(\"Текст\").equals(Element(\"Текст\")): " 
            + e1.equals(e2));
        System.out.println("Element(\"Текст\").equals(Element(\"Другой текст\")): " 
            + e1.equals(e3));
    }
}
