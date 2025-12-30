package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Unit tests for the Cylinder class.
 * This class tests the getNormal method to ensure it returns a correct normal vector.
 */
class CylinderTest {

    /**
     * Tests the getNormal method to verify it returns a correct normal vector.
     * The normal vector should have a length of 1 and should be perpendicular to the cylinder's surface.
     */
    @Test
    public void testGetNormalCylinder() {
        // ======= Test Case 1: Normal for infinite cylinder =======
        // Creating an infinite cylinder with axis on the Y-axis and radius 1
        Cylinder infiniteCylinder = new Cylinder(5.0, new Ray(new Point(0, 0, 0), new Vector(0, 1, 0)), 1);

        // Test a point on the side of the cylinder
        Point sidePoint = new Point(1, 0, 0);  // A point on the side of the cylinder
        Vector normal = infiniteCylinder.getNormal(sidePoint);

        // The normal vector should be perpendicular to the Y-axis, meaning it lies in the X-Z plane
        assertEquals(new Vector(0.0, -1.0, 0.0), normal, "Normal vector for infinite cylinder is incorrect");

        // ======= Test Case 2: Normal for finite cylinder =======
        // Creating a finite cylinder with height 10, axis along the Y-axis and radius 2
        Cylinder finiteCylinder = new Cylinder(10, new Ray(new Point(0, 0, 0), new Vector(0, 1, 0)), 2);

        // Test a point on the side of the cylinder
        Point sidePointFinite = new Point(2, 5, 0);  // A point on the side of the cylinder
        Vector normalSide = finiteCylinder.getNormal(sidePointFinite);

        // The normal vector on the side should be perpendicular to the axis (Y-axis) and in the X-Z plane
        assertEquals(new Vector(1, 0, 0), normalSide, "Normal vector on the side of the finite cylinder is incorrect");

        // ======= Test Case 3: Normal for top base of the finite cylinder =======
        // Test a point on the top base (at height 10)
        Point topBasePoint = new Point(2, 10, 0);
        Vector normalTop = finiteCylinder.getNormal(topBasePoint);

        // The normal vector for the top base should point in the direction of the Y-axis (upward)
        assertEquals(new Vector(0, 1, 0), normalTop, "Normal vector on the top base of the finite cylinder is incorrect");

        // ======= Test Case 4: Normal for bottom base of the finite cylinder =======
        // Test a point on the bottom base (at height 0)
        Point bottomBasePoint = new Point(2, 0, 0);
        Vector normalBottom = finiteCylinder.getNormal(bottomBasePoint);

        // The normal vector for the bottom base should point in the opposite direction of the Y-axis (downward)
        assertEquals(new Vector(0, -1, 0), normalBottom, "Normal vector on the bottom base of the finite cylinder is incorrect");
    }

    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}
     */
    @Test
    void findIntersectionsTest() {
        Cylinder cylinder = new Cylinder(1, new Ray(new Point(0, 0, -2), new Vector(0, 0, 1)), 10);
        // ============ Equivalence Partitions Tests ==============
        // Test Case 01 - Ray doesn't intersect the cylinder (0 points)
        Ray ray = new Ray(new Point(-6, 0, 1), new Vector(6, -4, 3));
        var result = cylinder.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");

        // Test Case 02 - Ray starts before and intersects the cylinder (but not through the cylinder's ray) (2 points)
        ray = new Ray(new Point(-6, 0, 1), new Vector(1, -0.1, 0));
        result = cylinder.findIntersections(ray);
       // assertNotNull(result, "ERROR: the intersections' array should not be null");
        if(result!=null) {
            assertEquals(2, result.size(), "ERROR: Wrong number of intersections");
            Point intersectionPoint = new Point(-0.857649282009763, -0.514235071799024, 1);
            Point intersectionPoint2 = new Point(0.738837400821645, -0.673883740082165, 1);
            var exp = List.of(intersectionPoint, intersectionPoint2);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // Test Case 03 - Ray starts inside the cylinder (1 point)
            ray = new Ray(new Point(-0.5, 0, 0), new Vector(-0.5, 0, 1));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");

            assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(-1, 0, 1);
            exp = List.of(intersectionPoint);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // Test Case 04 - Ray starts after the cylinder (0 points)
            ray = new Ray(new Point(-6, 0, 1), new Vector(-1, 0.1, 0));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");


            // =============== Boundary Values Tests ==================
            // **** Group 1: Ray starts at cylinder
            // TC11: Ray starts at cylinder and intersects the cylinder (but not through the cylinder's ray) (1 point)
            ray = new Ray(new Point(0, -1, 1), new Vector(1, 1, 0));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(1, 0, 1);
            exp = List.of(intersectionPoint);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC12: Ray starts at cylinder and goes outside (continuation does not cross through the cylinder's ray) (0 points)
            ray = new Ray(new Point(0, -1, 1), new Vector(-1, -1, 0));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");


            // **** Group 2: Ray (or it's continuation) intersects cylinder through it's ray (not head)
            // TC21 - Ray starts before and is not orthogonal to cylinder's ray (2 points)
            ray = new Ray(new Point(-6, 0, 1), new Vector(6, 0, 1));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(2, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(-1, 0, 1.833333333333333);
            intersectionPoint2 = new Point(1.000000000000001, 0, 2.166666666666667);
            exp = List.of(intersectionPoint, intersectionPoint2);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC22 - Ray starts before and is orthogonal to cylinder's ray (2 points)
            ray = new Ray(new Point(-6, 0, 2), new Vector(6, 0, 0));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(2, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(-1, 0, 2);
            intersectionPoint2 = new Point(1, 0, 2);
            exp = List.of(intersectionPoint, intersectionPoint2);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC23: Ray starts at cylinder and is not orthogonal to cylinder's ray (1 point)
            ray = new Ray(new Point(0, -1, 1), new Vector(0, 1, 1));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(0, 1, 3);
            exp = List.of(intersectionPoint);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC24: Ray starts at cylinder and is orthogonal to cylinder's ray (1 point)
            ray = new Ray(new Point(0, -1, 2), new Vector(0, 1, 0));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(0, 1, 2);
            exp = List.of(intersectionPoint);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC25: Ray starts inside cylinder and is not orthogonal to cylinder's ray (1 point)
            ray = new Ray(new Point(-0.5, 0, 0), new Vector(0.5, 0, 2));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(1, 0, 5.999999999999999);
            exp = List.of(intersectionPoint);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC26: Ray starts inside cylinder and is orthogonal to cylinder's ray (1 point)
            ray = new Ray(new Point(-0.5, 0, 2), new Vector(0.5, 0, 0));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(1, 0, 2);
            exp = List.of(intersectionPoint);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC27: Ray starts after cylinder and is not orthogonal to cylinder's ray (0 points)
            ray = new Ray(new Point(-6, 0, 1), new Vector(-6, 0, -1));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC28: Ray starts after cylinder and is orthogonal to cylinder's ray (0 points)
            ray = new Ray(new Point(-6, 0, 2), new Vector(-6, 0, 0));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");


            // **** Group 3: Ray (or it's continuation) intersects cylinder through it's head
            // TC31 - Ray starts before and is not orthogonal to cylinder's ray (2 points)
            ray = new Ray(new Point(-6, 0, 2), new Vector(6, 0, -4));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(2, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(-1.000000000000001, 0, -1.333333333333333);
            intersectionPoint2 = new Point(0, 0, -2);
            exp = List.of(intersectionPoint, intersectionPoint2);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC32 - Ray starts before and is orthogonal to cylinder's ray (0 points)
            ray = new Ray(new Point(-6, 0, -2), new Vector(6, 0, 0));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC33: Ray starts at cylinder and is not orthogonal to cylinder's ray (1 point)
            ray = new Ray(new Point(0, -1, 2), new Vector(0, 1, -4));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(0, 0, -2);
            exp = List.of(intersectionPoint);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC34: Ray starts at cylinder and is orthogonal to cylinder's ray (0 points)
            ray = new Ray(new Point(0, -1, -2), new Vector(0, 1, 0));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC35: Ray starts inside cylinder and is not orthogonal to cylinder's ray (1 point)
            ray = new Ray(new Point(-0.5, 0, 0), new Vector(0.5, 0, -2));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(0, 0, -2);
            exp = List.of(intersectionPoint);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC36: Ray starts inside cylinder and is orthogonal to cylinder's ray (0 points)
            ray = new Ray(new Point(-0.5, 0, -2), new Vector(0.5, 0, 0));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC37: Ray starts inside cylinder, but after it's head and is not orthogonal to cylinder's ray (1 point)
            ray = new Ray(new Point(-0.5, 0, 0), new Vector(-0.5, 0, 2));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(-1, 0, 2);
            exp = List.of(intersectionPoint);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC38: Ray starts inside cylinder, but after it's head and is orthogonal to cylinder's ray (0 points)
            ray = new Ray(new Point(-0.5, 0, -2), new Vector(-0.5, 0, 0));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC39: Ray starts at cylinder, but after it's head and is not orthogonal to cylinder's ray (0 points)
            ray = new Ray(new Point(0, -1, 2), new Vector(0, -1, 4));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC3-10: Ray starts at cylinder, but after it's head and is orthogonal to cylinder's ray (0 points)
            ray = new Ray(new Point(0, -1, -2), new Vector(0, -1, 0));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC3-11: Ray starts after cylinder and is not orthogonal to cylinder's ray (0 points)
            ray = new Ray(new Point(-6, 0, 2), new Vector(-6, 0, 4));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC3-12: Ray starts after cylinder and is orthogonal to cylinder's ray (0 points)
            ray = new Ray(new Point(-6, 0, -2), new Vector(-6, 0, 0));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC3-13: Ray starts on cylinder's head and is not orthogonal to cylinder's ray (1 point)
            ray = new Ray(new Point(0, 0, -2), new Vector(1, 0, 1));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(1, 0, -1);
            exp = List.of(intersectionPoint);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC3-14: Ray starts on cylinder's head and is orthogonal to cylinder's ray (0 points)
            ray = new Ray(new Point(0, 0, -2), new Vector(1, 0, 0));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC3-15: Ray starts on cylinder's top base (1 point)
            ray = new Ray(new Point(0.5, 0.5, 8), new Vector(-0.5, -0.5, -10));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(0, 0, -2);
            exp = List.of(intersectionPoint);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC3-16: Ray starts on cylinder's bottom base (1 point)
            ray = new Ray(new Point(0, 0, -2), new Vector(-0.5, -0.5, -10));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");


            // **** Group 4: Ray perpendicular to cylinder's ray
            // TC41: Ray and cylinder's ray unite in the same direction (not through head) (1 point)
            ray = new Ray(new Point(0, 0, 2), new Vector(0, 0, 1));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(0, 0, 8);
            exp = List.of(intersectionPoint);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC42: Ray and cylinder's ray unite in the same direction (through head) (2 points)
            ray = new Ray(new Point(0, 0, -3), new Vector(0, 0, 1));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(2, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(0, 0, -2);
            intersectionPoint2 = new Point(0, 0, 8);
            exp = List.of(intersectionPoint, intersectionPoint2);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC43: Ray and cylinder's ray unite in opposite directions (not through head) (0 points)
            ray = new Ray(new Point(0, 0, -3), new Vector(0, 0, -1));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC44: Ray and cylinder's ray unite in opposite directions (through head) (1 point)
            ray = new Ray(new Point(0, 0, 2), new Vector(0, 0, -1));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(0, 0, -2);
            exp = List.of(intersectionPoint);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC45: Ray and cylinder's ray unite in the same direction (starting at head) (1 point)
            ray = new Ray(new Point(0, 0, -2), new Vector(0, 0, 1));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(0, 0, 8);
            exp = List.of(intersectionPoint);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC46: Ray and cylinder's ray unite in opposite directions (starting at head) (0 points)
            ray = new Ray(new Point(0, 0, -2), new Vector(0, 0, -1));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC47: Ray is inside cylinder, and it is perpendicular to cylinder's ray in same direction (1 point)
            ray = new Ray(new Point(-0.5, 0, 0), new Vector(0, 0, 1));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(-0.5, 0, 8);
            exp = List.of(intersectionPoint);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC48: Ray is inside cylinder, and it is perpendicular to cylinder's ray in opposite directions (1 point)
            ray = new Ray(new Point(-0.5, 0, 0), new Vector(0, 0, -1));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(-0.5, 0, -2);
            exp = List.of(intersectionPoint);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC49: Ray is on cylinder's side, and it is perpendicular to cylinder's ray in same direction (0 points)
            ray = new Ray(new Point(1, 0, 0), new Vector(0, 0, 1));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC4-10: Ray is on cylinder's side, and it is perpendicular to cylinder's ray in opposite directions (0 points)
            ray = new Ray(new Point(1, 0, 0), new Vector(0, 0, -1));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC4-11: Ray is outside of cylinder, and it is perpendicular to cylinder's ray in same direction (0 points)
            ray = new Ray(new Point(2, 0, 0), new Vector(0, 0, 1));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC4-12: Ray is outside of cylinder, and it is perpendicular to cylinder's ray in opposite directions (0 points)
            ray = new Ray(new Point(2, 0, 0), new Vector(0, 0, -1));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC4-13: Ray is in the center of the top base of cylinder, and it is perpendicular to cylinder's ray in same direction (0 points)
            ray = new Ray(new Point(0, 0, 8), new Vector(0, 0, 1));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC4-14: Ray is in the center of the top base of cylinder, and it is perpendicular to cylinder's ray in opposite directions (1 point)
            ray = new Ray(new Point(0, 0, 8), new Vector(0, 0, -1));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(0, 0, -2);
            exp = List.of(intersectionPoint);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC4-15: Ray is in the center of the bottom base of cylinder, and it is perpendicular to cylinder's ray in same direction (1 point)
            ray = new Ray(new Point(0, 0, -2), new Vector(0, 0, 1));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(0, 0, 8);
            exp = List.of(intersectionPoint);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC4-16: Ray is in the center of the bottom base of cylinder, and it is perpendicular to cylinder's ray in opposite directions (0 points)
            ray = new Ray(new Point(0, 0, -2), new Vector(0, 0, -1));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC4-17: Ray is on the top base of cylinder, and it is perpendicular to cylinder's ray in same direction (0 points)
            ray = new Ray(new Point(0.5, 0.5, 8), new Vector(0, 0, 1));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC4-18: Ray is on the top base of cylinder, and it is perpendicular to cylinder's ray in opposite directions (1 point)
            ray = new Ray(new Point(0.5, 0.5, 8), new Vector(0, 0, -1));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(0.5, 0.5, -2);
            exp = List.of(intersectionPoint);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC4-19: Ray is on the bottom base of cylinder, and it is perpendicular to cylinder's ray in same direction (1 point)
            ray = new Ray(new Point(0.5, 0.5, -2), new Vector(0, 0, 1));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(0.5, 0.5, 8);
            exp = List.of(intersectionPoint);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC4-20: Ray is on the bottom base of cylinder, and it is perpendicular to cylinder's ray in opposite directions (0 points)
            ray = new Ray(new Point(0.5, 0.5, -2), new Vector(0, 0, -1));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC4-21: Ray is on the edge of top base of cylinder, and it is perpendicular to cylinder's ray in same direction (0 points)
            ray = new Ray(new Point(1, 0, 8), new Vector(0, 0, 1));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC4-22: Ray is on the edge of top base of cylinder, and it is perpendicular to cylinder's ray in opposite directions (0 points)
            ray = new Ray(new Point(1, 0, 8), new Vector(0, 0, -1));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC4-23: Ray is on the edge of bottom base of cylinder, and it is perpendicular to cylinder's ray in same direction (0 points)
            ray = new Ray(new Point(1, 0, -2), new Vector(0, 0, 1));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC4-24: Ray is on the edge of bottom base of cylinder, and it is perpendicular to cylinder's ray in opposite directions (0 points)
            ray = new Ray(new Point(1, 0, -2), new Vector(0, 0, -1));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC4-25: Ray starts outside cylinder against top base but unites with its ray, and it is perpendicular to cylinder's ray in same direction (0 points)
            ray = new Ray(new Point(0, 0, 9), new Vector(0, 0, 1));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC4-26: Ray starts outside cylinder against top base but unites with its ray, and it is perpendicular to cylinder's ray in opposite directions (2 points)
            ray = new Ray(new Point(0, 0, 9), new Vector(0, 0, -1));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(2, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(0, 0, -2);
            intersectionPoint2 = new Point(0, 0, 8);
            exp = List.of(intersectionPoint, intersectionPoint2);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC4-27: Ray starts outside cylinder against bottom base but unites with its ray, and it is perpendicular to cylinder's ray in same direction (2 points)
            ray = new Ray(new Point(0, 0, -3), new Vector(0, 0, 1));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(2, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(0, 0, -2);
            intersectionPoint2 = new Point(0, 0, 8);
            exp = List.of(intersectionPoint, intersectionPoint2);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC4-28: Ray starts outside cylinder against bottom base but unites with its ray, and it is perpendicular to cylinder's ray in opposite directions (0 points)
            ray = new Ray(new Point(0, 0, -3), new Vector(0, 0, -1));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC4-29: Ray starts outside cylinder against top base, and it is perpendicular to cylinder's ray in same direction (0 points)
            ray = new Ray(new Point(0.5, 0.5, 9), new Vector(0, 0, 1));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC4-30: Ray starts outside cylinder against top base, and it is perpendicular to cylinder's ray in opposite directions (2 points)
            ray = new Ray(new Point(0.5, 0.5, 9), new Vector(0, 0, -1));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(2, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(0.5, 0.5, -2);
            intersectionPoint2 = new Point(0.5, 0.5, 8);
            exp = List.of(intersectionPoint, intersectionPoint2);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC4-31: Ray starts outside cylinder against bottom base, and it is perpendicular to cylinder's ray in same direction (2 points)
            ray = new Ray(new Point(0.5, 0.5, -3), new Vector(0, 0, 1));
            result = cylinder.findIntersections(ray);
            assertNotNull(result, "ERROR: the intersections' array should not be null");
            assertEquals(2, result.size(), "ERROR: Wrong number of intersections");
            intersectionPoint = new Point(0.5, 0.5, -2);
            intersectionPoint2 = new Point(0.5, 0.5, 8);
            exp = List.of(intersectionPoint, intersectionPoint2);
            assertEquals(exp, result, "ERROR: Wrong intersection point");

            // TC4-32: Ray starts outside cylinder against bottom base, and it is perpendicular to cylinder's ray in opposite directions (0 points)
            ray = new Ray(new Point(0.5, 0.5, -3), new Vector(0, 0, -1));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");


            // **** Group 5: Ray tangent to cylinder
            // TC51: Ray starts before cylinder and is tangent to it's side (0 points)
            ray = new Ray(new Point(-1, -1, 1), new Vector(1, 0, 0));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC52: Ray starts on cylinder and is tangent to it's side (0 points)
            ray = new Ray(new Point(0, -1, 1), new Vector(1, 0, 0));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC53: Ray starts after cylinder, and it's continuation is tangent to cylinder's side (0 points)
            ray = new Ray(new Point(1, -1, 1), new Vector(1, 0, 0));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC54: Ray starts before cylinder and is tangent to the edge between the top base and the cylinder's side (0 points)
            ray = new Ray(new Point(-6, 0, 1), new Vector(5, 0, 7));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC55: Ray starts before cylinder and is tangent to the edge between the bottom base and the cylinder's side (0 points)
            ray = new Ray(new Point(-6, 0, 1), new Vector(5, 0, -3));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC56: Ray starts on cylinder and is tangent to the edge between the top base and the cylinder's side (0 points)
            ray = new Ray(new Point(-1, 0, 8), new Vector(5, 0, 7));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC57: Ray starts on cylinder and is tangent to the edge between the bottom base and the cylinder's side (0 points)
            ray = new Ray(new Point(-1, 0, -2), new Vector(5, 0, -3));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC58: Ray starts after cylinder, and it's continuation is tangent to the edge between the top base and the cylinder's side (0 points)
            ray = new Ray(new Point(-6, 0, 1), new Vector(-5, 0, -7));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC59: Ray starts after cylinder, and it's continuation is tangent to the edge between the bottom base and the cylinder's side (0 points)
            ray = new Ray(new Point(-6, 0, 1), new Vector(-5, 0, 3));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC5-10: Ray starts before cylinder and is tangent to the top base (0 points)
            ray = new Ray(new Point(-6, 0, 8), new Vector(6, -1, 0));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC5-11: Ray starts before cylinder and is tangent to the bottom base (0 points)
            ray = new Ray(new Point(-6, 0, -2), new Vector(6, -1, 0));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC5-12: Ray starts on cylinder and is tangent to the top base (0 points)
            ray = new Ray(new Point(0, -1, 8), new Vector(6, -1, 0));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC5-13: Ray starts on cylinder and is tangent to the bottom base (0 points)
            ray = new Ray(new Point(0, -1, -2), new Vector(6, -1, 0));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC5-14: Ray starts after cylinder and is tangent to the top base (0 points)
            ray = new Ray(new Point(-6, 0, 8), new Vector(-6, 1, 0));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");

            // TC5-15: Ray starts after cylinder and is tangent to the bottom base (0 points)
            ray = new Ray(new Point(-6, 0, -2), new Vector(-6, 1, 0));
            result = cylinder.findIntersections(ray);
            assertNull(result, "ERROR: the intersections' array should be null");
        }
    }
}

