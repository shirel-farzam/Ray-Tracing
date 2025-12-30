package renderer;

import primitives.Color;
import primitives.Ray;
import renderer.ImageWriter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ImageWriter} class
 */
class ImageWriterTest {
    /**
     * Test for verifying the functionality of the {@link ImageWriter} class
     * in generating and writing an image to a file.
     * <p>
     * Ensures that an image can be correctly created and saved using the class's methods.
     */
    @Test
    void writeImageTest() {
        final int nX = 801;
        final int nY = 501;
        final int interval = 50;
        ImageWriter imageWriter = new ImageWriter("yellow",nX, nY);
        final Color gridColor = new Color(255, 0, 0);
        final Color viewPlaneColor =new Color(java.awt.Color.YELLOW);
        for (int y = 0; y < nY; y++) {
            for (int x = 0; x < nX; x++) {
                imageWriter.writePixel(x, y, viewPlaneColor);
            }
        }
        for (int y = 0; y < nY; y += interval) {
            for (int x = 0; x < nX; x++) {
                imageWriter.writePixel(x, y, gridColor);
            }
        }
        for (int y = 0; y <nY; y++) {
            for (int x = 0; x < nX; x += interval) {
                imageWriter.writePixel(x, y, gridColor);
            }
        }
        imageWriter.writeToImage();
    }

}