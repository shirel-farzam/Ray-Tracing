package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Plane} class.
 */
class PlaneTest {

    // Define test points
    Point point = new Point(1.0, 2.0, 3.0);
    Point point1 = new Point(2.0, 4.0, 6.0);
    Point point2 = new Point(1.0, 0.0, 0.0);
    Point point3 = new Point(0, 1, 1);
    Point point4 = new Point(0, 0, 1);
    Point point5 = new Point(1.0, 1.0, 1.0);

    /**
     * Test method for {@link Plane#Plane(Point, Point, Point)}.
     * Verifies proper construction and exception handling.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test for a valid plane creation
        assertDoesNotThrow(() -> new Plane(point3, new Point(0, 1, 0), point4),
                "Failed to create a proper plane");

        // TC02: Test for exception when all points are identical
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(point, point, point),
                "Failed to throw an exception when creating a plane with all the same points");

        // =============== Boundary Values Tests =================
        // TC03: Test for exception when points are collinear
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(point2, new Point(2, 0, 0), new Point(3, 0, 0)),
                "Failed to throw an exception when creating a plane with points on the same line");

        // TC04: Test for exception when two points converge
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(Point.ZERO, point5, point5),
                "Failed to throw an exception when creating a plane with points that converge");

        assertThrows(IllegalArgumentException.class,
                () -> new Plane(point5, Point.ZERO, point5),
                "Failed to throw an exception when creating a plane with points that converge");

        assertThrows(IllegalArgumentException.class,
                () -> new Plane(point5, point5, Point.ZERO),
                "Failed to throw an exception when creating a plane with points that converge");
    }

    /**
     * Test method for {@link Plane#getNormal()}.
     * Verifies correct computation of the normal vector.
     */
    @Test
    void testGetNormal() {
        // Define a test point and expected normal vector
        Point point2 = new Point(1, 0, 0);
        Vector expectedNormal = new Vector(0, 0, 1);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test for correct normal vector calculation
        Vector normal = new Plane(point2, new Point(0, 1, 0), new Point(1, 1, 0)).getNormal();

        assertTrue(normal.equals(expectedNormal) || normal.equals(new Vector(0, 0, -1)),
                "Failed to get the normal vector of the plane 0");
    }

    @Test
    void testTestGetNormal() {
    }

    @Test
    void testTestGetNormal1() {
    }

    @Test
    void testFindIntersections() {

        // Points and vectors to define the plane and rays
        Point p001 = new Point(0.0, 0.0, 1.0);
        Point p101 = new Point(1.0, 0.0, 1.0);
        Point p011 = new Point(0.0, 1.0, 1.0);
        Vector v001 = new Vector(0.0, 0.0, 1.0);

        // Define the plane
        Plane plane = new Plane(p001, p101, p011);
        Plane normalPlane = new Plane(p011, v001);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray is not perpendicular, not parallel, and does not intersect the plane
        Point p012 = new Point(0.0, 1.0, 2.0);
        Vector v111 = new Vector(1.0, 1.0, 1.0);
        assertNull(plane.findIntersections(new Ray(p012, v111)), "Ray's line does not intersect the plane (case 1)");

        // TC02: Ray is not perpendicular, not parallel, and intersects the plane
        Point p010 = new Point(0.0, 1.0, 0.0);
        Vector v033 = new Vector(0.0, 3.0, 3.0);
        Point p021 = new Point(0.0, 2.0, 1.0);
        final var exp1 = List.of(p021);
        final var result1 = plane.findIntersections(new Ray(p010, v033));
        assertEquals(exp1, result1, "Ray intersects the plane");

        // TC03: Ray is not perpendicular, not parallel, and starts at the intersection point
        assertNull(plane.findIntersections(new Ray(p011, v033)), "Ray's line does not intersect the plane (case 2)");

        // TC04: Ray starts at a point on the plane
        Vector v011 = new Vector(0.0, 1.0, 1.0);
        assertNull(normalPlane.findIntersections(new Ray(p011, v011)), "Ray's line does not intersect the plane");

        // =============== Boundary Values Tests =================

        // TC05: Ray is parallel and does not intersect the plane
        Point p002 = new Point(0.0, 0.0, 2.0);
        Vector v100 = new Vector(1.0, 0.0, 0.0);
        Ray rayNull = new Ray(p002, v100);
        assertNull(plane.findIntersections(rayNull), "Ray's line does not intersect the plane (case 3)");

        // TC06: Ray is parallel and lies on the plane
        Ray rayNull2 = new Ray(p001, v100);
        assertNull(plane.findIntersections(rayNull2), "Ray's line does not intersect the plane (case 4)");

        // TC07: Ray is perpendicular to the plane and does not intersect
        Vector v003 = new Vector(0.0, 0.0, 3.0);
        assertNull(plane.findIntersections(new Ray(p002, v003)), "Ray's line does not intersect the plane (case 5)");

        // TC08: Ray is perpendicular and starts at the intersection point
        assertNull(plane.findIntersections(new Ray(p001, v003)), "Ray's line does not intersect the plane");

        // TC09: Ray is perpendicular and intersects the plane
        final var exp2 = List.of(p011);
        final var result2 = plane.findIntersections(new Ray(p010, v001));
        assertEquals(exp2, result2, "Ray intersects the plane");
    }



}
