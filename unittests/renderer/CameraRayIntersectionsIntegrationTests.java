package renderer;


import geometries.*;
import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for integration of Camera ray construction with intersections
 *
 * @author Dan
 */
public class CameraRayIntersectionsIntegrationTests {
    /**
     * Camera builder for all the tests
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setDirection(new Point(0, 0, -1), Vector.MINUS_Y)
            .setVpSize(3, 3)
            .setVpDistance(1);

    /**
     * Camera for some of the tests
     */
    private final Camera camera1 = cameraBuilder.setLocation(Point.ZERO).build();
    /**
     * Camera for some of the tests
     */
    private final Camera camera2 = cameraBuilder.setLocation(new Point(0, 0, 0.5)).build();

    /**
     * Test helper function to count the intersections and compare with expected
     * value
     *
     * @param camera   camera for the test
     * @param geo      3D body to test the integration of the camera with
     * @param expected amount of intersections
     */
    private void assertCountIntersections(Camera camera, Intersectable geo, int expected) {
        int count = 0;
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 3; ++j) {
                var intersections = geo.findIntersections(camera.constructRay(3, 3, j, i));
                count += intersections == null ? 0 : intersections.size();
            }
        assertEquals(expected, count, "Wrong amount of intersections");
    }

    /**
     * Integration tests of Camera Ray construction with Ray-Sphere
     * intersections
     */
    @Test
    public void cameraRaySphereIntegration() {
        // TC01: Small Sphere 2 points
        assertCountIntersections(camera1, new Sphere(new Point(0, 0, -3), 1d), 2);

        // TC02: Big Sphere 18 points
        assertCountIntersections(camera2, new Sphere(new Point(0, 0, -2.5), 2.5), 18);

        // TC03: Medium Sphere 10 points
        assertCountIntersections(camera2, new Sphere(new Point(0, 0, -2), 2d), 10);

        // TC04: Inside Sphere 9 points
        assertCountIntersections(camera2, new Sphere(new Point(0, 0, -1), 4d), 9);

        // TC05: Beyond Sphere 0 points
        assertCountIntersections(camera1, new Sphere(new Point(0, 0, 1), .5), 0);
    }

    /**
     * Integration tests of Camera Ray construction with Ray-Plane intersections
     */
    @Test
    public void cameraRayPlaneIntegration() {
        // TC01: Plane against camera 9 points
        assertCountIntersections(camera1, new Plane(new Point(0, 0, -5), new Vector(0, 0, 1)), 9);

        // TC02: Plane with small angle 9 points
        assertCountIntersections(camera1, new Plane(new Point(0, 0, -5), new Vector(0, 1, 2)), 9);

        // TC03: Plane parallel to lower rays 6 points
        assertCountIntersections(camera1, new Plane(new Point(0, 0, -5), new Vector(0, 1, 1)), 6);

        // TC04: Beyond Plane 0 points
        assertCountIntersections(camera1, new Plane(new Point(0, 0, -5), new Vector(0, 1, 1)), 6);
    }

    /**
     * Integration tests of Camera Ray construction with Ray-Triangle
     * intersections
     */
    @Test
    public void cameraRayTriangleIntegration() {
        // TC01: Small triangle 1 point
        assertCountIntersections(camera1,
                new Triangle(new Point(1, 1, -2), new Point(-1, 1, -2), new Point(0, -1, -2)),
                1);

        // TC02: Medium triangle 2 points
        assertCountIntersections(camera1,
                new Triangle(new Point(1, 1, -2), new Point(-1, 1, -2), new Point(0, -20, -2)),
                2);
    }

}