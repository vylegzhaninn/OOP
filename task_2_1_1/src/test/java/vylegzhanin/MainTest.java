package vylegzhanin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/** Тесты для класса {@link Main}. */
public class MainTest {

    @Test
    void testMainOutput() throws InterruptedException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        PrintStream old = System.out;
        try {
            System.setOut(ps);
            Main.main();
        } finally {
            System.setOut(old);
        }

        String s = out.toString();
        Assertions.assertTrue(s.contains("1)"), "should contain section 1");
        Assertions.assertTrue(s.contains("2)"), "should contain section 2");
        Assertions.assertTrue(s.contains("3)"), "should contain section 3");
    }
}
