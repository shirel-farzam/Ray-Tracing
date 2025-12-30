package geometries;

import primitives.Point;
import primitives.Ray;

import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Geometries extends Intersectable {
    List<Intersectable>geometries=new LinkedList<Intersectable>();

    public Geometries(Intersectable... geometries) {
        add (geometries);
    }

    public void add(Intersectable... geometries) {
        Collections.addAll(this.geometries, geometries);
    }
    /**
     * Removes all geometries from the collection.
     */
    public void clear() {
        geometries.clear();
    }

    public Geometries() {

    }

    /**
     * Finds all intersection points of a given ray with the geometries in the collection.
     *
     * @param ray the ray for which intersection points are being searched
     * @return a list of points where the ray intersects with the geometries,
     *         or null if there are no intersection points
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {

        List<Intersection> intersections = null;

        // Iterate through each geometry in the collection
        for (Intersectable geometry : geometries) {
            List<Intersection> geometryIntersections = geometry.calculateIntersectionsHelper(ray);

            // If the current geometry has intersection points
            if (geometryIntersections != null) {
                if (intersections == null)
                    intersections = new LinkedList<>(geometryIntersections);
                else
                    intersections.addAll(geometryIntersections);
            }
        }

        // Return the list of intersection points, or null if no intersections were found
        return intersections;
    }


}
