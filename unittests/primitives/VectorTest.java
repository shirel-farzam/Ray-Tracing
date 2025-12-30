package primitives;

import primitives.Point;
import primitives.Vector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Vector} class.
 */
class VectorTest {

    /** Test vectors used in various test cases. */
    Vector v1 = new Vector(1.0, 2.0, 3.0);
    Vector v2 = new Vector(1.0, 0.0, 0.0);
    Vector v3 = new Vector(0.0, 1.0, 0.0);
    Vector v4 = new Vector(1.0, 1.0, 1.0);
    Vector v5 = new Vector(-5.0, 0.0, 0.0);
    Vector v6 = new Vector(5.0, 0.0, 0.0);
    Vector v7 = new Vector(-1.0, -2.0, -3.0);

    /**
     * Test method for {@link Vector#add(Vector)}.
     * Verifies the correct sum of two vectors.
     */
    @Test
    void testAdd() {

        assertEquals(new Vector(2.0, 4.0, 6.0), v1.add(v1), "Wrong result of adding two vectors");
    }

    /**
     * Test method for {@link Vector#scale(double)}.
     * Checks scaling with positive, zero, and negative factors.
     */
    @Test
    void testScale() {
        double scalar1 = 3;
        double scalar2 = 0;
        double scalar3 = -1;
        assertEquals(new Vector(3, 6, 9), v1.scale(scalar1), "Wrong result for scale function");
        //assertEquals(new Vector(0, 0, 0), v1.scale(scalar2), "Wrong result for scale function");
        assertEquals(new Vector(-1.0, -2.0, -3.0), v1.scale(scalar3), "Wrong result for scale function");
    }

    /**
     * Test method for {@link Vector#dotProduct(Vector)}.
     * Checks dot product calculations.
     */
    @Test
    void testDotProduct() {
        assertEquals(14.0, v1.dotProduct(v1), 0.00001);
        assertEquals(0.0, v2.dotProduct(v3), 0.00001);
        assertEquals(6.0, v1.dotProduct(v4), 0.00001);
    }

    /**
     * Test method for {@link Vector#crossProduct(Vector)}.
     * Verifies correct cross product calculations.
     */
    @Test
    void testCrossProduct() {
       // assertEquals(v0, v2.crossProduct(v5), "Wrong result for CrossProduct function");
      //  assertEquals(v0, v6.crossProduct(v5), "Wrong result for CrossProduct function");
        assertThrows(IllegalArgumentException.class, () -> v2.crossProduct(v6), "Did not construct a zero vector");
        assertEquals(new Vector(0.0, 3.0, -2.0), v1.crossProduct(v2), "Wrong result for CrossProduct function");
    }

    /**
     * Test method for {@link Vector#lengthSquared()}.
     * Checks the squared length of vectors.
     */
    @Test
    void testLengthSquared() {
        assertEquals(14.0, v1.lengthSquared(), "Wrong length squared for vector (1.0, 2.0, 3.0)");
       // assertEquals(0.0, v0.lengthSquared(), "Length squared of zero vector should be 0");
        assertEquals(14.0, v7.lengthSquared(), "Length squared should be same for negative components");
    }

    /**
     * Test method for {@link Vector#length()}.
     * Checks the actual length of vectors.
     */
    @Test
    void testLength() {
        assertEquals(1, v2.length(), 0.00001, "Length of vector (1,0,0) should be 1");
        assertEquals(Math.sqrt(3), v4.length(), 0.00001, "Length of vector (1,1,1) should be √3");
        assertEquals(Math.sqrt(14), v7.length(), 0.00001, "The length of the vector is not calculated correctly.");
       // assertEquals(0.0, v0.length(), 0.00001, "Length of vector (0,0,0) should be 0");
    }

    /**
     * Test method for {@link Vector#normalize()}.
     * Checks normalization of vectors.
     */
    @Test
    void testNormalize() {
        assertEquals(1, v1.normalize().length(), "Wrong result of normalization");
        assertEquals(new Vector(1.0,0.0,0.0), v6.normalize(), "Wrong result of normalization");
        assertTrue(v1.normalize().dotProduct(v1) > 0, "Normalized vector is not parallel to the original one");
    }

    /**
     //* Test method for {@link Vector#(Vector)}.
     * Verifies correct vector subtraction.
     */
    @Test
    void testSubtract() {
        // Equivalence Partition Test: Subtracting two different vectors
        assertEquals(new Vector(-4.0, 2.0, 3.0), v1.subtract(v6), "Subtract two vectors does not work correctly");

        // Boundary Value Test: Subtracting a vector from itself should throw an exception
        assertThrows(IllegalArgumentException.class, () -> v1.subtract(v1), "Subtracting equal vectors should throw an exception");
    }
}
