package geometries;

import primitives.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Box class represents a 3D rectangular box (cuboid) geometry
 */
public class Box extends Geometry {

    /** The minimum point of the box (corner with smallest coordinates) */
    private final Point minPoint;
    /** The maximum point of the box (corner with largest coordinates) */
    private final Point maxPoint;

    /** List of all 6 faces of the box as Polygon objects */
    private final List<Polygon> faces;

    /**
     * Constructor for Box
     * Creates a box from two opposite corner points
     *
     * @param p1 First corner point
     * @param p2 Opposite corner point
     */

    public Box(Point p1, Point p2) {
        // Calculate min and max points to ensure correct orientation
        double minX = Math.min(p1.getX(), p2.getX());
        double maxX = Math.max(p1.getX(), p2.getX());
        double minY = Math.min(p1.getY(), p2.getY());
        double maxY = Math.max(p1.getY(), p2.getY());
        double minZ = Math.min(p1.getZ(), p2.getZ());
        double maxZ = Math.max(p1.getZ(), p2.getZ());

        this.minPoint = new Point(minX, minY, minZ);
        this.maxPoint = new Point(maxX, maxY, maxZ);

        // Create all 8 vertices of the box
        Point v1 = new Point(minX, minY, minZ); // min corner
        Point v2 = new Point(maxX, minY, minZ);
        Point v3 = new Point(maxX, maxY, minZ);
        Point v4 = new Point(minX, maxY, minZ);
        Point v5 = new Point(minX, minY, maxZ);
        Point v6 = new Point(maxX, minY, maxZ);
        Point v7 = new Point(maxX, maxY, maxZ); // max corner
        Point v8 = new Point(minX, maxY, maxZ);

        // Create the 6 faces of the box as polygons
        faces = new ArrayList<>();

        // Front face (minZ)
        faces.add(new Polygon(v1, v2, v3, v4));

        // Back face (maxZ)
        faces.add(new Polygon(v5, v8, v7, v6));

        // Bottom face (minY)
        faces.add(new Polygon(v1, v5, v6, v2));

        // Top face (maxY)
        faces.add(new Polygon(v4, v3, v7, v8));

        // Left face (minX)
        faces.add(new Polygon(v1, v4, v8, v5));

        // Right face (maxX)
        faces.add(new Polygon(v2, v6, v7, v3));
    }

    /**
     * Gets the minimum point of the box
     * @return minimum point
     */
    public Point getMinPoint() {
        return minPoint;
    }

    /**
     * Gets the maximum point of the box
     * @return maximum point
     */
    public Point getMaxPoint() {
        return maxPoint;
    }

    /**
     * Gets all faces of the box
     * @return list of polygon faces
     */
    public List<Polygon> getFaces() {
        return new ArrayList<>(faces);
    }

    @Override
    public Vector getNormal(Point point) {
        // Find which face the point is on and return its normal
        double epsilon = 1e-10;

        // Check each face
        if (Math.abs(point.getZ() - minPoint.getZ()) < epsilon) {
            return new Vector(0, 0, -1); // Front face normal
        }
        if (Math.abs(point.getZ() - maxPoint.getZ()) < epsilon) {
            return new Vector(0, 0, 1); // Back face normal
        }
        if (Math.abs(point.getY() - minPoint.getY()) < epsilon) {
            return new Vector(0, -1, 0); // Bottom face normal
        }
        if (Math.abs(point.getY() - maxPoint.getY()) < epsilon) {
            return new Vector(0, 1, 0); // Top face normal
        }
        if (Math.abs(point.getX() - minPoint.getX()) < epsilon) {
            return new Vector(-1, 0, 0); // Left face normal
        }
        if (Math.abs(point.getX() - maxPoint.getX()) < epsilon) {
            return new Vector(1, 0, 0); // Right face normal
        }

        // If point is not exactly on a face, find the closest face
        return getClosestFaceNormal(point);
    }

    /**
     * Helper method to find normal of closest face when point is not exactly on a face
     */
    private Vector getClosestFaceNormal(Point point) {
        double[] distances = {
                Math.abs(point.getZ() - minPoint.getZ()), // Front
                Math.abs(point.getZ() - maxPoint.getZ()), // Back
                Math.abs(point.getY() - minPoint.getY()), // Bottom
                Math.abs(point.getY() - maxPoint.getY()), // Top
                Math.abs(point.getX() - minPoint.getX()), // Left
                Math.abs(point.getX() - maxPoint.getX())  // Right
        };

        Vector[] normals = {
                new Vector(0, 0, -1), // Front
                new Vector(0, 0, 1),  // Back
                new Vector(0, -1, 0), // Bottom
                new Vector(0, 1, 0),  // Top
                new Vector(-1, 0, 0), // Left
                new Vector(1, 0, 0)   // Right
        };

        int minIndex = 0;
        for (int i = 1; i < distances.length; i++) {
            if (distances[i] < distances[minIndex]) {
                minIndex = i;
            }
        }

        return normals[minIndex];
    }


    protected List<Point> findGeoIntersectionsHelper(Ray ray) {
        List<Point> intersections = new ArrayList<>();

        // Check intersection with each face
        for (Polygon face : faces) {
            List<Point> faceIntersections = face.findIntersections(ray);
            if (faceIntersections != null) {
                intersections.addAll(faceIntersections);
            }
        }

        return intersections.isEmpty() ? null : intersections;
    }

    @Override
    public Box setMaterial(Material material) {
        // Set material for all faces
        for (Polygon face : faces) {
            face.setMaterial(material);
        }
        return (Box) super.setMaterial(material);
    }

    @Override
    public Box setEmission(Color emission) {
        // Set emission for all faces
        for (Polygon face : faces) {
            face.setEmission(emission);
        }
        return (Box) super.setEmission(emission);
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        return List.of();
    }
}