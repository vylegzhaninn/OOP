package ru.nsu.vylegzhanin;

import java.util.ArrayList;
import java.util.List;

import ru.nsu.vylegzhanin.elements.Element;
import ru.nsu.vylegzhanin.elements.tasksformatting.TaskItem;

public class Example {
    public static void main(String[] args) {
        //маркдаун
        MarkdownBuilder builder = new MarkdownBuilder();
        
        ArrayList<TaskItem> tasks = new ArrayList<>();
        tasks.add(new TaskItem(new Element("Первая задача"), true));
        tasks.add(new TaskItem(new Element("Вторая задача"), false));
        tasks.add(new TaskItem(new Element("Третья задача"), false));
           
        ArrayList<Element> listItems = new ArrayList<>();
        listItems.add(new Element("Элемент списка 1"));
        listItems.add(new Element("Элемент списка 2"));
        listItems.add(new Element("Элемент списка 3"));
        
        List<List<Element>> tableData = new ArrayList<>();
        
        List<Element> headers = new ArrayList<>();
        headers.add(new Element("Название"));
        headers.add(new Element("Количество"));
        headers.add(new Element("Цена"));
        tableData.add(headers);
        
        List<Element> row1 = new ArrayList<>();
        row1.add(new Element("Яблоки"));
        row1.add(new Element("5"));
        row1.add(new Element("100"));
        tableData.add(row1);
        
        List<Element> row2 = new ArrayList<>();
        row2.add(new Element("Бананы"));
        row2.add(new Element("3"));
        row2.add(new Element("75"));
        tableData.add(row2);
        
        String markdown = builder.tasklist(tasks)
            .list(listItems)
            .table(tableData)
            .build();
        
        System.out.println(markdown);

        //иквалс
        Element e1 = new Element("Текст");
        Element e2 = new Element("Текст");
        Element e3 = new Element("Другой текст");
        
        System.out.println("Element(\"Текст\").equals(Element(\"Текст\")): " + e1.equals(e2));
        System.out.println("Element(\"Текст\").equals(Element(\"Другой текст\")): " + e1.equals(e3));
    }
}
