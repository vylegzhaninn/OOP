package ru.nsu.vylegzhanin;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ru.nsu.vylegzhanin.elements.Element;
import ru.nsu.vylegzhanin.elements.Text;
import ru.nsu.vylegzhanin.elements.tableformatting.Table;

public class ReadmeGenerator {

    public static void main(String[] args) {
        MarkdownBuilder builder = new MarkdownBuilder();

        builder.header(new Text("Markdown Builder — Учебный проект"), 1);

        builder.addelement(new Text("Лучшая библиотека на Java для создания Markdown-документов программно."));
        builder.addelement(new Text("Проект реализует набор элементов (заголовки, цитаты, списки, таблицы, код, ссылки и т. п.) и предоставляет `MarkdownBuilder` для их создания."));

        builder.header(new Text("Структура проекта"), 2);

        ArrayList<Element> structureList = new ArrayList<>();
        structureList.add(new Text("основная логика работы программы: `task_1_5_1/src/main/java/ru/nsu/vylegzhanin/elements`"));
        structureList.add(new Text("конструктор маркдаун элементов: `task_1_5_1/src/main/java/ru/nsu/vylegzhanin/builder`"));
        builder.list(structureList);

        builder.header(new Text("Использование"), 2);
        builder.addelement(new Text("Для использования библиотеки импортируйте билдер и содержимое папки elements."));

        builder.header(new Text("Пример использования"), 3);
        builder.codeblock(new Text(
            "MarkdownBuilder builder = new MarkdownBuilder();\n" +
            "builder.header(new Text(\"Пример документа\"), 1);"
        ));

        builder.header(new Text("Доступные элементы"), 2);

        List<List<Element>> tableData = new ArrayList<>();

        List<Element> headers = new ArrayList<>();
        headers.add(new Text("Элемент"));
        headers.add(new Text("Метод"));
        headers.add(new Text("Описание"));
        tableData.add(headers);

        tableData.add(createRow("Заголовок", "header(element, level)", "Заголовок уровня 1-6"));
        tableData.add(createRow("Цитата", "blockquotes(element, level)", "Блок цитаты"));
        tableData.add(createRow("Жирный текст", "bold(element)", "**жирный текст**"));
        tableData.add(createRow("Курсив", "italic(element)", "*курсивный текст*"));
        tableData.add(createRow("Зачёркнутый", "strike(element)", "~~зачёркнутый текст~~"));
        tableData.add(createRow("Код", "codeblock(element)", "Блок кода"));
        tableData.add(createRow("Inline код", "inlinecode(element)", "`inline код`"));
        tableData.add(createRow("Ссылка", "link(element)", "Гиперссылка"));
        tableData.add(createRow("Изображение", "image(element, url)", "Изображение"));
        tableData.add(createRow("Список", "list(items)", "Маркированный список"));
        tableData.add(createRow("Таблица", "table(data, alignments)", "Таблица с выравниванием"));
        tableData.add(createRow("Список задач", "tasklist(items)", "Чек-лист задач"));

        builder.table(tableData, Table.Alignment.LEFT, Table.Alignment.LEFT, Table.Alignment.LEFT);

        builder.header(new Text("Сборка и запуск"), 2);
        builder.codeblock(new Text(
            "# Сборка проекта\n./gradlew build\n\n# Запуск примера\n./gradlew run\n\n# Запуск тестов\n./gradlew test"
        ));

        builder.header(new Text("Автор"), 2);
        builder.addelement(new Text("Вылегжанин Максим — НГУ, ФИТ"));

        String markdown = builder.build();

        try (FileWriter writer = new FileWriter("README.md")) {
            writer.write(markdown);
            System.out.println("README.md успешно сгенерирован!");
        } catch (IOException e) {
            System.err.println("Ошибка при записи файла: " + e.getMessage());
        }
        System.out.println("\n--- Содержимое README.md ---\n");
        System.out.println(markdown);
    }

    private static List<Element> createRow(String col1, String col2, String col3) {
        List<Element> row = new ArrayList<>();
        row.add(new Text(col1));
        row.add(new Text(col2));
        row.add(new Text(col3));
        return row;
    }
}
