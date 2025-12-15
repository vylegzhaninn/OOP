# Markdown Builder — Учебный проект

Лучшая библиотека на Java для создания Markdown-документов программно.

Проект реализует набор элементов (заголовки, цитаты, списки, таблицы, код, ссылки и т. п.) и предоставляет `MarkdownBuilder` для их создания.

## Структура проекта

- основная логика работы программы: `task_1_5_1/src/main/java/ru/nsu/vylegzhanin/elements`
- конструктор маркдаун элементов: `task_1_5_1/src/main/java/ru/nsu/vylegzhanin/builder`


## Использование

Для использования библиотеки импортируйте билдер и содержимое папки elements.

### Пример использования

```
MarkdownBuilder builder = new MarkdownBuilder();
builder.header(new Text("Пример документа"), 1);
```

## Доступные элементы

| Элемент | Метод | Описание |
| --- | --- | --- |
| Заголовок | header(element, level) | Заголовок уровня 1-6 |
| Цитата | blockquotes(element, level) | Блок цитаты |
| Жирный текст | bold(element) | **жирный текст** |
| Курсив | italic(element) | *курсивный текст* |
| Зачёркнутый | strike(element) | ~~зачёркнутый текст~~ |
| Код | codeblock(element) | Блок кода |
| Inline код | inlinecode(element) | `inline код` |
| Ссылка | link(element) | Гиперссылка |
| Изображение | image(element, url) | Изображение |
| Список | list(items) | Маркированный список |
| Таблица | table(data, alignments) | Таблица с выравниванием |
| Список задач | tasklist(items) | Чек-лист задач |

## Сборка и запуск

```
# Сборка проекта
./gradlew build

# Запуск примера
./gradlew run

# Запуск тестов
./gradlew test
```

## Автор

Вылегжанин Максим — НГУ, ФИТ

