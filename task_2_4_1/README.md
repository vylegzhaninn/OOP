# Task_2_4_1: OOP Auto Checker with Groovy DSL

Консольное приложение для преподавателя ООП:
- читает конфигурацию из DSL (`oop-check.gradle`),
- поддерживает импорт долгоживущих конфигов (`importConfig`),
- клонирует/обновляет репозитории студентов,
- для ветки `main`/`master` запускает `compileJava -> javadoc -> checkstyleMain -> test`,
- парсит JUnit XML и считает passed/failed/skipped,
- вычисляет баллы, контрольные точки, итоговую оценку,
- печатает HTML-отчет в `stdout`.

## Структура DSL

```groovy
importConfig 'oop-check.base.gradle'

course {
    task('task_2_4_1') {
        title 'DSL auto-checker'
        maxPoints 10
        softDeadline '2026-04-20'
        hardDeadline '2026-04-27'
    }

    group('M3101') {
        student('student-github') {
            fullName 'Student Name'
            repo 'https://github.com/student/repository.git'
        }
    }

    check('student-github', 'task_2_4_1') {
        submittedAt '2026-04-22'
        bonus 0.0
    }

    checkpoint 'Checkpoint 1', '2026-04-25'

    settings {
        workspace '.oop-checker-work'
        timeoutSeconds 600
        mainBranch 'main'
        fallbackBranch 'master'
        hardLateMultiplier 0.1
        clearGrades()
        grade 'A', 85
        grade 'B', 70
        grade 'C', 55
        grade 'D', 40
        grade 'F', 0
    }
}
```

## Запуск

```bash
cd /Users/maksimvylegzanin/IdeaProjects/Java_OOP/task_2_4_1
./gradlew run
```

Или с явным файлом:

```bash
./gradlew run --args="--config oop-check.gradle"
```

## Тесты

```bash
./gradlew test
```

## Важно

Git-команды выполняются с отключенными интерактивными запросами (`GIT_TERMINAL_PROMPT=0`, `GCM_INTERACTIVE=Never`).
Если у пользователя нет доступа к репозиторию, это отражается в HTML-отчете как ошибка этапа git.

