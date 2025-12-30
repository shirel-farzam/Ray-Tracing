package geometries;

//import lighting.LightSource;
import lighting.LightSource;
import primitives.Material;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Abstract base class for all intersectable geometry objects.
 * <p>
 * This class defines a public, final interface for intersection calculations
 * and delegates the actual implementation to a protected abstract method.
 * This follows the Non-Virtual Interface (NVI) pattern, allowing subclasses
 * to implement specific intersection logic while keeping the external interface consistent and controlled.
 * </p>
 */
public abstract class Intersectable {

    /**
     * Finds the intersection points between the given ray and the geometry.
     *
     * @param ray the ray to intersect with
     * @return a list of intersection points, or {@code null} if there are no intersections
     */
    public final List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null : list.stream().map(intersection -> intersection.point).toList();
    }

    /**
     * Calculates the intersections between the ray and the geometry.
     * <p>
     * This is the public method called by external users, which delegates to the internal helper method.
     * </p>
     *
     * @param ray the ray to intersect with
     * @return a list of {@link Intersection} objects, or {@code null} if there are no intersections
     */
    public final List<Intersection> calculateIntersections(Ray ray) {
        return calculateIntersectionsHelper(ray);
    }



    /**
     * Calculates the intersections between the ray and the geometry, limited to a maximum distance.
     * <p>
     * This method must be implemented by subclasses to define the geometry-specific intersection logic.
     * Called internally by the public methods in accordance with the Non-Virtual Interface (NVI) pattern.
     * </p>
     *
     * @param ray the ray to intersect with the geometry
     * @return a list of {@link Intersection} objects representing valid intersections within the distance limit,
     *         or {@code null} if there are no intersections
     */
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);


    /**
     * Represents an intersection between a ray and a geometry object.
     */
    public static class Intersection {
        /**
         * The geometry object that was intersected.
         */
        public Vector rayDirection;             // כיוון הקרן שפגעה בנקודה
        public Vector normalBeforeHit;          // הנורמל המקורי לפני בדיקת כיוונים
        public double rayNormalDotProduct;      // המכפלה הסקלרית בין הקרן לנורמל

        public LightSource lightSource;         // מקור האור הרלוונטי לחישוב הנוכחי
        public Vector lightDirection;           // כיוון האור מהנקודה למקור האור
        public double lightNormalDotProduct;    // dot(n, l) לחישובי אור

        public final Geometry geometry;

        /**
         * The point of intersection.
         */
        public final Point point;

        /**
         * The material of the intersected geometry.
         */
        public final Material material;

        /**
         * The normal vector at the point of intersection.
         * Can be set after intersection is constructed.
         */
        public Vector normal;

        /**
         * The direction vector of the viewing ray at the intersection.
         * Can be set for lighting/shading calculations.
         */
        public Vector v;

        /**
         * The dot product between the normal and v (used in lighting).
         */
        public double vNormal;

        /**
         * The light source used in lighting calculation.
         */
        public LightSource light;

//        /**
//         * The dot product between the normal and the light direction (used in lighting).
//         */
//        public double lNormal;
//

        /**
         * Constructs an intersection record with the specified geometry and point.
         *
         * @param geometry the intersected geometry
         * @param point    the point of intersection
         */
        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
            this.material = geometry != null ? geometry.getMaterial() : null;
        }

        @Override
        public String toString() {
            return "Intersection{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            return (obj instanceof Intersection other) &&
                    geometry == other.geometry &&
                    point.equals(other.point);
        }
    }
}