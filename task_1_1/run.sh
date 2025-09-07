#!/bin/bash
set -e # Остановка при ошибке

SRC_MAIN="app/src/main/java"
OUT_DIR="out"
JAR_FILE="HeapSortApp.jar"

rm -rf "$OUT_DIR"
mkdir -p "$OUT_DIR"

javac -d "$OUT_DIR" $(find $SRC_MAIN -name "*.java")

# Манифест для jar
echo "Main-Class: org.example.HeapSort" > MANIFEST.MF

jar cfm "$JAR_FILE" MANIFEST.MF -C "$OUT_DIR" .

javadoc -d docs $(find $SRC_MAIN -name "*.java")

java -jar "$JAR_FILE"
