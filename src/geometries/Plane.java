package geometries;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import primitives.Util;

import javax.imageio.ImageWriter;
import java.awt.*;
import java.util.List;

/**
 * Represents a plane in 3D space defined by a point and a normal vector.
 */
public class Plane extends Geometry {

    /**
     * The normal vector of the plane.
     */
    private final Vector normal;

    /**
     * A point on the plane.
     */
    private final Point q;

    /**
     * Constructs a plane from three points lying on the plane.
     *
     * @param point1 the first point
     * @param point2 the second point
     * @param point3 the third point
     */
    public Plane(Point point1, Point point2, Point point3) {
        // בדיקה אם הנקודות חופפות
        if (point1.equals(point2) || point1.equals(point3) || point2.equals(point3)) {
            throw new IllegalArgumentException("Plane cannot be defined by identical points");
        }

        // יצירת וקטורים בין הנקודות
        Vector v1 = point2.subtract(point1);
        Vector v2 = point3.subtract(point1);

        // בדיקה אם הוקטורים חופפים או אפסיים
        if (v1.equals(Vector.ZERO) || v2.equals(Vector.ZERO) || v1.crossProduct(v2).equals(Vector.ZERO)) {
            throw new IllegalArgumentException("Plane cannot be defined by collinear points or zero vectors");
        }

        // הגדרת המאפיינים של המישור
        this.q = point1;
        this.normal = v1.crossProduct(v2).normalize();
    }


    /**
     * Constructs a plane from a point and a normal vector.
     *
     * @param point      a point on the plane
     * @param normal the normal vector of the plane
     */
    public Plane(Point point, Vector normal) {
        this.q = point;
        this.normal = normal.normalize();
    }

    @Override
    public Vector getNormal(Point point) {
        return normal.normalize();
    }

    /**
     * Returns the normal vector of the plane.
     *
     * @return the normal vector
     */
    public Vector getNormal() {
        return normal.normalize();
    }


    /**
     * This method calculates the intersections between a ray and a plane.
     * It returns the intersection point(s) if any, or null if no intersection occurs.
     *
     * @param ray the ray to check for intersections with the plane.
     * @return a list of intersections between the ray and the plane, or null if no intersection exists.
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        Vector direction = ray.getDirection();
        Point point0 = ray.getHead();
        // if the ray is parallel to the plane or the ray starts on the plane at the point q
        if (Util.isZero(direction.dotProduct(normal)) || q.equals(point0))
            return null;
        double t = normal.dotProduct(q.subtract(point0)) / normal.dotProduct(direction);


        return Util.alignZero(t) <= 0d? null : List.of(new Intersection(this,ray.getPoint(t)));
    }
    }

