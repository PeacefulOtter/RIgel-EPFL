package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

public final class Angle
{
    // The TAU constant
    public static final double TAU = 2 * Math.PI;
    // One hour per radian
    private static final double HR_PER_RAD = 24 / TAU;
    // One radian per hour
    private static final double RAD_PER_HR = TAU / 24;

    /**
     * Reduce an angle in radian to [0,τ[
     * @param rad : the angle in radian
     * @return the corresponding angle inside the interval
     */
    public static double normalizePositive( double rad )
    {
        return Math.abs( rad % TAU );
    }

    /**
     * Converts an angle given in seconds to an angle in radians
     * @param sec : the angle in seconds
     * @return a radian angle
     */
    public static double ofArcsec( double sec )
    {
        return sec * ( TAU / (3600*360) );
    }


    /**
     * Converts an angle in deg° min' sec" to an angle in radian
     * @param deg : degrees
     * @param min : minutes
     * @param sec : seconds
     * @throws IllegalArgumentException if the minutes or seconds are not between 0 (included) and 60 (excluded)
     * @return the corresponding angle in radian
     */
    public static double ofDMS( int deg, int min, double sec )
    {
        Preconditions.checkArgument( deg >= 0 && min >= 0 && sec >= 0 );
        if ( min < 0 || min >= 60 || sec < 0 || sec >= 60 )
        {
            throw new IllegalArgumentException();
        }
        return ofDeg( deg ) + ofArcsec( min * 60 + sec );
    }


    /**
     * Converts an angle given in degrees to radian
     * @param deg : angle in degree
     * @return the angle in radian
     */
    public static double ofDeg( double deg )
    {
        return Math.toRadians( deg );
    }

    /**
     * Converts an angle given in radian to degrees
     * @param rad : angle in radian
     * @return the angle in degree
     */
    public static double toDeg( double rad )
    {
        return Math.toDegrees( rad );
    }

    /**
     * Converts an angle in hour to radian
     * @param hr : angle in hr
     * @return the angle in radian
     */
    public static double ofHr( double hr )
    {
        return hr * RAD_PER_HR;
    }

    /**
     * Converts an angle in radian to hour
     * @param rad : angle in radian
     * @return the angle in hr
     */
    public static double toHr( double rad )
    {
        return rad * HR_PER_RAD;
    }
}
