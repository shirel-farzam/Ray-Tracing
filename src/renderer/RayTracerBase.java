package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Abstract base class for ray tracing engines.
 * Provides a framework for tracing rays and calculating their resulting color.
 */
public abstract class RayTracerBase {

    /**
     * The scene to be rendered by the ray tracer.
     * This field is protected to allow subclasses to access it.
     */
    protected final Scene scene;

    /**
     * Constructs a RayTracerBase with the given scene.
     *
     * @param scene the scene to render
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Traces a given ray through the scene and returns the calculated color.
     *
     * @param ray the ray to trace
     * @return the color resulting from tracing the ray
     */
    public abstract Color traceRay(Ray ray);
}
