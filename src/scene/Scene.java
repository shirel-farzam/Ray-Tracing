package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;
import primitives.Point;

import java.util.LinkedList;
import java.util.List;


public class Scene {
    public String sceneName;
    public Color background = Color.BLACK;
    public AmbientLight ambientLight = AmbientLight.NONE; //העוצמה של התאורה הסביבתית
    public Geometries geometries = new Geometries();
    public List<LightSource> lights=new LinkedList<>();

    public Scene(String sceneName) {
        this.sceneName = sceneName;
    }
    /**
     * Sets the scene lights
     * @param lights the lights
     * @return this scene
     */
    public Scene setLights(List<LightSource> lights) {
        if(lights != null)
            this.lights = lights;
        return this;
    }
    /**
     * Sets the background color for the scene.
     *
     * @param color the color to set as the background of the scene.
     * @return the Scene instance to allow method chaining.
     */
    public Scene setBackground(Color color) {
        this.background = color;
        return this;
    }

    /**
     * Sets the ambient light for the scene.
     *
     * @param ambientLight the ambient light to set for the scene.
     * @return the Scene instance to allow method chaining.
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Sets the geometries (objects) in the scene.
     *
     * @param geometries the geometries to set for the scene.
     * @return the Scene instance to allow method chaining.
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }

    /**
     * Returns the background color of the scene.
     *
     * @return the background color
     */
    public Color getBackground() {
        return background;
    }


    /**
     * @return the ambient light of the scene
     */
    public AmbientLight getAmbientLight() {
        return ambientLight;
    }

    /**
     * @return the collection of geometries in the scene
     */
    public Geometries getGeometries() {
        return geometries;
    }

}
