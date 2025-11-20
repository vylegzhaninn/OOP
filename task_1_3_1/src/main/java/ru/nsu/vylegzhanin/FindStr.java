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
        try (Reader reader = Files.newBufferedReader(Path.of(fileName), StandardCharsets.UTF_8)) {
            int index = 0;
            List<Integer> ans = new ArrayList<>();
            List<MatchEntry> pairs = new ArrayList<>();
            int rawChar;

            while ((rawChar = reader.read()) != -1) {
                char c = (char) rawChar;
                Iterator<MatchEntry> iterator = pairs.iterator();
                while (iterator.hasNext()) {
                    MatchEntry pair = iterator.next();
                    int pos = pair.getCurrentPosition();

                    if (c == goal.charAt(pos)) {
                        pair.incrementPosition();
                        if (pair.getCurrentPosition() == goal.length()) {
                            ans.add(pair.getStartIndex());
                            iterator.remove();
                        }
                    } else {
                        iterator.remove();
                    }
                }

                if (c == goal.charAt(0)) {
                    pairs.add(new MatchEntry(index));
                }

                index++;
            }
            System.out.println(ans);
        }
    }

    /**
     * Хранит индекс начала предполагаемого совпадения и текущую позицию в искомой строке.
     */
    private static final class MatchEntry {
        private final int startIndex;
        private int currentPosition;

        private MatchEntry(int startIndex) {
            this.startIndex = startIndex;
            this.currentPosition = 1;
        }

        private int getStartIndex() {
            return startIndex;
        }

        private int getCurrentPosition() {
            return currentPosition;
        }

        private void incrementPosition() {
            currentPosition++;
        }
    }
}