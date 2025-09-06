#!/bin/bash
set -e

SRC_MAIN="app/src/main/java"
OUT_DIR="out"
JAR_FILE="HeapSortApp.jar"

rm -rf "$OUT_DIR"
mkdir -p "$OUT_DIR"

# Компиляция исходников
javac -d "$OUT_DIR" $(find $SRC_MAIN -name "*.java")

# Манифест для jar
echo "Main-Class: org.example.HeapSort" > MANIFEST.MF

# Создание jar
jar cfm "$JAR_FILE" MANIFEST.MF -C "$OUT_DIR" .

# Генерация документации
javadoc -d docs $(find $SRC_MAIN -name "*.java")

# Запуск
java -jar "$JAR_FILE"
