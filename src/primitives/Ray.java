package primitives;

import java.util.List;
import java.util.Objects;
import geometries.Intersectable.Intersection;

/**
 * Represents a ray in 3D space defined by an origin point and a direction vector.
 */
public class Ray {

    /**
     * The starting point (origin) of the ray.
     */
    private final Point head;

    /**
     * The normalized direction vector of the ray.
     */
    private final Vector direction;

    /**
     * Constructs a ray with a given origin and direction.
     *
     * @param head      the origin point of the ray
     * @param direction the direction vector of the ray
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && this.head.equals(other.head)
                && this.direction.equals(other.direction);
    }

    @Override
    public String toString() {
        return "head: " + head + " , direction: " + direction;
    }

    public Vector getDirection() {
        return direction;
    }

    public Point getHead() {
        return head;
    }
    //The method calculates a point on the line of the beam,
//at a given distance from the beginning of the beam
    public Point getPoint(double t) {
        // if t is zero, return the head point
        if (Util.isZero(t))
            return head;
        return head.add(direction.scale(t));
    }

//    public Intersection findClosestIntersection(List<Intersection> intersections) {
//        if (intersections == null) {
//            return null;
//        }
//
//        Intersection closest = intersections.getFirst();
//        double minDistanceSquared = closest.point.distanceSquared(getHead());
//
//        for (var intersection : intersections) {
//            double distance = intersection.point.distanceSquared(getHead());
//            if (distance < minDistanceSquared) {
//                closest = intersection;
//                minDistanceSquared = distance;
//            }
//        }
//
//        return closest;
//    }



//    public Intersection findClosestIntersection(List<Intersection> intersections) {
//        if (intersections == null)
//            return null;
//        Intersection closestIntersection = null;
//        double minDistance = Double.POSITIVE_INFINITY;
//        for (Intersection intersection : intersections) {
//            double distance = intersection.point.distanceSquared(head);
//            if (distance < minDistance) {
//                minDistance = distance;
//                closestIntersection = intersection;
//            }
//        }
//        return closestIntersection;
//    }

    public Intersection findClosestIntersection(List<Intersection> intersectionList) {
        double minDistance, nowDistance;
        int index = 0;
        if (intersectionList == null) {
            return null;
        }

        minDistance = this.head.distance(intersectionList.getFirst().point);

        for (int i = 1; i < intersectionList.size(); i++) {
            nowDistance = this.head.distance(intersectionList.get(i).point);
            if (minDistance > nowDistance) {
                minDistance = nowDistance;
                index = i;
            }
        }
        return intersectionList.get(index);
    }


    public Point findClosestPoint(List<Point> points) {
        return points == null ? null
                : findClosestIntersection(points.stream().map(p -> new Intersection(null, p)).toList()).point;
    }

    public static final double DELTA = 0.1;
    /**
     * Constructs a ray with a small offset from the point in the direction of the normal.
     * Used to avoid self-intersection when casting secondary rays.
     *
     * @param p      the origin point of the ray
     * @param dir    the direction vector (assumed to be normalized)
     * @param normal the normal at the point (used to offset the start)
     */
    public Ray(Point p, Vector dir, Vector normal) {
        Vector delta = normal.scale(dir.dotProduct(normal) > 0 ? DELTA : -DELTA);
        this.head = p.add(delta);
        this.direction = dir.normalize();
    }


}
