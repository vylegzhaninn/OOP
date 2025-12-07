package ru.nsu.vylegzhanin.elements.tableformatting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import ru.nsu.vylegzhanin.elements.Element;
import ru.nsu.vylegzhanin.elements.Text;

class TableTest {
    
    @Test
    void testToMarkdown() {
        List<List<Element>> rows = new ArrayList<>();
        
        List<Element> header = new ArrayList<>();
        header.add(new Text("A"));
        header.add(new Text("B"));
        rows.add(header);
        
        List<Element> row1 = new ArrayList<>();
        row1.add(new Text("1"));
        row1.add(new Text("2"));
        rows.add(row1);
        
        Table table = new Table(rows);
        String result = table.toMarkdown();
        
        assertTrue(result.contains("| A | B |"));
        assertTrue(result.contains("| --- | --- |"));
        assertTrue(result.contains("| 1 | 2 |"));
    }
    
    @Test
    void testEquals_SameRows() {
        List<List<Element>> rows1 = new ArrayList<>();
        List<Element> header1 = new ArrayList<>();
        header1.add(new Text("A"));
        rows1.add(header1);
        
        List<List<Element>> rows2 = new ArrayList<>();
        List<Element> header2 = new ArrayList<>();
        header2.add(new Text("A"));
        rows2.add(header2);
        
        Table t1 = new Table(rows1);
        Table t2 = new Table(rows2);
        assertEquals(t1, t2);
    }
}
