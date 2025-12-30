package MP1;

import org.junit.jupiter.api.Test;
import java.util.List;

// Assuming your package structure and classes are correctly imported
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import primitives.*;
import renderer.Camera;
import renderer.RayTracerType;
import scene.Scene;
import geometries.*; // Ensure all geometry classes are imported
import static java.lang.StrictMath.PI;

public class AntiAliasingTest2 {

    // Scene and Camera.Builder can remain as they are for now,
    // as we'll build a new camera within the test method.
    private final Scene scene1 = new Scene("Test scene").setAmbientLight(new AmbientLight(new Color(38, 38, 38)));

    /**
     * First camera builder for some of tests (not used in complexSceneRecreation now)
     */
    private final Camera.Builder camera1 = Camera.getBuilder()
            .setRayTracer(scene1, RayTracerType.SIMPLE)
            .setLocation(new Point(0, 0, 1000))
            .setDirection(new Vector(0, 0, -1), Vector.AXIS_Y)
            .setVpSize(150, 150).setVpDistance(1000);


    @Test
    void complexSceneRecreation() {
        // Clear all existing geometries from the scene (assuming a fresh start for this test)
        scene1.geometries.clear();
        scene1.lights.clear();

        // Ambient light - slightly brighter than before for overall illumination
        scene1.setAmbientLight(new AmbientLight(new Color(50, 50, 50)));

        // --- Materials ---
        // General diffuse white material for walls/pedestals
        Material diffuseWhite = new Material().setKD(new Double3(0.8, 0.8, 0.8)).setKS(new Double3(0.1, 0.1, 0.1)).setShininess(10);
        // General diffuse gray material
        Material diffuseGray = new Material().setKD(new Double3(0.6, 0.6, 0.6)).setKS(new Double3(0.1, 0.1, 0.1)).setShininess(10);
        // Diffuse black for checkerboard dark squares
        Material diffuseBlack = new Material().setKD(new Double3(0.1, 0.1, 0.1)).setKS(new Double3(0.05, 0.05, 0.05)).setShininess(5);


        // For the glossy white sphere
        Material glossyWhite = new Material().setKD(new Double3(0.7, 0.7, 0.7)).setKS(new Double3(0.6, 0.6, 0.6)).setShininess(150);

        // For the reflective (chrome) sphere
        Material chromeMaterial = new Material().setKD(new Double3(0.05, 0.05, 0.05)) // Very low diffuse
                .setKS(new Double3(0.9, 0.9, 0.9))   // High specular
                .setShininess(500)                    // Very high shininess for sharp reflections
                .setKR(new Double3(0.9, 0.9, 0.9));  // High reflection coefficient

        // Diffuse colored materials for small spheres
        Material blueMaterial = new Material().setKD(new Double3(0.1, 0.1, 0.8)).setKS(new Double3(0.1, 0.1, 0.1)).setShininess(30);
        Material greenMaterial = new Material().setKD(new Double3(0.1, 0.8, 0.1)).setKS(new Double3(0.1, 0.1, 0.1)).setShininess(30);
        Material redMaterial = new Material().setKD(new Double3(0.8, 0.1, 0.1)).setKS(new Double3(0.1, 0.1, 0.1)).setShininess(30);

        // Transparent materials (glass-like)
        Material clearGlass = new Material().setKD(new Double3(0.01, 0.01, 0.01)) // Very low diffuse
                .setKS(new Double3(0.9, 0.9, 0.9))    // High specular
                .setShininess(300)                     // High shininess
                .setKT(new Double3(0.9, 0.9, 0.9))     // High transparency
                .setKR(new Double3(0.1, 0.1, 0.1));   // Some reflection

        Material blueGlass = new Material().setKD(new Double3(0.0, 0.0, 0.0)) // Almost no diffuse
                .setKS(new Double3(0.5, 0.5, 0.5))    // Specular
                .setShininess(200)
                .setKT(new Double3(0.3, 0.3, 0.8))     // Blue tint transparency
                .setKR(new Double3(0.05, 0.05, 0.05)); // Slight reflection

        Material greenGlass = new Material().setKD(new Double3(0.0, 0.0, 0.0))
                .setKS(new Double3(0.5, 0.5, 0.5))
                .setShininess(200)
                .setKT(new Double3(0.3, 0.8, 0.3)) // Green tint transparency
                .setKR(new Double3(0.05, 0.05, 0.05));

        Material yellowGlass = new Material().setKD(new Double3(0.0, 0.0, 0.0))
                .setKS(new Double3(0.5, 0.5, 0.5))
                .setShininess(200)
                .setKT(new Double3(0.8, 0.8, 0.3)) // Yellow tint transparency
                .setKR(new Double3(0.05, 0.05, 0.05));

        Material magentaGlass = new Material().setKD(new Double3(0.0, 0.0, 0.0))
                .setKS(new Double3(0.5, 0.5, 0.5))
                .setShininess(200)
                .setKT(new Double3(0.8, 0.3, 0.8)) // Magenta tint transparency
                .setKR(new Double3(0.05, 0.05, 0.05));

        // --- Cornell Box Setup ---
        // Dimensions of the box
        double boxSize = 100;
        double halfBox = boxSize / 2;

        // Floor (Y=-halfBox)
        scene1.geometries.add(
                new Plane(new Point(0, -halfBox, 0), new Vector(0, 1, 0))
                        .setMaterial(diffuseWhite) // Base material for floor
        );

        // Ceiling (Y=halfBox)
        scene1.geometries.add(
                new Plane(new Point(0, halfBox, 0), new Vector(0, -1, 0))
                        .setMaterial(diffuseWhite)
        );

        // Back Wall (Z=-halfBox)
        scene1.geometries.add(
                new Plane(new Point(0, 0, -halfBox), new Vector(0, 0, 1))
                        .setMaterial(diffuseWhite) // Base material for back wall
        );

        // Right Wall (X=halfBox)
        scene1.geometries.add(
                new Plane(new Point(halfBox, 0, 0), new Vector(-1, 0, 0))
                        .setMaterial(diffuseWhite)
        );

        // Left Wall (X=-halfBox)
        scene1.geometries.add(
                new Plane(new Point(-halfBox, 0, 0), new Vector(1, 0, 0))
                        .setMaterial(diffuseWhite)
        );

        // --- Checkerboard pattern on Floor and Back Wall ---
        // This is a simplified approach. For a true checkerboard, you might need a texture mapping
        // system or to subdivide the plane into many small squares.
        // Assuming small squares for now as a workaround for no texture support.
        // If your Plane constructor can take bounds or you have a Rectangle/Quad primitive, use that.
        // For simplicity, I'll use a dense grid of small white/black planes on top of the base wall/floor.
        // This will be very inefficient. A better solution would be a procedural texture in your Material.

        double checkerSize = 10; // Size of each checker square
        int numChecks = (int) (boxSize / checkerSize);

        // Floor checkerboard (on top of the base floor plane)
        for (int i = 0; i < numChecks; i++) {
            for (int j = 0; j < numChecks; j++) {
                Material currentMat = ((i + j) % 2 == 0) ? diffuseWhite : diffuseBlack;
                // These points define a square on the Y=-halfBox plane
                Point p1 = new Point(-halfBox + i * checkerSize, -halfBox + 0.1, -halfBox + j * checkerSize);
                Point p2 = new Point(-halfBox + (i + 1) * checkerSize, -halfBox + 0.1, -halfBox + (j + 1) * checkerSize);
                // A Box primitive can represent a thin square if height is small
                // Note: The Box primitive in your code seems to take two points, assuming min/max corners.
                // We'll create thin boxes slightly above the floor/wall.
                scene1.geometries.add(
                        new Box(p1, new Point(p2.getX(), -halfBox + 0.2, p2.getZ()))
                                .setMaterial(currentMat)
                );
            }
        }

        // Back Wall checkerboard (on top of the base back wall plane)
        for (int i = 0; i < numChecks; i++) {
            for (int j = 0; j < numChecks; j++) {
                Material currentMat = ((i + j) % 2 == 0) ? diffuseWhite : diffuseBlack;
                // These points define a square on the Z=-halfBox plane
                Point p1 = new Point(-halfBox + i * checkerSize, -halfBox + j * checkerSize, -halfBox + 0.1);
                Point p2 = new Point(-halfBox + (i + 1) * checkerSize, -halfBox + (j + 1) * checkerSize, -halfBox + 0.2);
                scene1.geometries.add(
                        new Box(p1, p2)
                                .setMaterial(currentMat)
                );
            }
        }


        // --- Objects within the scene ---
        double pedestalHeight = 10;
        double pedestalSize = 25;
        double sphereRadiusLarge = 20;
        double sphereRadiusSmall = 10;
        double cubeSize = 15;

        // Pedestal 1 (Left, for transparent sphere)
        scene1.geometries.add(
                new Box(new Point(-40 - pedestalSize / 2, -halfBox, 10 - pedestalSize / 2), new Point(-40 + pedestalSize / 2, -halfBox + pedestalHeight, 10 + pedestalSize / 2))
                        .setMaterial(diffuseWhite)
        );
        // Transparent sphere
        scene1.geometries.add(
                new Sphere(new Point(-40, -halfBox + pedestalHeight + sphereRadiusSmall, 10), sphereRadiusSmall)
                        .setMaterial(clearGlass)
        );

        // Pedestal 2 (Center-Left, for diffuse white sphere)
        scene1.geometries.add(
                new Box(new Point(-pedestalSize / 2, -halfBox, 20 - pedestalSize / 2), new Point(pedestalSize / 2, -halfBox + pedestalHeight, 20 + pedestalSize / 2))
                        .setMaterial(diffuseWhite)
        );
        // Diffuse white sphere
        scene1.geometries.add(
                new Sphere(new Point(0, -halfBox + pedestalHeight + sphereRadiusLarge, 20), sphereRadiusLarge)
                        .setMaterial(diffuseWhite)
        );

        // Pedestal 3 (Center-Right, for glossy white sphere)
        scene1.geometries.add(
                new Box(new Point(40 - pedestalSize / 2, -halfBox, 10 - pedestalSize / 2), new Point(40 + pedestalSize / 2, -halfBox + pedestalHeight, 10 + pedestalSize / 2))
                        .setMaterial(diffuseWhite)
        );
        // Glossy white sphere
        scene1.geometries.add(
                new Sphere(new Point(40, -halfBox + pedestalHeight + sphereRadiusLarge, 10), sphereRadiusLarge)
                        .setMaterial(glossyWhite)
        );

        // Pedestal 4 (Far right, for reflective sphere)
        scene1.geometries.add(
                new Box(new Point(80 - pedestalSize / 2, -halfBox, 0 - pedestalSize / 2), new Point(80 + pedestalSize / 2, -halfBox + pedestalHeight, 0 + pedestalSize / 2))
                        .setMaterial(diffuseWhite)
        );
        // Reflective (chrome) sphere
        scene1.geometries.add(
                new Sphere(new Point(80, -halfBox + pedestalHeight + sphereRadiusLarge, 0), sphereRadiusLarge)
                        .setMaterial(chromeMaterial)
        );

        // Small colored spheres on the floor
        scene1.geometries.add(
                new Sphere(new Point(-20, -halfBox + sphereRadiusSmall, 50), sphereRadiusSmall)
                        .setMaterial(blueMaterial)
        );
        scene1.geometries.add(
                new Sphere(new Point(60, -halfBox + sphereRadiusSmall, 50), sphereRadiusSmall)
                        .setMaterial(greenMaterial)
        );
        scene1.geometries.add(
                new Sphere(new Point(100, -halfBox + sphereRadiusSmall, 50), sphereRadiusSmall)
                        .setMaterial(redMaterial)
        );

        // Transparent colored cubes on pedestals (these pedestals are smaller)
        double smallPedestalHeight = 5;
        double smallPedestalSize = 10;
        double cubeHalfSize = cubeSize / 2;

        // Yellow transparent cube
        scene1.geometries.add(
                new Box(new Point(-60 - smallPedestalSize / 2, -halfBox, 40 - smallPedestalSize / 2), new Point(-60 + smallPedestalSize / 2, -halfBox + smallPedestalHeight, 40 + smallPedestalSize / 2))
                        .setMaterial(diffuseWhite)
        );
        scene1.geometries.add(
                new Box(new Point(-60 - cubeHalfSize, -halfBox + smallPedestalHeight, 40 - cubeHalfSize), new Point(-60 + cubeHalfSize, -halfBox + smallPedestalHeight + cubeSize, 40 + cubeHalfSize))
                        .setMaterial(yellowGlass)
        );

        // Green transparent cube
        scene1.geometries.add(
                new Box(new Point(-40 - smallPedestalSize / 2, -halfBox, 50 - smallPedestalSize / 2), new Point(-40 + smallPedestalSize / 2, -halfBox + smallPedestalHeight, 50 + smallPedestalSize / 2))
                        .setMaterial(diffuseWhite)
        );
        scene1.geometries.add(
                new Box(new Point(-40 - cubeHalfSize, -halfBox + smallPedestalHeight, 50 - cubeHalfSize), new Point(-40 + cubeHalfSize, -halfBox + smallPedestalHeight + cubeSize, 50 + cubeHalfSize))
                        .setMaterial(greenGlass)
        );

        // Blue transparent cube
        scene1.geometries.add(
                new Box(new Point(-20 - smallPedestalSize / 2, -halfBox, 40 - smallPedestalSize / 2), new Point(-20 + smallPedestalSize / 2, -halfBox + smallPedestalHeight, 40 + smallPedestalSize / 2))
                        .setMaterial(diffuseWhite)
        );
        scene1.geometries.add(
                new Box(new Point(-20 - cubeHalfSize, -halfBox + smallPedestalHeight, 40 - cubeHalfSize), new Point(-20 + cubeHalfSize, -halfBox + smallPedestalHeight + cubeSize, 40 + cubeHalfSize))
                        .setMaterial(blueGlass)
        );

        // Magenta transparent cube
        scene1.geometries.add(
                new Box(new Point(0 - smallPedestalSize / 2, -halfBox, 50 - smallPedestalSize / 2), new Point(0 + smallPedestalSize / 2, -halfBox + smallPedestalHeight, 50 + smallPedestalSize / 2))
                        .setMaterial(diffuseWhite)
        );
        scene1.geometries.add(
                new Box(new Point(0 - cubeHalfSize, -halfBox + smallPedestalHeight, 50 - cubeHalfSize), new Point(0 + cubeHalfSize, -halfBox + smallPedestalHeight + cubeSize, 50 + cubeHalfSize))
                        .setMaterial(magentaGlass)
        );

        // Scattered gray rectangular prisms/planes on the floor
        // Use Box to represent thick planes or cuboids
        scene1.geometries.add(
                new Box(new Point(-70, -halfBox, -10), new Point(-50, -halfBox + 5, 0))
                        .setMaterial(diffuseGray)
        );
        scene1.geometries.add(
                new Box(new Point(-50, -halfBox, 0), new Point(-30, -halfBox + 5, 10))
                        .setMaterial(diffuseGray)
        );
        scene1.geometries.add(
                new Box(new Point(-10, -halfBox, -20), new Point(10, -halfBox + 5, -10))
                        .setMaterial(diffuseGray)
        );
        scene1.geometries.add(
                new Box(new Point(20, -halfBox, -5), new Point(40, -halfBox + 5, 5))
                        .setMaterial(diffuseGray)
        );
        scene1.geometries.add(
                new Box(new Point(50, -halfBox, -20), new Point(70, -halfBox + 5, -10))
                        .setMaterial(diffuseGray)
        );

        // Large gray vertical rectangular prism on the far right
        scene1.geometries.add(
                new Box(new Point(halfBox - 20, -halfBox, -halfBox / 2), new Point(halfBox, halfBox, halfBox / 2))
                        .setMaterial(diffuseGray)
        );

        // --- Lighting ---
        // Adjust lights to mimic the desired image's illumination
        scene1.lights.addAll(List.of(
                // Main light from top-front-right, slightly yellowish to give warmth
                new PointLight(new Color(600, 550, 500), new Point(halfBox * 0.7, halfBox * 0.9, halfBox * 0.7))
                        .setKl(0.00001).setKq(0.000005), // Low attenuation for soft falloff

                // Secondary fill light from top-front-left, slightly bluish
                new PointLight(new Color(400, 400, 450), new Point(-halfBox * 0.7, halfBox * 0.8, halfBox * 0.7))
                        .setKl(0.00002).setKq(0.00001),

                // Directional light for overall ambient direction, very subtle
                new DirectionalLight(new Color(50, 50, 50), new Vector(0, -1, -0.5))
        ));


        // --- Camera Setup ---
        // This will be crucial for the correct perspective.
        // Needs to be outside the box, looking in.
        Camera camera = Camera.getBuilder()
                .setRayTracer(scene1, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 30, boxSize * 1.5)) // Slightly elevated, further back to see the whole box
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0)) // Looking slightly down into the box
                .setVpSize(200, 200) // Viewport size
                .setVpDistance(200) // Distance to viewport
                .setResolution(800, 800) // Output image resolution
                .setAntiAliasing(true) // Assuming this method exists for anti-aliasing
                .setSamplesPerPixel(9) // Number of rays per pixel for anti-aliasing (e.g., 3x3 grid)
                //.setMultithreading(true) // Enable if available and beneficial for performance
                .build();

        camera.renderImage()
                .writeToImage("complex_scene_recreation_desired_look"); // New file name
    }
}