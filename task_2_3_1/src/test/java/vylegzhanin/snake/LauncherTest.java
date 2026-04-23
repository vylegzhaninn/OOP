package vylegzhanin.snake;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class LauncherTest {
    @Test
    void testLauncherConstructor() {
        Launcher p = new Launcher();
        assertNotNull(p);
    }
}
