package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import primitives.Point;
import primitives.Vector;

/**
 * Unit tests for the {@link Polygon} class.
 */
class PolygonTest {

    /**
     * Test method for {@link Polygon#getNormal(Point)}.
     * Verifies correct computation of the normal vector.
     */
    @Test
    void testGetNormal() {
        // Create a polygon using specified points
        Polygon polygon = new Polygon(
                new Point(1, 1, 0),
                new Point(1, 0, 0),
                new Point(-1, -1, 0),
                new Point(0, 1, 0)
        );

        // Define additional test points
        Point[] pts = {
                new Point(0, 0, 1),
                new Point(1, 0, 0),
                new Point(0, 1, 0),
                new Point(-1, 1, 1)
        };

        // Create a polygon using the test points
        Polygon pol = new Polygon(pts);

        // Ensure no exception is thrown when retrieving the normal
        assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "Exception was thrown in getNormal method");

        // Compute the normal vector
        Vector result = pol.getNormal(new Point(0, 0, 1));

        // Verify that the normal is a unit vector (length 1)
        assertEquals(1, result.length(), 0.00001, "Polygon's normal is not a unit vector");

        // Verify that the normal is orthogonal to each polygon edge
        for (int i = 0; i < 3; ++i) {
            assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1])), 0.00001,
                    "Polygon's normal is not orthogonal to one of the edges");
        }
    }

    @Test
    void testFindIntersections() {
    }
}
