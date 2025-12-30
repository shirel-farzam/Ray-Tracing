package primitives;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RayTest {

    @Test
    void testRay() {
        Ray r=new Ray(new Point(0.0,0.0,0.0), new Vector(1.0,2.0,3.0));
        assertEquals(1.0,r.getDirection().length(),0.00001,"Ray is not nomalized!!");

    }

    @Test
    void testGetPoint() {
        Point p1 = new Point(1, 2, 3);
        Ray ray = new Ray(p1, new Vector(1, 0, 0));
        // ============ Equivalence Partitions Tests ==============
        // TC01: t is a negative number
        assertEquals(new Point(-2, 2, 3), ray.getPoint(-3), "Bad getPoint with negative t");

        // TC02: t is a positive number
        assertEquals(new Point(4, 2, 3), ray.getPoint(3), "Bad getPoint with positive t");

        // =============== Boundary Values Tests =================
        // TC03: t is zero
        assertEquals(p1, ray.getPoint(0), "Bad getPoint with t=0");
    }

    @Test
    void testFindClosetPoint() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: simple check
        List<Point> pointList = List.of(
                new Point(0, 0, 0),
                new Point(1, 1, 1),
                new Point(2, 2, 2),
                new Point(3, 3, 3));
        Ray test = new Ray(new Point(2.2, 2.4, 1.9), new Vector(3, 2.4, 1.9));
        assertEquals(pointList.get(2), test.findClosestPoint(pointList));
        // =============== Boundary Values Tests ==================
        //TC01: null check
        assertNull(test.findClosestPoint(null));
        //TC02: first point
        Ray test2 = new Ray(new Point(0.2, 0.2, 0.2), new Vector(1, 0, 0));
        assertEquals(pointList.get(0), test2.findClosestPoint(pointList));
        //TC: last point
        Ray test3 = new Ray(new Point(4, 4, 4), new Vector(5, 4, 4));
        assertEquals(pointList.get(3), test3.findClosestPoint(pointList));
    }


}
