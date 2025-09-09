#!/bin/bash

# Компиляция основного кода
javac -d build src/main/java/ru/nsu/shadrina/HeapSort.java

# Компиляция тестов
javac -cp "build:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar" \
      -d build src/test/java/ru/nsu/shadrina/SampleTest.java

# Запуск тестов
java -cp "build:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar" \
     org.junit.runner.JUnitCore ru.nsu.shadrina.SampleTest

# Запуск приложения
java -cp build ru.nsu.shadrina.HeapSort