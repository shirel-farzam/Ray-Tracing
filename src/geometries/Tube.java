package geometries;

import primitives.Ray;
import primitives.Point;
import primitives.Vector;

import java.util.List;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Represents an infinite tube in 3D space defined by a central axis and a radius.
 */
public class Tube extends RadialGeometry {

    /**
     * The central axis of the tube.
     */
    protected final Ray axis;

    /**
     * Constructs a Tube with a given axis and radius.
     *
     * @param axis   the central axis of the tube
     * @param radius the radius of the tube
     */
    public Tube(Ray axis, double radius) {
        super(radius);
        this.axis = axis;
    }

    /**
     * This method calculates the normal vector to a point on the surface of the tube.
     * It first calculates the projection of the point onto the axis of the tube and then
     * returns the normalized vector from the center of the tube to the given point.
     *
     * @param point the point on the surface of the tube where the normal vector is calculated.
     * @return the normalized vector pointing from the center of the tube to the given point.
     */
    @Override
    public Vector getNormal(Point point) {
        //calculate the projection of the point on the axis
        Vector pminusP0 = point.subtract(this.axis.getPoint(0d));
        double t = alignZero(this.axis.getDirection().dotProduct(pminusP0));

        //find center of the tube
        //return the normalized vector from the center of the tube to the point
        return point.subtract(this.axis.getPoint(t)).normalize();
    }

    /**
     * This method is used to calculate intersections between the ray and the tube.
     * Currently, it is not implemented and returns null.
     *
     * @param ray the ray to check for intersections with the tube.
     * @return null, as the method is not yet implemented.
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        return null;
    }

    public List<Point> findIntersectionsHelper(Ray ray) {

        Point p0 = ray.getHead();
        Vector v = ray.getDirection();
        Point pa = this.axis.getPoint(0);
        Vector va = this.axis.getDirection();

        double a, b, c; //coefficients for quadratic equation ax^2 + bx + c

        // a = (v-(v,va)va)^2
        // b = 2(v-(v,va)va,delP-(delP,va)va)
        // c = (delP-(delP,va)va)^2 - r^2
        // delP = p0-pa

        //note: (v,u) = v dot product u = vu = v*u

        //Step 1 - calculates a:
        Vector vecA = v;
        try {
            double vva = v.dotProduct(va); //(v,va)
            if (!isZero(vva)) vecA = v.subtract(va.scale(vva)); //v-(v,va)va
            a = vecA.lengthSquared(); //(v-(v,va)va)^2
        } catch (IllegalArgumentException e) {
            return null; //if a=0 there are no intersections because Ray is parallel to axisRay
        }

        //Step 2 - calculates deltaP (delP), b, c:
        try {
            Vector deltaP = p0.subtract(pa); //p0-pa
            Vector deltaPMinusDeltaPVaVa = deltaP;
            double deltaPVa = deltaP.dotProduct(va); //(delP,va)va)
            if (!isZero(deltaPVa)) deltaPMinusDeltaPVaVa = deltaP.subtract(va.scale(deltaPVa)); //(delP-(delP,va)va)
            b = 2 * (vecA.dotProduct(deltaPMinusDeltaPVaVa)); //2(v-(v,va)va,delP-(delP,va)va)
            c = deltaPMinusDeltaPVaVa.lengthSquared() - this.radius * this.radius; //(delP-(delP,va)va)^2 - r^2
        } catch (IllegalArgumentException e) {
            b = 0; //if delP = 0, or (delP-(delP,va)va = (0, 0, 0)
            c = -1 * this.radius * this.radius;
        }

        //Step 3 - solving the quadratic equation: ax^2 + bx + c = 0
        double discriminator = alignZero(b * b - 4 * a * c); //discriminator: b^2 - 4ac
        if (discriminator <= 0) return null; //there are no intersections because Ray is parallel to axisRay

        //the solutions for the equation: (-b +- discriminator) / 2a
        double sqrtDiscriminator = Math.sqrt(discriminator);
        double t1 = alignZero(-b + sqrtDiscriminator) / (2 * a);
        double t2 = alignZero(-b - sqrtDiscriminator) / (2 * a);

//        //if t1 or t2 are bigger than maxDistance they wll be set to negative value
//        if (alignZero(t1 - maxDistance) > 0) t1 = -1;
//        if (alignZero(t2 - maxDistance) > 0) t2 = -1;

        //takes all positive solutions
        if (t1 > 0 && t2 > 0)
            return List.of(ray.getPoint(t1), ray.getPoint(t2));
        if (t1 > 0) return List.of(ray.getPoint(t1));
        if (t2 > 0) return List.of(ray.getPoint(t2));

        return null; //if there are no positive solutions
    }

}


