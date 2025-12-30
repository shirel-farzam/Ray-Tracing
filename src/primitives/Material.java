package primitives;

/**
 * Class representing material properties for a geometry's surface.
 * <p>
 * Material parameters define how the surface interacts with light, such as
 * ambient reflection, diffuse reflection, specular reflection, and shininess.
 * </p>
 */
public class Material {

    /**
     * Ambient reflection coefficient.
     * Controls how much ambient light is reflected from the surface.
     */
    public Double3 kA = Double3.ONE;

    /**
     * Specular reflection coefficient.
     * Controls the strength of the specular highlight.
     */
    public Double3 kS = Double3.ZERO;

    /**
     * Diffuse reflection coefficient.
     * Controls how much diffuse light is scattered from the surface.
     */
    public Double3 kD = Double3.ZERO;
    /**
     * Transparency coefficient.
     * Controls how much light passes through the surface.
     */
    public Double3 kT = Double3.ZERO;  //שקיפות

    /**
     * Reflection coefficient.
     * Controls how much light is reflected like a mirror.
     */
    public Double3 kR = Double3.ZERO; //השתקפות

    /**
     * Shininess factor (Phong model).
     * Higher values produce smaller and sharper specular highlights.
     */

    public int nShininess = 0;

    /**
     * Sets the ambient reflection coefficient using a {@link Double3}.
     *
     * @param kA the ambient coefficient as a {@code Double3}
     * @return the current {@code Material} object for method chaining
     */
    public Material setKA(Double3 kA) {
        this.kA = kA;
        return this;
    }

    /**
     * Sets the ambient reflection coefficient using a single double value.
     * The same value is applied to all RGB components.
     *
     * @param kA the ambient coefficient value
     * @return the current {@code Material} object for method chaining
     */
    public Material setKA(double kA) {
        return setKA(new Double3(kA));
    }

    /**
     * Sets the specular reflection coefficient using a {@link Double3}.
     *
     * @param kS the specular coefficient as a {@code Double3}
     * @return the current {@code Material} object for method chaining
     */
    public Material setKS(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Sets the specular reflection coefficient using a single double value.
     * The same value is applied to all RGB components.
     *
     * @param kS the specular coefficient value
     * @return the current {@code Material} object for method chaining
     */
    public Material setKS(double kS) {
        return setKS(new Double3(kS));
    }

    /**
     * Sets the diffuse reflection coefficient using a {@link Double3}.
     *
     * @param kD the diffuse coefficient as a {@code Double3}
     * @return the current {@code Material} object for method chaining
     */
    public Material setKD(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Sets the diffuse reflection coefficient using a single double value.
     * The same value is applied to all RGB components.
     *
     * @param kD the diffuse coefficient value
     * @return the current {@code Material} object for method chaining
     */
    public Material setKD(double kD) {
        return setKD(new Double3(kD));
    }
    /**
     * Sets the transparency coefficient using a {@link Double3}.
     *
     * @param kT the transparency coefficient as a {@code Double3}
     * @return the current {@code Material} object for method chaining
     */
    public Material setKT(Double3 kT) {
        this.kT = kT;
        return this;
    }

    /**
     * Sets the transparency coefficient using a single double value.
     *
     * @param kT the transparency coefficient value
     * @return the current {@code Material} object for method chaining
     */
    public Material setKT(double kT) {
        return setKT(new Double3(kT));
    }

    /**
     * Sets the reflection coefficient using a {@link Double3}.
     *
     * @param kR the reflection coefficient as a {@code Double3}
     * @return the current {@code Material} object for method chaining
     */
    public Material setKR(Double3 kR) {
        this.kR = kR;
        return this;
    }

    /**
     * Sets the reflection coefficient using a single double value.
     *
     * @param kR the reflection coefficient value
     * @return the current {@code Material} object for method chaining
     */
    public Material setKR(double kR) {
        return setKR(new Double3(kR));
    }

    /**
     * Sets the shininess factor.
     *
     * @param nShininess the shininess value
     * @return the current {@code Material} object for method chaining
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }
}