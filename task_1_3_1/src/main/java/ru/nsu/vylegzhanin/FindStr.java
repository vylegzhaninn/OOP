package ru.nsu.vylegzhanin;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Утилита для чтения текстового файла посимвольно и поиска всех позиций заданной подстроки.
 */
public class FindStr {

    /**
     * Сканирует файл и печатает в стандартный вывод индексы всех вхождений подстроки {@code goal}.
     *
     * @param fileName путь к входному файлу в кодировке UTF-8
     * @param goal     искомая подстрока, должен содержать хотя бы один символ
     * @throws IOException при ошибках доступа к файлу или чтения его содержимого
     * @throws IllegalArgumentException если {@code goal} пустая строка
     */
    public static void find(String fileName, String goal) throws IOException {
        Reader reader = Files.newBufferedReader(Path.of(fileName), StandardCharsets.UTF_8);
        int index = 0;
        List<Integer> ans = new ArrayList<>();
        List<MatchEntry> pairs = new ArrayList<>();
        int rawChar;

        while ((rawChar = reader.read()) != -1) {
            char c = (char) rawChar;
            Iterator<MatchEntry> iterator = pairs.iterator();
            while (iterator.hasNext()) {
                MatchEntry pair = iterator.next();
                pair.getBuffer().append(c);
                if (pair.getBuffer().toString().equals(goal)) {
                    ans.add(pair.getStartIndex());
                    iterator.remove();
                }
            }

            if (c == goal.charAt(0)) {
                pairs.add(new MatchEntry(index, c));
            }

            index++;
        }

        System.out.println(ans);
    }

    /**
     * Хранит индекс начала предполагаемого совпадения и накапливает уже найденные символы.
     */
    private static final class MatchEntry {
        private final int startIndex;
        private final StringBuilder buffer;

        private MatchEntry(int startIndex, char firstChar) {
            this.startIndex = startIndex;
            this.buffer = new StringBuilder().append(firstChar);
        }

        private int getStartIndex() {
            return startIndex;
        }

        private StringBuilder getBuffer() {
            return buffer;
        }
    }
}