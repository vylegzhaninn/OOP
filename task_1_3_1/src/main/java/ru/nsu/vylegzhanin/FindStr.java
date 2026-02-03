package ru.nsu.vylegzhanin;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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
        if (goal.isEmpty()) {
            throw new IllegalArgumentException("goal must not be empty");
        }

        char[] pattern = goal.toCharArray();
        int[] fallback = buildFallback(pattern);

        try (Reader reader = Files.newBufferedReader(Path.of(fileName), StandardCharsets.UTF_8)) {
            List<Long> matches = new ArrayList<>();
            int matched = 0;
            long index = 0L;
            int rawChar;

            while ((rawChar = reader.read()) != -1) {
                char next = (char) rawChar;

                while (matched > 0 && next != pattern[matched]) {
                    matched = fallback[matched - 1];
                }

                if (next == pattern[matched]) {
                    matched++;
                }

                if (matched == pattern.length) {
                    matches.add(index - pattern.length + 1);
                    matched = fallback[matched - 1];
                }

                index++;
            }

            System.out.println(matches);
        }
    }

    private static int[] buildFallback(char[] pattern) {
        int[] fallback = new int[pattern.length];
        int matched = 0;
        for (int i = 1; i < pattern.length; i++) {
            while (matched > 0 && pattern[i] != pattern[matched]) {
                matched = fallback[matched - 1];
            }
            if (pattern[i] == pattern[matched]) {
                matched++;
            }
            fallback[i] = matched;
        }
        return fallback;
    }
}