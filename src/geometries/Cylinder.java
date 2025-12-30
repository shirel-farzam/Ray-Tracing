package geometries;
import static primitives.Util.isZero;

import primitives.*;
import primitives.Ray;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.alignZero;

/**
 * Represents a finite cylinder in 3D space, defined by a central axis, radius, and height.
 * Inherits from {@link Tube}, which represents an infinite cylinder.
 */
public class Cylinder extends Tube {

    /**
     * The height of the cylinder.
     */
    private final double height;

    /**
     * Constructs a Cylinder with a given height, axis, and radius.
     *
     * @param height the height of the cylinder
     * @param axis   the central axis of the cylinder
     * @param radius the radius of the cylinder
     */
    public Cylinder(double height, Ray axis, double radius) {
        super(axis, radius);
        this.height = height;
    }

    @Override
    public Vector getNormal(Point point) {
        Point p0 = axis.getPoint(0d);
        Vector dir = this.axis.getDirection();

        //  If p0 is the head of the axis
        if (point.equals(p0))
            return dir.scale(-1);

        // If p1 is the end of the axis
        if (point.equals(axis.getPoint(height)))
            return dir;

        // If the point is on the top or bottom surface of the cylinder

        if (Util.isZero(p0.subtract(point).dotProduct(dir)))
            return dir.scale(-1d);

        if (Util.isZero(axis.getPoint(height).subtract(point).dotProduct(dir)))
            return dir;

        // Otherwise, call the superclass method
        return super.getNormal(point);
    }

    @Override
    public  List<Intersection> calculateIntersectionsHelper(Ray ray) {
        List<Intersection> intersections = new ArrayList<>();

        // Vector from the ray origin to the axis origin
        Vector p0ToRayOrigin = ray.getHead().subtract(axis.getHead());
        Vector axisDirection = axis.getDirection();

        // Project the ray direction onto the axis direction (this is needed for the cylinder's axis)
        double a = ray.getDirection().dotProduct(ray.getDirection()) - Math.pow(ray.getDirection().dotProduct(axisDirection), 2);
        double b = 2 * (ray.getDirection().dotProduct(p0ToRayOrigin) - (ray.getDirection().dotProduct(axisDirection) * p0ToRayOrigin.dotProduct(axisDirection)));
        double c = p0ToRayOrigin.dotProduct(p0ToRayOrigin) - Math.pow(p0ToRayOrigin.dotProduct(axisDirection), 2) - Math.pow(radius, 2);

        // Solve the quadratic equation for intersection points
        double discriminant = b * b - 4 * a * c;
        if (discriminant < 0) {
            return intersections;  // No intersection
        }

        // Calculate the two solutions (the points where the ray intersects the infinite cylinder)
        double t1 = (-b - Math.sqrt(discriminant)) / (2 * a);
        double t2 = (-b + Math.sqrt(discriminant)) / (2 * a);

        // Calculate the intersection points for t1 and t2
        Point p1 = ray.getHead().add(ray.getDirection().scale(t1));
        Point p2 = ray.getHead().add(ray.getDirection().scale(t2));

        // Check if the intersection points are within the height of the cylinder
        Vector p1ToAxis = p1.subtract(axis.getHead());
        Vector p2ToAxis = p2.subtract(axis.getHead());
        double projection1 = p1ToAxis.dotProduct(axisDirection);
        double projection2 = p2ToAxis.dotProduct(axisDirection);

        // Check if the points are within the valid height range of the cylinder
        if (projection1 >= 0 && projection1 <= height) {
            intersections.add(new Intersection(this,p1));
        }
        if (projection2 >= 0 && projection2 <= height) {
            intersections.add(new Intersection(this,p2));
        }
        if(intersections.size() == 0) {return null;}
        return intersections;
    }
}






