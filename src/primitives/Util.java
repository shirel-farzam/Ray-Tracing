package primitives;

/**
 * Utility class that provides internal static methods and constants
 * for mathematical operations with controlled accuracy.
 * This class cannot be instantiated.
 *
 * @author Dan
 */
public final class Util {

    /**
     * The threshold exponent for considering a number as zero.
     * Equivalent to approximately 1e-12.
     */
    private static final int ACCURACY = -40;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Util() {
    }

    /**
     * Returns the exponent part of a given double number.
     *
     * @param num the number whose exponent is to be retrieved
     * @return the exponent part of the number
     */
    private static int getExp(double num) {
        return (int) ((Double.doubleToRawLongBits(num) >> 52) & 0x7FFL) - 1023;
    }

    /**
     * Checks whether a given number is zero or close enough to be considered zero.
     *
     * @param number the number to check
     * @return true if the number is almost zero, false otherwise
     */
    public static boolean isZero(double number) {
        return getExp(number) < ACCURACY;
    }

    /**
     * Aligns a number to zero if it is close enough.
     *
     * @param number the number to align
     * @return 0.0 if the number is almost zero, otherwise returns the number itself
     */
    public static double alignZero(double number) {
        return isZero(number) ? 0.0 : number;
    }

    /**
     * Checks whether two numbers have the same sign.
     *
     * @param n1 the first number
     * @param n2 the second number
     * @return true if both numbers have the same sign, false otherwise
     */
    public static boolean compareSign(double n1, double n2) {
        return (n1 < 0 && n2 < 0) || (n1 > 0 && n2 > 0);
    }

    /**
     * Generates a random real number in the specified range.
     *
     * @param min the lower bound (inclusive)
     * @param max the upper bound (exclusive)
     * @return a random number between min (inclusive) and max (exclusive)
     */
    public static double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }
}
