package renderer;

import geometries.*;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import static java.awt.Color.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class Bonus7 {
    private Point rotatePoint45(Point p) {
        double cos45 = Math.cos(Math.PI / 4); // ≈ 0.707
        double sin45 = Math.sin(Math.PI / 4); // ≈ 0.707

        double newX = p.getX() * cos45 - p.getY() * sin45;
        double newY = p.getX() * sin45 + p.getY() * cos45;

        return new Point(newX, newY, p.getZ());
    }
    /** Scene for the tests */
    private final Scene          scene         = new Scene("Test scene");
    /** Camera builder for the tests with triangles */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()     //
            .setRayTracer(scene, RayTracerType.SIMPLE);

    @Test
    void trianglesTransparentSphere() {

        // ---------------------
// ☁️ Sky Group – Middle
// ---------------------
        Geometry sky1 = new Sphere(new Point(0, 250, -20), 20d)  // middle-m
                .setEmission(new Color(135, 206, 235))
                .setMaterial(new Material().setKD(0.15).setKS(0.05).setKT(0.03).setShininess(10));

        Geometry sky2 = new Sphere(new Point(-20, 245, -20), 15d)  // middle-l
                .setEmission(new Color(176, 226, 255))
                .setMaterial(new Material().setKD(0.05).setKS(0.05).setKT(0.02).setShininess(10));

        Geometry sky3 = new Sphere(new Point(18, 245, -14), 18d)  // middle-r
                .setEmission(new Color(0, 191, 255))
                .setMaterial(new Material().setKD(0.05).setKS(0.05).setKT(0.8).setShininess(10));

// ---------------------
// ☁️ Sky Group – Left
// ---------------------
        Geometry sky4 = new Sphere(new Point(-170, 255, -70), 20d)  // left-m
                .setEmission(new Color(135, 206, 235))
                .setMaterial(new Material().setKD(0.15).setKS(0.05).setKT(0.04).setShininess(10));

        Geometry sky5 = new Sphere(new Point(-190, 250, -70), 15d)  // left-l
                .setEmission(new Color(176, 226, 255))
                .setMaterial(new Material().setKD(0.05).setKS(0.05).setKT(0.03).setShininess(10));

        Geometry sky6 = new Sphere(new Point(-150, 250, -64), 18d)  // left-r
                .setEmission(new Color(0, 191, 255))
                .setMaterial(new Material().setKD(0.05).setKS(0.05).setKT(0.7).setShininess(10));

// ---------------------
// ☁️ Sky Group – Right
// ---------------------
        Geometry sky7 = new Sphere(new Point(160, 255, 30), 20d)  // right-m
                .setEmission(new Color(135, 206, 235))
                .setMaterial(new Material().setKD(0.15).setKS(0.05).setKT(0.04).setShininess(10));

        Geometry sky8 = new Sphere(new Point(180, 250, 30), 15d)  // right-r
                .setEmission(new Color(176, 226, 255))
                .setMaterial(new Material().setKD(0.05).setKS(0.05).setKT(0.03).setShininess(10));

        Geometry sky9 = new Sphere(new Point(140, 250, 40), 18d)  // right-l
                .setEmission(new Color(0, 191, 255))
                .setMaterial(new Material().setKD(0.05).setKS(0.05).setKT(0.7).setShininess(10));

// ---------------------
// 🌿 Ground – Two Triangles (grass)
// ---------------------
        Geometry grass1 = new Triangle(
                new Point(-450, -450, -115),
                new Point(450, -450, -135),
                new Point(460, 75, -150))
                .setEmission(new Color(34, 139, 34))  // dark green for grass
                .setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(10));

        Geometry grass2 = new Triangle(
                new Point(-450, -450, -115),
                new Point(-450, 70, -140),
                new Point(460, 75, -150))
                .setEmission(new Color(34, 139, 34))  // dark green for grass
                .setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(10));

// ---------------------
// 📦 Add All Geometries to Scene
// ---------------------
        scene.geometries.add(
                sky1, sky2, sky3,
                sky4, sky5, sky6,
                sky7, sky8, sky9,
                grass1, grass2
        );


//// --- House Geometry Declarations ---
//        Geometry wall1 = new Polygon(
//                new Point(-60, -40, -40),
//                new Point(60, -40, -40),
//                new Point(60, -40, -240),
//                new Point(-60, -40, -240))
//                .setEmission(new Color(128, 0, 128))
//                .setMaterial(new Material().setKD(0.4).setKS(0.2).setShininess(20));
//
//        Geometry wall2 = new Polygon(
//                new Point(-60, 40, -40),
//                new Point(60, 40, -40),
//                new Point(60, 40, -240),
//                new Point(-60, 40, -240))
//                .setEmission(new Color(128, 0, 128))
//                .setMaterial(new Material().setKD(0.4).setKS(0.2).setShininess(20));
//
//        Geometry wall3 = new Polygon(
//                new Point(-60, -40, -40),
//                new Point(-60, 40, -40),
//                new Point(-60, 40, -240),
//                new Point(-60, -40, -240))
//                .setEmission(new Color(128, 0, 128))
//                .setMaterial(new Material().setKD(0.4).setKS(0.2).setShininess(20));
//
//        Geometry wall4 = new Polygon(
//                new Point(60, -40, -40),
//                new Point(60, 40, -40),
//                new Point(60, 40, -240),
//                new Point(60, -40, -240))
//                .setEmission(new Color(128, 0, 128))
//                .setMaterial(new Material().setKD(0.4).setKS(0.2).setShininess(20));
//
//        Geometry roof1 = new Triangle(
//                new Point(-60, -40, -40),
//                new Point(60, -40, -40),
//                new Point(0, 0, 0))
//                .setEmission(new Color(178, 34, 34))
//                .setMaterial(new Material().setKD(0.4).setKS(0.2).setShininess(30));
//
//        Geometry roof2 = new Triangle(
//                new Point(-60, 40, -40),
//                new Point(60, 40, -40),
//                new Point(0, 0, 0))
//                .setEmission(new Color(178, 34, 34))
//                .setMaterial(new Material().setKD(0.4).setKS(0.2).setShininess(30));
//
//        Geometry roof3 = new Triangle(
//                new Point(-60, -40, -40),
//                new Point(-60, 40, -40),
//                new Point(0, 0, 0))
//                .setEmission(new Color(178, 34, 34))
//                .setMaterial(new Material().setKD(0.4).setKS(0.2).setShininess(30));
//
//        Geometry roof4 = new Triangle(
//                new Point(60, -40, -40),
//                new Point(60, 40, -40),
//                new Point(0, 0, 0))
//                .setEmission(new Color(178, 34, 34))
//                .setMaterial(new Material().setKD(0.4).setKS(0.2).setShininess(30));
//
//// --- Add to Scene ---
//        scene.geometries.add(
//                wall1, wall2, wall3, wall4,
//                roof1, roof2, roof3, roof4
//        );

        // פונקציה לסיבוב נקודה ב-45 מעלות סביב ציר Z


// --- House Geometry Declarations (Rotated 45 degrees) ---
// הקירות לאחר סיבוב של 45 מעלות
        Geometry wall1 = new Polygon(
                rotatePoint45(new Point(-60, -40, -40)),
                rotatePoint45(new Point(60, -40, -40)),
                rotatePoint45(new Point(60, -40, -240)),
                rotatePoint45(new Point(-60, -40, -240)))
                .setEmission(new Color(128, 0, 128))
                .setMaterial(new Material().setKD(0.4).setKS(0.2).setShininess(20));

        Geometry wall2 = new Polygon(
                rotatePoint45(new Point(-60, 40, -40)),
                rotatePoint45(new Point(60, 40, -40)),
                rotatePoint45(new Point(60, 40, -240)),
                rotatePoint45(new Point(-60, 40, -240)))
                .setEmission(new Color(128, 0, 128))
                .setMaterial(new Material().setKD(0.4).setKS(0.2).setShininess(20));

        Geometry wall3 = new Polygon(
                rotatePoint45(new Point(-60, -40, -40)),
                rotatePoint45(new Point(-60, 40, -40)),
                rotatePoint45(new Point(-60, 40, -240)),
                rotatePoint45(new Point(-60, -40, -240)))
                .setEmission(new Color(128, 0, 128))
                .setMaterial(new Material().setKD(0.4).setKS(0.2).setShininess(20));

        Geometry wall4 = new Polygon(
                rotatePoint45(new Point(60, -40, -40)),
                rotatePoint45(new Point(60, 40, -40)),
                rotatePoint45(new Point(60, 40, -240)),
                rotatePoint45(new Point(60, -40, -240)))
                .setEmission(new Color(128, 0, 128))
                .setMaterial(new Material().setKD(0.4).setKS(0.2).setShininess(20));

// משולשי הגג לאחר סיבוב של 45 מעלות
        Geometry roof1 = new Triangle(
                rotatePoint45(new Point(-60, -40, -40)),
                rotatePoint45(new Point(60, -40, -40)),
                rotatePoint45(new Point(0, 0, 0)))
                .setEmission(new Color(178, 34, 34))
                .setMaterial(new Material().setKD(0.4).setKS(0.2).setShininess(30));

        Geometry roof2 = new Triangle(
                rotatePoint45(new Point(-60, 40, -40)),
                rotatePoint45(new Point(60, 40, -40)),
                rotatePoint45(new Point(0, 0, 0)))
                .setEmission(new Color(178, 34, 34))
                .setMaterial(new Material().setKD(0.4).setKS(0.2).setShininess(30));

        Geometry roof3 = new Triangle(
                rotatePoint45(new Point(-60, -40, -40)),
                rotatePoint45(new Point(-60, 40, -40)),
                rotatePoint45(new Point(0, 0, 0)))
                .setEmission(new Color(178, 34, 34))
                .setMaterial(new Material().setKD(0.4).setKS(0.2).setShininess(30));

        Geometry roof4 = new Triangle(
                rotatePoint45(new Point(60, -40, -40)),
                rotatePoint45(new Point(60, 40, -40)),
                rotatePoint45(new Point(0, 0, 0)))
                .setEmission(new Color(178, 34, 34))
                .setMaterial(new Material().setKD(0.4).setKS(0.2).setShininess(30));

// --- Add to Scene ---
        scene.geometries.add(
                wall1, wall2, wall3, wall4,
                roof1, roof2, roof3, roof4
        );






        scene.setAmbientLight(new AmbientLight(new Color(38, 38, 38)));
        scene.setBackground(new Color(135, 206, 235));
        scene.lights.add(new SpotLight(
                        new Color(300, 200, 200),               // עוצמת אור
                        new Point(60, 50, 0),                   // מיקום האור
                        new Vector(0, 0, -1))                   // מאיר למטה
                .setKl(4E-5).setKq(2E-7));



        cameraBuilder
//                .setLocation(new Point(0, 0, 2300)) //
//                .setDirection(Point.ZERO, new Vector(0, 1, -0.5))   //Vector.AXIS_Y) //
//                .setLocation(new Point(0, 0, 2300)) // מאחורי הבית ובגובה בינוני
//                .setDirection(new Point(0, 0, 0), new Vector(0, 1, 0)) // אלכסון כלפי מעלה

                .setLocation(new Point(0, -1600, 1500)) // יותר נמוך
                .setDirection(new Point(-5, 0, -1), new Vector(0, 2.5, 1.3))


                .setVpDistance(1000).setVpSize(200, 200) //
                .setResolution(600, 600) //
                .build() //
                .renderImage() //
                .writeToImage("try1");
    }

//    @Test
//    void houseSceneWithFloorAndGlassSphere() {
//        Scene scene = new Scene("Fancy Test Scene");
//
//        // צבע רקע תכלת בהיר (שמיים)
//        scene.setBackground(new Color(135, 206, 235));
//
//        // משטח רצפה אינסופי בירוק־בהיר
//        Geometry floor = new Plane(
//                new Point(0, 0, -120),    // הרצפה נמצאת מתחת לשאר האובייקטים
//                new Vector(0, 0, 1)       // נורמל פונה כלפי מעלה
//        ).setEmission(new Color(60, 180, 75))          // ירוק עשב
//                .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(10));
//
//        // שני משולשים תלת־ממדיים בצורת V פתוח
//        Geometry triangle1 = new Triangle(
//                new Point(-150, -150, -90),
//                new Point(150, -150, -90),
//                new Point(75, 75, -110)
//        ).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60));
//
//        Geometry triangle2 = new Triangle(
//                new Point(-150, -150, -90),
//                new Point(-70, 70, -110),
//                new Point(75, 75, -110)
//        ).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60));
//
//        // ספרה שקופה כחולה – בין שני המשולשים
//        Geometry glassSphere = new Sphere(new Point(30, 30, -60), 30d)
//                .setEmission(new Color(BLUE))
//                .setMaterial(new Material()
//                        .setKD(0.2).setKS(0.2).setShininess(30)
//                        .setKT(0.6)); // שקיפות 60%
//
//        // הוספת גאומטריות לסצנה
//        scene.geometries.add(floor, triangle1, triangle2, glassSphere);
//
//        // תאורת רקע (ambient light) כללית
//        scene.setAmbientLight(new AmbientLight(new Color(38, 38, 38)));
//
//        // אור ספוט שמאיר כלפי מטה
//        scene.lights.add(new SpotLight(
//                new Color(300, 200, 200),               // עוצמת אור
//                new Point(60, 50, 0),                   // מיקום האור
//                new Vector(0, 0, -1))                   // מאיר למטה
//                .setKl(4E-5).setKq(2E-7));
//
//        // מצלמה – בזווית שמביטה ישירות קדימה
//        Camera.Builder cameraBuilder = Camera.getBuilder()
//                .setLocation(new Point(0, 0, 1000))             // מאחור
//                .setDirection(new Point(0, 0, -100), Vector.AXIS_Y) // מביטה לעבר עומק הסצנה
//                .setVpDistance(1000)
//                .setVpSize(200, 200)
//                .setRayTracer(scene, RayTracerType.SIMPLE)
//                .setResolution(600, 600);
//
//        // בניית מצלמה, רינדור וכתיבה לתמונה
//        cameraBuilder.build()
//                .renderImage()
//                .writeToImage("try 2");
//    }


}