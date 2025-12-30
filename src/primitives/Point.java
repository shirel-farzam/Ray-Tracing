package primitives;

/**
 * Represents a point in 3D space using three coordinates.
 */
public class Point {

    /**
     * The point at the origin (0, 0, 0).
     */
    public static final Point ZERO = new Point(Double3.ZERO);

    /**
     * The internal representation of the point as a 3D coordinate.
     */
    protected final Double3 xyz;

    /**
     * Constructs a point from three coordinate values.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     */
    public Point(double x, double y, double z) {
        xyz = new Double3(x, y, z);
    }

    /**
     * Constructs a point from a {@link Double3} object.
     *
     * @param xyz the 3D coordinate
     */
    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    /**
     * Returns a vector from the given point to this point.
     *
     * @param point the point to subtract
     * @return the resulting vector
     */
    public Vector subtract(Point point) {
        if (this.equals(point)) {
            throw new IllegalArgumentException("Cannot create a zero vector from two identical points");
        }
        return new Vector(xyz.subtract(point.xyz));
    }

    /**
     * Returns a new point by adding the given vector to this point.
     *
     * @param v1 the vector to add
     * @return the resulting point
     */
    public Point add(Vector v1) {
        return new Point(xyz.add(v1.xyz));
    }

    /**
     * Returns the Euclidean distance between this point and another point.
     *
     * @param point the other point
     * @return the distance
     */
    public double distance(Point point) {
        return Math.sqrt(distanceSquared(point));
    }

    /**
     * Returns the squared distance between this point and another point.
     *
     * @param point the other point
     * @return the squared distance
     */
    public double distanceSquared(Point point) {
        Double3 diff = this.xyz.subtract(point.xyz);
        Double3 squared = diff.product(diff);
        return squared.d1 + squared.d2 + squared.d3;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other)
                && this.xyz.equals(other.xyz);
    }

    @Override
    public String toString() {
        return "" + xyz;
    }

    public double getX() {
        return this.xyz.d1;
    }
    public double getY() {
        return this.xyz.d2;
    }
    public double getZ() {
        return this.xyz.d3;
    }
}
