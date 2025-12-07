package ru.nsu.vylegzhanin.elements.formatting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.vylegzhanin.elements.Text;

class ImageTest {

    @Test
    void toMarkdown_rendersAltAndUrl() {
        Image image = new Image(new Text("Alt"), "https://example.com/img.png");
        assertEquals("![Alt](https://example.com/img.png)", image.toMarkdown());
    }

    @Test
    void equals_respectsUrlAndText() {
        Image img1 = new Image(new Text("Alt"), "https://example.com/a.png");
        Image img2 = new Image(new Text("Alt"), "https://example.com/a.png");
        Image img3 = new Image(new Text("Alt"), "https://example.com/b.png");
        assertEquals(img1, img2);
        assertNotEquals(img1, img3);
    }
}
