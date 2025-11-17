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
}
