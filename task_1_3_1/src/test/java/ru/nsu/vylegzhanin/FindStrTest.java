package ru.nsu.vylegzhanin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Набор тестов, проверяющий строковый вывод {@link FindStr} при различных входных данных.
 */
class FindStrTest {

    @Test
    @DisplayName("Корректный вывод при нескольких совпадениях")
    void printsMultipleMatches() throws IOException {
        Path temp = writeTempFile("abracadabra");

        String output = captureStdout(() -> FindStr.find(temp.toString(), "bra"));

        assertEquals("[1, 8]", output);
    }

    @Test
    @DisplayName("Корректный вывод при отсутствии совпадений")
    void printsEmptyListWhenNotFound() throws IOException {
        Path temp = writeTempFile("abcdef");

        String output = captureStdout(() -> FindStr.find(temp.toString(), "zzz"));

        assertEquals("[]", output);
    }

    /**
     * Создаёт временный UTF-8 файл с заданным содержимым и помечает его на удаление.
     */
    private Path writeTempFile(String content) throws IOException {
        Path tempFile = Files.createTempFile("findstr", ".txt");
        Files.writeString(tempFile, content, StandardCharsets.UTF_8);
        tempFile.toFile().deleteOnExit();
        return tempFile;
    }

    /**
     * Перехватывает стандартный вывод на время выполнения переданного действия.
     */
    private String captureStdout(ThrowingRunnable action) throws IOException {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (PrintStream proxy = new PrintStream(buffer, true, StandardCharsets.UTF_8)) {
            System.setOut(proxy);
            action.run();
        } finally {
            System.setOut(originalOut);
        }
        return buffer.toString(StandardCharsets.UTF_8).trim();
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws IOException;
    }
}
