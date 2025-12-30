package geometries;

import primitives.*;
import primitives.Ray;
import geometries.Polygon;

import java.util.List;

/**
 * Represents a triangle in 3D space defined by three vertices.
 */
public class Triangle extends Polygon {

    /**
     * Constructs a Triangle from three points.
     *
     * @param point1 the first vertex of the triangle
     * @param point2 the second vertex of the triangle
     * @param point3 the third vertex of the triangle
     */
    public Triangle(Point point1, Point point2, Point point3) {
        super(point1, point2, point3);

    }

    /**
     * This method calculates the intersections between a ray and a triangle defined by three vertices.
     * It first checks for intersection with the plane of the triangle and then checks if the ray
     * intersects the triangle itself by evaluating the sign of the dot products with the triangle's edges.
     * If an intersection occurs, the method returns the intersection point(s).
     *
     * @param ray the ray to check for intersections with the triangle.
     * @return a list of intersection points between the ray and the triangle, or null if no intersection exists.
     */
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        Plane plane = new Plane(vertices.get(0), vertices.get(1), vertices.get(2));
        if (plane.findIntersections(ray) == null) return null;
        Point p0 = ray.getHead();
        Vector rayDirection = ray.getDirection();
        Vector v1 = vertices.get(0).subtract(p0);
        Vector v2 = vertices.get(1).subtract(p0);
        Vector v3 = vertices.get(2).subtract(p0);
        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        double d1 = Util.alignZero(n1.dotProduct(rayDirection));
        double d2 = Util.alignZero(n2.dotProduct(rayDirection));
        double d3 = Util.alignZero(n3.dotProduct(rayDirection));

        if ((d1 > 0 && d2 > 0 && d3 > 0) ||
                (d1 < 0 && d2 < 0 && d3 < 0) ||
                (d1 == 0 && d2 == 0) ||
                (d2 == 0 && d3 == 0) ||
                (d1 == 0 && d3 == 0)) {

            // אם יש חיתוך, צריך להחזיר אותו עם this ולא להשאיר את זה מה-plane
            List<Point> pts = plane.findIntersections(ray);
            return pts == null ? null : List.of(new Intersection(this, pts.get(0)));
        }

        return null;
    }
}
