package lighting;

import primitives.*;

import static primitives.Util.alignZero;

/**
 * A spotlight is a {@link PointLight} with a specific direction.
 * <p>
 * The intensity is modulated by the angle between the direction of the spotlight
 * and the vector from the light's position to the target point.
 * <p>
 * The spotlight can also be concentrated into a narrower beam by setting a beam exponent.
 */
public class SpotLight extends PointLight {
    /**
     * The normalized direction vector the spotlight is shining in (d_L).
     */
    private final Vector direction;

    /**
     * Controls the concentration of the beam.
     * A higher value creates a narrower and more focused beam.
     * Default is 1.0.
     */
    private double beamExponent = 1.0;

    /**
     * Constructs a {@code SpotLight} with the given intensity, position, and direction.
     *
     * @param intensity the base color/intensity of the light
     * @param position  the position of the spotlight (P_L)
     * @param direction the direction in which the light is shining (d_L), will be normalized
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    /**
     * Sets the beam exponent to control the narrowness of the spotlight.
     * A higher exponent results in a sharper, more focused beam.
     *
     * @param beamExponent the exponent value (should be >= 0); default is 1.0
     * @return this {@code SpotLight} instance (for method chaining)
     */
    public SpotLight setBeamExponent(double beamExponent) {
        if (beamExponent < 0) {
            this.beamExponent = 1.0;
        } else {
            this.beamExponent = beamExponent;
        }
        return this;
    }

    @Override
    public SpotLight setKc(double kC) {
        super.setKc(kC);
        return this;
    }

    @Override
    public SpotLight setKl(double kL) {
        super.setKl(kL);
        return this;
    }

    @Override
    public SpotLight setKq(double kQ) {
        super.setKq(kQ);
        return this;
    }


    @Override
    public Color getIntensity(Point p)
    {
        Color pointIntensity = super.getIntensity(p);
        Vector lightToPoint = getL(p);
        double cosAlpha = direction.dotProduct(lightToPoint);
        if (alignZero(cosAlpha)<=0)
        {
            return Color.BLACK;
        }

       double exp=1.0;
       if(beamExponent!=1.0) {
       cosAlpha=alignZero(Math.pow(cosAlpha, beamExponent));
       }
        return pointIntensity.scale(cosAlpha);
    }
}

