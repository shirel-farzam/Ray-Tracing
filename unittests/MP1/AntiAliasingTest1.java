package MP1;


import geometries.*;
import geometries.Box;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import renderer.Camera;
import renderer.RayTracerType;
import renderer.SimpleRayTracer;
import scene.Scene;

import static java.awt.Color.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import lighting.PointLight;
import org.junit.jupiter.api.Test;
import scene.Scene;

import javax.imageio.ImageWriter;
import javax.swing.*;
import java.util.List;

////
////public class Test1 {
////    @Test
////    void trianglesTransparentSphere() {
////
////        // --- Scene Setup for Anti-Aliasing Demonstration ---
////
////        Scene scene = new Scene("AntiAliasingDemo")
////                .setBackground(new Color(30, 30, 30))
////                .setAmbientLight(new AmbientLight(new Color(WHITE), 0.1));
////
////// --- Lights ---
////        scene.lights.add(new PointLight(new Color(500, 300, 300), new Point(-100, 100, 100)));
////        scene.lights.add(new DirectionalLight(new Color(300, 500, 300), new Vector(-1, -1, -1)));
////        scene.lights.add(new SpotLight(new Color(300, 300, 500), new Point(50, 100, 50), new Vector(-1, -2, -1)));
////
////// --- Geometries ---
////// Background wall
////        scene.geometries.add(new Polygon(
////                new Point(-200, -100, -100),
////                new Point(200, -100, -100),
////                new Point(200, 100, -100),
////                new Point(-200, 100, -100)
////        ).setEmission(new Color(20, 20, 20)).setMaterial(new Material().setKD(0.6)));
////
////// Floor
////        scene.geometries.add(new Polygon(
////                new Point(-200, -100, -101),
////                new Point(200, -100, -101),
////                new Point(200, -100, 100),
////                new Point(-200, -100, 100)
////        ).setEmission(new Color(40, 40, 40)).setMaterial(new Material().setKD(0.7)));
////
////// Sphere (center)
////        scene.geometries.add(new Sphere(new Point(0, -50, -50), 30)
////                .setEmission(new Color(0, 0, 255))
////                .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(50)));
////
////// Tilted Cubes (2 sides)
////        scene.geometries.add(new Polygon(
////                new Point(-60, -100, -60),
////                new Point(-20, -100, -60),
////                new Point(-20, -60, -60),
////                new Point(-60, -60, -60)
////        ).setEmission(new Color(255, 0, 0)).setMaterial(new Material().setKD(0.6)));
////
////        scene.geometries.add(new Polygon(
////                new Point(20, -100, -70),
////                new Point(60, -100, -70),
////                new Point(60, -60, -70),
////                new Point(20, -60, -70)
////        ).setEmission(new Color(0, 255, 0)).setMaterial(new Material().setKD(0.6)));
////
////// Triangle (top)
////        scene.geometries.add(new Triangle(
////                new Point(-30, -60, -30),
////                new Point(0, -20, -30),
////                new Point(30, -60, -30)
////        ).setEmission(new Color(255, 255, 0)).setMaterial(new Material().setKD(0.5)));
////
////// More shapes to complete 10 objects
////        scene.geometries.add(new Sphere(new Point(-100, -70, -50), 20)
////                .setEmission(new Color(255, 105, 180))
////                .setMaterial(new Material().setKD(0.7).setKS(0.2)));
////
////        scene.geometries.add(new Sphere(new Point(100, -70, -50), 20)
////                .setEmission(new Color(173, 216, 230))
////                .setMaterial(new Material().setKD(0.7).setKS(0.2)));
////
////        scene.geometries.add(new Polygon(
////                new Point(-100, -100, -30),
////                new Point(-60, -100, -30),
////                new Point(-60, -60, -30),
////                new Point(-100, -60, -30)
////        ).setEmission(new Color(0, 255, 255)).setMaterial(new Material().setKD(0.5)));
////    }
////}
//
//



    //    public void testAntiAliasingOnOff() {
//        // ----- Scene setup -----
//        Scene scene = new Scene("AntiAliasingDemo")
//                .setBackground(new Color(30, 30, 30))
//                .setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.1)));
//
//        // ----- Lights -----
//        scene.lights.add(new PointLight(new Color(500, 300, 300), new Point(-100, 100, 100)));
//        scene.lights.add(new DirectionalLight(new Color(300, 500, 300), new Vector(-1, -1, -1)));
//        scene.lights.add(new SpotLight(new Color(300, 300, 500), new Point(50, 100, 50), new Vector(-1, -2, -1)));
//
//        // ----- Geometries -----
//        scene.geometries.add(
//                new Polygon(new Point(-200, -100, -100), new Point(200, -100, -100),
//                        new Point(200, 100, -100), new Point(-200, 100, -100))
//                        .setEmission(new Color(20, 20, 20)).setMaterial(new Material().setKD(0.6)));
//
//        scene.geometries.add(
//                new Polygon(new Point(-200, -100, -101), new Point(200, -100, -101),
//                        new Point(200, -100, 100), new Point(-200, -100, 100))
//                        .setEmission(new Color(40, 40, 40)).setMaterial(new Material().setKD(0.7)));
//
//        scene.geometries.add(new Sphere(new Point(0, -50, -50), 30)
//                .setEmission(new Color(0, 0, 255))
//                .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(50)));
//
//        scene.geometries.add(
//                new Polygon(new Point(-60, -100, -60), new Point(-20, -100, -60),
//                        new Point(-20, -60, -60), new Point(-60, -60, -60))
//                        .setEmission(new Color(255, 0, 0)).setMaterial(new Material().setKD(0.6)));
//
//        scene.geometries.add(
//                new Polygon(new Point(20, -100, -70), new Point(60, -100, -70),
//                        new Point(60, -60, -70), new Point(20, -60, -70))
//                        .setEmission(new Color(0, 255, 0)).setMaterial(new Material().setKD(0.6)));
//
//        scene.geometries.add(new Triangle(new Point(-30, -60, -30), new Point(0, -20, -30), new Point(30, -60, -30))
//                .setEmission(new Color(255, 255, 0)).setMaterial(new Material().setKD(0.5)));
//
//        scene.geometries.add(new Sphere(new Point(-100, -70, -50), 20)
//                .setEmission(new Color(255, 105, 180))
//                .setMaterial(new Material().setKD(0.7).setKS(0.2)));
//
//        scene.geometries.add(new Sphere(new Point(100, -70, -50), 20)
//                .setEmission(new Color(173, 216, 230))
//                .setMaterial(new Material().setKD(0.7).setKS(0.2)));
//
//        scene.geometries.add(
//                new Polygon(new Point(-100, -100, -30), new Point(-60, -100, -30),
//                        new Point(-60, -60, -30), new Point(-100, -60, -30))
//                        .setEmission(new Color(0, 255, 255)).setMaterial(new Material().setKD(0.5)));
//
//        // ----- Camera setup -----
//        Camera camera = Camera.getBuilder()
//                .setLocation(new Point(0, 0, 1000))
//                .setDirection(Point.ZERO, new Vector(0, 0, -1))
//                .setVpSize(200, 200)
//                .setVpDistance(1000)
//                .setRayTracer(new SimpleRayTracer(scene))
//                .setAntiAliasing(false) // דולק או כבוי
//                .build();
//
//        // ----- Render without AntiAliasing -----
//        ImageWriter imageWriter1 = new ImageWriter("noAA", 400, 400);
//        camera.setImageWriter(imageWriter1);
//        long start1 = System.currentTimeMillis();
//        camera.renderImage();
//        long end1 = System.currentTimeMillis();
//        System.out.println("Render time without AA: " + (end1 - start1) + "ms");
//        camera.writeToImage();
//
//        // ----- Render with AntiAliasing -----
//        camera.setAntiAliasing(true);
//        ImageWriter imageWriter2 = new ImageWriter("withAA", 400, 400);
//        camera.setImageWriter(imageWriter2);
//        long start2 = System.currentTimeMillis();
//        camera.renderImage();
//        long end2 = System.currentTimeMillis();
//        System.out.println("Render time with AA: " + (end2 - start2) + "ms");
//        camera.writeToImage();
//    }
//}

public class AntiAliasingTest1 {

    private final Scene scene1 = new Scene("Test scene").setAmbientLight(new AmbientLight(new Color(38, 38, 38)));

    /**
     * First camera builder for some of tests
     */
    private final Camera.Builder camera1 = Camera.getBuilder()                                          //
            .setRayTracer(scene1, RayTracerType.SIMPLE)                                                                      //
            .setLocation(new Point(0, 0, 1000))                                                                              //
            .setDirection(Point.ZERO, Vector.AXIS_Y)                                                                         //
            .setVpSize(150, 150).setVpDistance(1000);


    @Test
    void complexSceneRecreation() {
        // הגדרת אור סביבתי עדין
        scene1.setAmbientLight(new AmbientLight(new Color(40, 40, 40)));

        // חומרים שונים לאובייקטים
        Material whiteMaterial = new Material()
                .setKD(new Double3(0.7, 0.7, 0.7))
                .setKS(new Double3(0.3, 0.3, 0.3))
                .setShininess(100);

        Material blueMaterial = new Material()
                .setKD(new Double3(0.1, 0.1, 0.8))
                .setKS(new Double3(0.5, 0.5, 0.5))
                .setShininess(200);

        Material redMaterial = new Material()
                .setKD(new Double3(0.8, 0.1, 0.1))
                .setKS(new Double3(0.4, 0.4, 0.4))
                .setShininess(150);

        Material greenMaterial = new Material()
                .setKD(new Double3(0.1, 0.8, 0.1))
                .setKS(new Double3(0.3, 0.3, 0.3))
                .setShininess(120);

        Material glassMaterial = new Material()
                .setKD(new Double3(0.1, 0.1, 0.1))
                .setKS(new Double3(0.9, 0.9, 0.9))
                .setShininess(300)
                .setKT(new Double3(0.8, 0.8, 0.8))
                .setKR(new Double3(0.2, 0.2, 0.2));

        Material coloredGlassMaterial = new Material()
                .setKD(new Double3(0.05, 0.3, 0.2))
                .setKS(new Double3(0.7, 0.7, 0.7))
                .setShininess(250)
                .setKT(new Double3(0.6, 0.9, 0.7))
                .setKR(new Double3(0.1, 0.1, 0.1));

        Material yellowGlassMaterial = new Material()
                .setKD(new Double3(0.4, 0.4, 0.05))
                .setKS(new Double3(0.7, 0.7, 0.7))
                .setShininess(250)
                .setKT(new Double3(0.9, 0.9, 0.3))
                .setKR(new Double3(0.1, 0.1, 0.1));

        Material reflectiveMaterial = new Material()
                .setKD(new Double3(0.2, 0.2, 0.2))
                .setKS(new Double3(0.8, 0.8, 0.8))
                .setShininess(400)
                .setKR(new Double3(0.7, 0.7, 0.7));

        // יצירת הרצפה (משטח גדול)
        scene1.geometries.add(
                new Plane(new Point(0, -50, 0), new Vector(0, 1, 0))
                        .setMaterial(new Material()
                                .setKD(new Double3(0.6, 0.6, 0.6))
                                .setKS(new Double3(0.2, 0.2, 0.2))
                                .setShininess(50)
                                .setKR(new Double3(0.1, 0.1, 0.1)))
        );

        // הקיר האחורי - רשת
        for (int i = -200; i <= 200; i += 20) {
            // קווים אנכיים
            scene1.geometries.add(
                    new Cylinder(400, new Ray(new Point(i, -50, -100), new Vector(0, 1, 0)), 1)
                            .setMaterial(new Material()
                                    .setKD(new Double3(0.1, 0.1, 0.1))
                                    .setKS(new Double3(0.1, 0.1, 0.1))
                                    .setShininess(30))
            );

            // קווים אופקיים
            scene1.geometries.add(
                    new Cylinder(400, new Ray(new Point(-200, i, -100), new Vector(1, 0, 0)), 1)
                            .setMaterial(new Material()
                                    .setKD(new Double3(0.1, 0.1, 0.1))
                                    .setKS(new Double3(0.1, 0.1, 0.1))
                                    .setShininess(30))
            );
        }

        // כדורים גדולים אפורים ברקע
        scene1.geometries.add(
                new Sphere(new Point(-80, -15, -60), 35)
                        .setMaterial(whiteMaterial)
        );

        scene1.geometries.add(
                new Sphere(new Point(20, -10, -70), 40)
                        .setMaterial(whiteMaterial)
        );

        scene1.geometries.add(
                new Sphere(new Point(100, -15, -50), 35)
                        .setMaterial(whiteMaterial)
        );

        // כדור כחול קטן
        scene1.geometries.add(
                new Sphere(new Point(-40, -35, -20), 15)
                        .setMaterial(blueMaterial)
        );

        // כדור ירוק
        scene1.geometries.add(
                new Sphere(new Point(60, -30, -30), 20)
                        .setMaterial(greenMaterial)
        );

        // כדור אדום גדול בצד
        scene1.geometries.add(
                new Sphere(new Point(120, -25, -10), 25)
                        .setMaterial(redMaterial)
        );

        // כדור זכוכית שקוף עם נקודות לבנות
        scene1.geometries.add(
                new Sphere(new Point(-100, -25, -10), 25)
                        .setMaterial(glassMaterial)
        );

        // נקודות לבנות בתוך הכדור השקוף
        for (int i = 0; i < 5; i++) {
            double angle = i * Math.PI * 2 / 5;
            double x = -100 + Math.cos(angle) * 15;
            double z = -10 + Math.sin(angle) * 15;
            scene1.geometries.add(
                    new Sphere(new Point(x, -25, z), 2)
                            .setMaterial(new Material()
                                    .setKD(new Double3(0.9, 0.9, 0.9))
                                    .setKS(new Double3(0.1, 0.1, 0.1))
                                    .setShininess(50))
            );
        }

        // קוביות צבעוניות זכוכית בחזית
        // קובייה כחולה-ירוקה
        scene1.geometries.add(
                new Box(new Point(-90, -50, 20), new Point(-70, -30, 40))
                        .setMaterial(coloredGlassMaterial)
        );

        // קובייה צהובה
        scene1.geometries.add(
                new Box(new Point(-50, -50, 15), new Point(-30, -30, 35))
                        .setMaterial(yellowGlassMaterial)
        );

        // קובייה ירוקה
        scene1.geometries.add(
                new Box(new Point(40, -50, 10), new Point(60, -30, 30))
                        .setMaterial(greenMaterial)
        );

        // קובייה אדומה
        scene1.geometries.add(
                new Box(new Point(70, -50, 15), new Point(90, -30, 35))
                        .setMaterial(redMaterial)
        );

        // פירמידות/משולשים לבנים
        scene1.geometries.add(
                new Triangle(new Point(-20, -50, 25), new Point(-10, -50, 35), new Point(-15, -30, 30))
                        .setMaterial(whiteMaterial)
        );

        scene1.geometries.add(
                new Triangle(new Point(10, -50, 20), new Point(20, -50, 30), new Point(15, -30, 25))
                        .setMaterial(whiteMaterial)
        );

        // כדור זכוכית רפלקטיבי עם אלמנטים בתוכו
        scene1.geometries.add(
                new Sphere(new Point(80, -20, -40), 30)
                        .setMaterial(new Material()
                                .setKD(new Double3(0.05, 0.05, 0.05))
                                .setKS(new Double3(0.9, 0.9, 0.9))
                                .setShininess(500)
                                .setKT(new Double3(0.7, 0.7, 0.7))
                                .setKR(new Double3(0.5, 0.5, 0.5)))
        );

        // אלמנטים קטנים בתוך הכדור הרפלקטיבי
        scene1.geometries.add(
                new Sphere(new Point(75, -20, -40), 3)
                        .setMaterial(redMaterial)
        );

        scene1.geometries.add(
                new Sphere(new Point(85, -20, -40), 3)
                        .setMaterial(greenMaterial)
        );

        // תאורה מרובת מקורות
        scene1.lights.addAll(List.of(
                // אור כיווני ראשי
                new DirectionalLight(new Color(300, 300, 300), new Vector(1, -1, -1)),

                // אור נקודתי עליון שמאל
                new PointLight(new Color(400, 350, 300), new Point(-100, 100, 50))
                        .setKl(0.0001).setKq(0.00005),

                // אור נקודתי עליון ימין
                new PointLight(new Color(350, 400, 350), new Point(100, 100, 50))
                        .setKl(0.0001).setKq(0.00005),

                // אור ממוקד על האובייקטים הקדמיים
                new SpotLight(new Color(500, 450, 400), new Point(0, 50, 100), new Vector(0, -1, -1))
                        .setKl(0.0002).setKq(0.0001).setBeamExponent(3),

                // אור רקע עדין
                new PointLight(new Color(150, 150, 200), new Point(0, 0, -150))
                        .setKl(0.001).setKq(0.0001)
        ));

        // הגדרת המצלמה לזווית אופטימלית
        Camera camera = Camera.getBuilder()
                .setRayTracer(scene1, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 30, 150))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
//                .setVpSize(200, 200)
//                .setVpDistance(150)
//                .setResolution(800, 800)
//                //.setMultithreading(true)
//                .build();
                .setVpSize(2000, 2000)
                .setVpDistance(150)
                .setResolution(800, 800)
                .setDebugPrint(0.1)
                .setASS(4)
                .setMultithreading(-2)
                .build();

        camera.renderImage()
                .writeToImage("complex_scene_recreation");
    }
}
