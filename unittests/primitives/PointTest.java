package primitives;

import primitives.Ray;
import primitives.Point;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Point} class.
 */
class PointTest {

    public static final double DELTA = 0.00001;
    /** Test points used in various test cases. */
    Point point1 = new Point(2.0, 4.0, 6.0);
    Point point2 = new Point(3.0, 5.0, 7.0);
    Point point3 = new Point(0.0, 0.0, 0.0);
    Point point4 = new Point(2.0, 0.0, 0.0);


    /** Test vectors used for addition and subtraction operations. */
    Vector v1 = new Vector(1.0, 2.0, 3.0);
    Vector v2 = new Vector(-1.0, -2.0, -3.0);

    /**
     * Test method for {@link Point#subtract(Point)}.
     * Verifies correct subtraction of two points.
     */
    @Test
    void testSubtract() {
        // Test subtraction of a vector from a point

        assertEquals(new Point(1.0, 1.0, 1.0), point2.subtract(point1), "Wrong result of adding a vector to a point");
    }

    /**
     * Test method for {@link Point#add(Vector)}.
     * Verifies correct addition of a vector to a point.
     */
    @Test
    void testAdd() {
        // Test addition of vectors to points
        assertEquals(new Point(3.0, 6.0, 9.0), point1.add(v1), "Wrong result of adding a vector to a point");
        assertEquals(new Point(1.0, 2.0, 3.0), point1.add(v2), "Wrong result of adding a vector to a point");
    }

    /**
     * Test method for {@link Point#distance(Point)}.
     * Verifies correct distance calculation between points.
     */
    @Test
    void testDistance() {
        // Test distance calculation between the same point (should be zero)
        assertEquals(0.0, point1.distance(point1), DELTA, "Wrong result of distance calculation between the same points");
        // Test distance calculation between two different points
        assertEquals(2.0, point3.distance(point4), DELTA, "Wrong result of distance calculation between two points");
    }

    /**
     * Test method for {@link Point#distanceSquared(Point)}.
     * Verifies correct squared distance calculation between points.
     */
    @Test
    void testDistanceSquared() {
        // Test squared distance calculation between the same point (should be zero)
        assertEquals(0.0, point1.distanceSquared(point1), DELTA, "Squared distance calculation between the same points failed");
        // Test squared distance calculation between two different points
        assertEquals(3.0, point1.distanceSquared(point2), DELTA, "Squared distance calculation between two points failed");
    }
}
