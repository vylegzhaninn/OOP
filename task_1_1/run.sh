javac -d build/classes/java/main src/main/java/ru/nsu/vylegzhanin/HeapSort.java

echo "Main-Class: ru.nsu.vylegzhanin.HeapSort" > MANIFEST.MF
jar cfm build/libs/app.jar MANIFEST.MF -C build/classes/java/main .

java -jar build/libs/app.jar