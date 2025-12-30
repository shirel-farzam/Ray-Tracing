package lighting;

import primitives.*;

/**
 * A point light source that radiates light in all directions from a given position.
 * Its intensity attenuates with distance using constant, linear, and quadratic factors.
 */
public class PointLight extends Light implements LightSource {
    private final Point position;
    private double kC = 1.0; // Constant attenuation factor
    private double kL = 0.0; // Linear attenuation factor
    private double kQ = 0.0; // Quadratic attenuation factor

    /**
     * Constructs a PointLight with the given intensity and position.
     *
     * @param intensity The base color/intensity of the light.
     * @param position  The position of the point light in space.
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }


    @Override
    public Vector getL(Point p) {
        if (p.equals(position)) {
            throw new IllegalArgumentException("The point cannot be equal to the position of the point light.");
        }
        return p.subtract(position).normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        // Compute attenuation based on distance and scale the light's color
        double d = getDistance(p);
        return intensity.scale(1 / (kC + kL * d + kQ * d * d));
    }

    /**
     * Sets the constant attenuation factor.
     *
     * @param kC Constant attenuation
     * @return This light for method chaining
     */
    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * Sets the linear attenuation factor.
     *
     * @param kL Linear attenuation
     * @return This light for method chaining
     */
    public PointLight setKl(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * Sets the quadratic attenuation factor.
     *
     * @param kQ Quadratic attenuation
     * @return This light for method chaining
     */
    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }


    @Override
    public double getDistance(Point p) {
        if (p.equals(position)) {
            throw new IllegalArgumentException("The point cannot be equal to the position of the point light.");
        }
        return p.distance(this.position); // assuming this.position exists
    }

}