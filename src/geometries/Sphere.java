package geometries;

import java.awt.*;
import java.util.List;
import primitives.Util;
import primitives.Ray;
import primitives.Util.*;
import primitives.Vector;
import primitives.Point;

import static primitives.Util.alignZero;

/**
 * Represents a sphere in 3D space defined by a center point and a radius.
 */
public class Sphere extends RadialGeometry {

    /**
     * The center point of the sphere.
     */
    private final Point center;

    /**
     * Constructs a Sphere with a given center and radius.
     *
     * @param center the center point of the sphere
     * @param radius the radius of the sphere
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }

    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }

    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        // Extract the ray's origin (p0) and direction vector (v)
        Point p0 = ray.getHead();
        Vector v = ray.getDirection();

        // Special case: if the ray starts at the center of the sphere
        if (p0.equals(center)) {
            // The intersection point is along the ray's direction at a distance equal to the radius
            return List.of(new Intersection(this, ray.getPoint(radius)));
        }

        // Calculate the vector from the ray's origin to the sphere's center
        Vector u = center.subtract(p0);

        // Project vector u onto the ray's direction vector v to find the projection length (tm)
        double tm = alignZero(u.dotProduct(v));

        // Calculate the squared distance from the sphere's center to the ray
        double dSquared = alignZero(u.lengthSquared() - tm * tm);

        // Calculate the squared radius of the sphere
        double radiusSquared = radius * radius;

        // If the distance from the ray to the sphere's center is greater than the radius, there are no intersections
        if (alignZero(dSquared - radiusSquared) >= 0) {
            return null;
        }

        // Calculate the distance from the projection point to the intersection points (th)
        double th = Math.sqrt(radiusSquared - dSquared);

        // Calculate the two possible intersection distances along the ray
        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);

        // If both intersection distances are positive, return both intersection points
        if ( t1  > 0 &&  t2 > 0 ) {  //&& alignZero(t1 - distance) <= 0 && alignZero(t2 - distance) <= 0
            Point p1 = ray.getPoint(t1); // First intersection point
            Point p2 = ray.getPoint(t2); // Second intersection point
            return List.of(new Intersection(this, p1), new Intersection(this, p2));
        }

        // If one of the intersection distances is negative, return the positive intersection point
        if (t1 > 0 ) { //&& alignZero(t1 - distance) <= 0
            return List.of(new Intersection(this, ray.getPoint(t1)));
        }
        if ( t2 > 0 ) {  //&& alignZero(t2 - distance) <= 0
            return List.of(new Intersection(this, ray.getPoint(t2)));
        }
        // If both intersection distances are negative, the intersections are behind the ray's origin

        return null;
    }
}
