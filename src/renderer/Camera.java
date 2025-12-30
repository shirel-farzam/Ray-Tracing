package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;


/**
 * Represents a virtual camera in a 3D scene.
 * Defines camera orientation, position, and view plane parameters.
 */
public class Camera implements Cloneable {

    private boolean antiAliasing = false;
    private int samplesPerPixel = 81; // למשל 9x9

    private int amountOfRays_DOF = 1;// the number of rays in the grid for the depth of field
    private int amountOfRays_AA = 1; // the number of rays in the grid for the depth of field
    private double aperture = 0; // the radius of the circle of the camera
    private double depthOfField = 100; // the distance between the camera and the focus _focusPoint

    private int adaptiveSuperSamplingDepth = 0; // 0 - no adaptive super sampling, else - adaptive super sampling Depth times

    private int threadsCount = 0; // -2 auto, -1 range/stream, 0 no threads, 1+ number of threads
    private final int SPARE_THREADS = 2; // Spare threads if trying to use all the cores
    private double printInterval = 0; // printing progress percentage interval
    private PixelManager pixelManager; // Manages pixel processing in multi-threaded rendering



    private Vector vTo = null;         // Forward direction vector
    private Vector vUp = null;         // Upward direction vector
    private Vector vRight = null;      // Rightward direction vector
    private Point p0 = null;           // Camera position
    private Point pcenter = null;      // Center of the view plane
    private double distance = 0.0;     // Distance to the view plane
    private double width = 0.0;        // View plane width
    private double height = 0.0;       // View plane height

    private ImageWriter imageWriter;   // Responsible for writing the final image
    private RayTracerBase rayTracer;   // Ray tracing engine for calculating colors
    private int nX = 1;                // Image resolution width (default 1)
    private int nY = 1;                // Image resolution height (default 1)

    /**
     * Private constructor for use by the Builder only.
     */
    private Camera() {}

    /**
     * @return A new Builder instance for building a Camera.
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    public void setSamplesPerPixel(int samples) {
        this.samplesPerPixel = samples;
    }
    public void setAntiAliasing(boolean value) {
        this.antiAliasing = value;
    }
    /**
     * Constructs a ray from the camera to a specific pixel on the view plane.
     *
     * @param nX number of pixels in width
     * @param nY number of pixels in height
     * @param j  column index of pixel
     * @param i  row index of pixel
     * @return ray from camera to pixel (i,j)
     */
    public Ray constructRay(int nX, int nY, double j, double i) {
        double pixelWidth = width / nX;
        double pixelHeight = height / nY;

        double xJ = (j - (nX - 1) / 2.0) * pixelWidth;
        double yI = (i - (nY - 1) / 2.0) * pixelHeight;

        Point pIJ = pcenter;

        if (xJ != 0) {
            pIJ = pIJ.add(vRight.scale(xJ));
        }
        if (yI != 0) {
            pIJ = pIJ.add(vUp.scale(-yI));
        }

        return new Ray(p0, pIJ.subtract(p0));
    }

    /**
     * Renders the image using ray tracing.
     *
     * @return this Camera instance
     */
//    public Camera renderImage() {
//        for (int i = 0; i < nY; i++) {
//            for (int j = 0; j < nX; j++) {
//                castRay(j, i);
//            }
//        }
//        return this;
//    }
    /**
     * Render image using multi-threading by parallel streaming
     * @return the camera object itself
     */
    private Camera renderImageStream() {
        IntStream.range(0, nY).parallel()
                .forEach(i -> IntStream.range(0, nX).parallel()
                        .forEach(j -> castRay(j, i)));
        return this;
    }
    /**
     * Render image without multi-threading
     * @return the camera object itself
     */
    private Camera renderImageNoThreads() {
        for (int i = 0; i < nY; ++i)
            for (int j = 0; j < nX; ++j)
                castRay(j, i);
        return this;
    }
    /**
     * Render image using multi-threading by creating and running raw threads
     * @return the camera object itself
     */
    private Camera renderImageRawThreads() {
        var threads = new LinkedList<Thread>();
        while (threadsCount-- > 0)
            threads.add(new Thread(() -> {
                PixelManager.Pixel pixel;
                while ((pixel = pixelManager.nextPixel()) != null)
                    castRay(pixel.col(), pixel.row());
            }));
        for (var thread : threads) thread.start();
        try {
            for (var thread : threads) thread.join();
        } catch (InterruptedException ignored) {}
        return this;
    }

    /** This function renders image's pixel color map from the scene
     * included in the ray tracer object
     * @return the camera object itself
     */
    public Camera renderImage() {
        pixelManager = new PixelManager(nY, nX, printInterval);
        return switch (threadsCount) {
            case 0 -> renderImageNoThreads();
            case -1 -> renderImageStream();
            default -> renderImageRawThreads();
        };
    }




    /**
     * Casts a ray through the center of a given pixel, calculates its color, and writes it to the image.
     *
     * @param j pixel column index (X axis)
     * @param i pixel row index (Y axis)
     */
//    private void castRay(int j, int i) {
//        Ray ray = constructRay(nX, nY, j, i);
//        Color color = rayTracer.traceRay(ray);
//        imageWriter.writePixel(j, i, color);
//    }
    /**
     * Applies adaptive super sampling to a given pixel area by checking the colors at the four corners.
     * If all corners have the same color, returns it directly. Otherwise, subdivides the region recursively.
     *
     * @param row     the row index of the pixel (i)
     * @param col     the column index of the pixel (j)
     * @param depth   current recursion depth
     * @param minX    X coordinate of the left edge of the region
     * @param maxX    X coordinate of the right edge of the region
     * @param minY    Y coordinate of the top edge of the region
     * @param maxY    Y coordinate of the bottom edge of the region
     * @return        the computed color for the region
     */
    private Color adaptiveSuperSampling(int row, int col, int depth,
                                        double minX, double maxX, double minY, double maxY) {

        // Shoot ray through top-left corner
        Ray rayTL = constructRay(nX, nY, row + minY, col + minX);
        Color colorTL = rayTracer.traceRay(rayTL);

        // Stop if recursion limit reached
        if (depth >= adaptiveSuperSamplingDepth) {
            return colorTL;
        }

        // Top-right corner
        Ray rayTR = constructRay(nX, nY, row + minY, col + maxX);
        Color colorTR = rayTracer.traceRay(rayTR);
        if (!colorTR.iSimilar(colorTL)) {
            return subdivideAndAverage(row, col, depth, minX, maxX, minY, maxY);
        }

        // Bottom-left corner
        Ray rayBL = constructRay(nX, nY, row + maxY, col + minX);
        Color colorBL = rayTracer.traceRay(rayBL);
        if (!colorBL.iSimilar(colorTL)) {
            return subdivideAndAverage(row, col, depth, minX, maxX, minY, maxY);
        }

        // Bottom-right corner
        Ray rayBR = constructRay(nX, nY, row + maxY, col + maxX);
        Color colorBR = rayTracer.traceRay(rayBR);
        if (!colorBR.iSimilar(colorTL)) {
            return subdivideAndAverage(row, col, depth, minX, maxX, minY, maxY);
        }

        // All corners returned same color
        return colorTL;
    }

    /**
     * Subdivides the region into four smaller regions and averages their recursively computed colors.
     *
     * @param row     the pixel's row index
     * @param col     the pixel's column index
     * @param depth   current recursion depth
     * @param minX    left bound of region
     * @param maxX    right bound of region
     * @param minY    top bound of region
     * @param maxY    bottom bound of region
     * @return        averaged color from the 4 subregions
     */
    private Color subdivideAndAverage(int row, int col, int depth,
                                      double minX, double maxX, double minY, double maxY) {

        double midX = (minX + maxX) / 2.0;
        double midY = (minY + maxY) / 2.0;
        int nextDepth = depth + 1;

        Color topLeft     = adaptiveSuperSampling(row, col, nextDepth, minX, midX, minY, midY);
        Color topRight    = adaptiveSuperSampling(row, col, nextDepth, midX, maxX, minY, midY);
        Color bottomLeft  = adaptiveSuperSampling(row, col, nextDepth, minX, midX, midY, maxY);
        Color bottomRight = adaptiveSuperSampling(row, col, nextDepth, midX, maxX, midY, maxY);

        return topLeft.add(topRight, bottomLeft, bottomRight).reduce(4);
    }




    private void castRay(int j, int i) {
        Color color;
        if (adaptiveSuperSamplingDepth != 0){
            color = adaptiveSuperSampling(j,i,0,-0.5,0.5,-0.5,0.5);
        }
        else if (antiAliasing) {
            color = renderPixelWithAA(j, i);

        }
        else {
            Ray ray = constructRay(nX, nY, j, i);
            color = rayTracer.traceRay(ray);
        }
        imageWriter.writePixel(j, i, color);
        pixelManager.pixelDone();
    }
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    //---
    private Color renderPixelWithAA(int i, int j) {
        List<Ray> rays = constructBeam(i, j, samplesPerPixel); // פונקציה שתבני
        Color color = Color.BLACK;
        for (Ray ray : rays) {
            color = color.add(rayTracer.traceRay(ray));
        }
        return color.reduce(rays.size());
    }



    /**
     * Camera getter
     *
     * @return the aperture of the camera
     */
    public double getAperture() {
        return aperture;
    }

    /**
     * Camera getter
     *
     * @return the depth of field of the camera
     */
    public double getDepthOfField() {
        return depthOfField;
    }
    /**
     * Camera getter
     *
     * @return the number of rays in the grid for the depth of field
     */
    public int getAmountOfRaysDOF() {
        return amountOfRays_DOF;
    }

    /**
     * Camera getter
     *
     * @return the number of rays in the grid for the depth of field
     */
    public int getAmountOfRaysAA() {
        return amountOfRays_AA;
    }




    //        for (int xi = 0; xi < gridSize; xi++) {
//            for (int yi = 0; yi < gridSize; yi++) {
//                double dx = (xi + 0.5) / gridSize - 0.5;
//                double dy = (yi + 0.5) / gridSize - 0.5;
//
//                Point samplePoint = pixelCenter
//                        .add(vRight.scale(dx * pixelWidth))
//                        .add(vUp.scale(dy * pixelHeight));
//
//                rays.add(new Ray(p0, samplePoint.subtract(p0)));
//            }
//        }
//
//        return rays;
//    }


    //---
    private List<Ray> constructBeam(int j, int i, int samples) {
        if (samples <= 0)
            throw new IllegalArgumentException("samples must be > 0");

        int gridSize = (int) Math.sqrt(samples);
        if (gridSize * gridSize != samples)
            throw new IllegalArgumentException("samples must be a perfect square");

        List<Ray> rays = new ArrayList<>();

        double pixelWidth = width / nX;
        double pixelHeight = height / nY;

        // חישוב מדויק של מרכז הפיקסל
        double xJ = (j - (nX - 1) / 2.0) * pixelWidth;
        double yI = (i - (nY - 1) / 2.0) * pixelHeight;

        Point pixelCenter = pcenter
                .add(vRight.scale(xJ))
                .add(vUp.scale(-yI));

        for (int xi = 0; xi < gridSize; xi++) {
            for (int yi = 0; yi < gridSize; yi++) {
                double dx = (xi + 0.5) / gridSize - 0.5;
                double dy = (yi + 0.5) / gridSize - 0.5;


                Point samplePoint = pixelCenter;
                if(dx>0)
                    samplePoint = samplePoint.add(vRight.scale(dx * pixelWidth));
                if(dy >0)
                    samplePoint = samplePoint.add(vUp.scale(dy * pixelHeight));

                Vector dir = samplePoint.subtract(p0);
                if (dir.lengthSquared() == 0)
                    continue;

                rays.add(new Ray(p0, dir));
            }
        }

        // אם במקרה לא נוספה אף קרן - החזר אחת רגילה
        if (rays.isEmpty()) {
            Vector dir = pixelCenter.subtract(p0);
            if (dir.lengthSquared() != 0)
                rays.add(new Ray(p0, dir));
        }

        return rays;
    }

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    /**
     * Draws a grid on the image with the specified interval and color.
     * Only draws lines and leaves the rest of the image intact.
     *
     * @param interval the spacing between grid lines
     * @param color    the color of the grid lines
     * @return this Camera instance
     */
    public Camera printGrid(int interval, Color color) {
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                if (i % interval == 0 || j % interval == 0) {
                    imageWriter.writePixel(j, i, color);
                }
            }
        }
        return this;
    }

    /**
     * Writes the rendered image to a file with the given name (without extension).
     *
     * @param filename name of the image file (without extension)
     * @return this Camera instance
     */
    public Camera writeToImage(String filename) {
        try {
            java.lang.reflect.Field field = imageWriter.getClass().getDeclaredField("imageName");
            field.setAccessible(true);
            field.set(imageWriter, filename);
        } catch (Exception e) {
            throw new RuntimeException("Unable to set image filename", e);
        }
        imageWriter.writeToImage();
        return this;
    }

    // Getters
    public Vector getVTo()     { return vTo; }
    public Vector getVUp()     { return vUp; }
    public Vector getVRight()  { return vRight; }
    public Point getP0()       { return p0; }
    public Point getPcenter()  { return pcenter; }
    public double getDistance(){ return distance; }
    public double getWidth()   { return width; }
    public double getHeight()  { return height; }

    /**
     * Builder class for constructing Camera objects step-by-step.
     */
    public static class Builder {
        private final Camera camera = new Camera();
        private Point target = null;

        /**
         * Sets the direction vectors for the camera.
         * The vectors vTo and vUp must be orthogonal, otherwise an IllegalArgumentException will be thrown.
         *
         * @param vTo the forward direction vector of the camera.
         * @param vUp the upward direction vector of the camera.
         * @return the Builder instance to allow method chaining.
         * @throws IllegalArgumentException if the vectors are not orthogonal.
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            if (!isZero(vTo.dotProduct(vUp))) {
                throw new IllegalArgumentException("Direction vectors must be orthogonal");
            }
            this.target = null;
            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();
            camera.vRight = camera.vTo.crossProduct(camera.vUp);
            return this;
        }

        /**
         * Sets the target point for the camera's direction.
         *
         * @param target the point the camera will look at.
         * @return the Builder instance to allow method chaining.
         */
        public Builder setDirection(Point target) {
            this.target = target;
            camera.vTo = null;
            camera.vUp = null;
            camera.vRight = null;
            return this;
        }

        /**
         * Sets the target point and upward direction for the camera.
         *
         * @param target the point the camera will look at.
         * @param vUp the upward direction vector of the camera.
         * @return the Builder instance to allow method chaining.
         */
        public Builder setDirection(Point target, Vector vUp) {
            this.target = target;
            camera.vUp = vUp.normalize();
            camera.vTo = null;
            camera.vRight = null;
            return this;
        }

        /**
         * Sets the location of the camera.
         *
         * @param p0 the location of the camera.
         * @return the Builder instance to allow method chaining.
         */
        public Builder setLocation(Point p0) {
            camera.p0 = p0;
            return this;
        }
        //BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB
        //---
        public Builder setAntiAliasing(boolean value) {
            this.camera.antiAliasing = value;
            return this;
        }
        ///**
        //         * Set the number of rays in the grid for the anti-aliasing
        //         *
        //         * @param samples the number of rays in the grid
        //         * @return the camera builder
        //         */
        public Builder setSamplesPerPixel(int samples) {
            this.camera.samplesPerPixel = samples;
            return this;
        }


        /**
         * Set the amount of threads to use for rendering
         *
         * @param threads the amount of threads to use
         * @return the camera builder
         */
        public Builder setMultithreading(int threads) {
            if (threads < -2) throw new IllegalArgumentException("Multithreading must be -2 or higher");
            if (threads >= -1) camera.threadsCount = threads;
            else { // == -2
                int cores = Runtime.getRuntime().availableProcessors() - camera.SPARE_THREADS;
                camera.threadsCount = cores <= 2 ? 1 : cores;
            }
            return this;
        }

        /**
         * Set the interval for printing debug information
         *
         * @param interval the interval for printing debug information
         * @return the camera builder
         */
        public Builder setDebugPrint(double interval) {
            camera.printInterval = interval;
            return this;
        }

        /**
         * Sets the distance to the view plane.
         *
         * @param distance the distance from the camera to the view plane.
         * @return the Builder instance to allow method chaining.
         * @throws IllegalArgumentException if the distance is not positive.
         */

        public Builder setVpDistance(double distance) {
            if (alignZero(distance) <= 0) {
                throw new IllegalArgumentException("Distance must be positive");
            }
            camera.distance = distance;
            return this;
        }

        /**
         * Sets the size of the view plane (width and height).
         *
         * @param width the width of the view plane.
         * @param height the height of the view plane.
         * @return the Builder instance to allow method chaining.
         * @throws IllegalArgumentException if either the width or height is not positive.
         */
        public Builder setVpSize(double width, double height) {
            if (alignZero(width) <= 0 || alignZero(height) <= 0) {
                throw new IllegalArgumentException("Width and height must be positive");
            }
            camera.width = width;
            camera.height = height;
            return this;
        }

        /**
         * Sets the resolution of the camera.
         *
         * @param nX the number of pixels in the x-direction.
         * @param nY the number of pixels in the y-direction.
         * @return the Builder instance to allow method chaining.
         * @throws IllegalArgumentException if either nX or nY is not positive.
         */
        public Builder setResolution(int nX, int nY) {
            if (nX <= 0 || nY <= 0) {
                throw new IllegalArgumentException("Resolution values must be positive");
            }
            camera.nX = nX;
            camera.nY = nY;
            return this;
        }


        /**
         * Sets the ray tracer for the camera.
         *
         * @param scene the scene that will be rendered.
         * @param rayTracerType the type of the ray tracer to use (e.g., SIMPLE).
         * @return the Builder instance to allow method chaining.
         */
        public Builder setRayTracer(Scene scene, RayTracerType rayTracerType) {
            if (rayTracerType == RayTracerType.SIMPLE) {
                camera.rayTracer = new SimpleRayTracer(scene);
            } else {
                camera.rayTracer = null;
            }
            return this;
        }

        /**
         * Sets the Adaptive Super Sampling depth for the camera.
         *
         * @param depth the depth of adaptive super sampling (0 for no adaptive super sampling).
         * @return the Builder instance to allow method chaining.
         */
        public Builder setASS(int depth) {
            if (depth < 0) {
                throw new IllegalArgumentException("Adaptive Super Sampling depth must be non-negative");
            }
            camera.adaptiveSuperSamplingDepth = depth;
            return this;
        }

        /**
         * Builds the Camera instance after all necessary settings have been applied.
         *
         * @return the built Camera object.
         * @throws IllegalStateException if required fields are not set correctly.
         */
        public Camera build() {
            try {
                validate(camera);

                if (camera.nX <= 0 || camera.nY <= 0) {
                    throw new IllegalStateException("Resolution must be set and positive");
                }

                camera.imageWriter = new ImageWriter("default", camera.nX, camera.nY);

                if (camera.rayTracer == null) {
                    camera.rayTracer = new SimpleRayTracer(null);
                }

                return (Camera) camera.clone();

            } catch (CloneNotSupportedException ignored) {
                return null;
            }
        }

        /**
         * Validates the camera configuration to ensure all required fields are set correctly.
         *
         * @param camera the camera instance to validate.
         * @throws IllegalStateException if any required fields are not set or invalid.
         */
        private void validate(Camera camera) {
            if (camera.width == 0 || camera.height == 0) {
                throw new IllegalStateException("View plane size is not set");
            }

            if (camera.p0 == null) {
                camera.p0 = Point.ZERO;
            }

            if (camera.distance == 0.0) {
                throw new IllegalStateException("Distance to view plane is not set");
            }

            if (target != null && target.equals(camera.p0)) {
                throw new IllegalStateException("Camera cannot be at the target point");
            }

            if (target != null) {
                camera.vTo = target.subtract(camera.p0).normalize();
                if (camera.vUp == null) {
                    camera.vUp = Vector.AXIS_Y;
                }
            }

            if (camera.vTo == null) {
                camera.vTo = Vector.AXIS_Z;
            }

            if (camera.vUp == null) {
                camera.vUp = Vector.AXIS_Y;
            }

            if (!isOrthogonal(camera.vTo, camera.vUp)) {
                camera.vUp = camera.vTo.crossProduct(camera.vUp).crossProduct(camera.vTo).normalize();
            }

            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            camera.pcenter = camera.p0.add(camera.vTo.scale(camera.distance));
            target = null;
        }

        /**
         * Checks if two vectors are orthogonal.
         *
         * @param v1 the first vector.
         * @param v2 the second vector.
         * @return true if the vectors are orthogonal, false otherwise.
         */
        private boolean isOrthogonal(Vector v1, Vector v2) {
            return isZero(v1.dotProduct(v2));
        }

//        public Builder setMultithreading(boolean b) {
//        }
    }
}