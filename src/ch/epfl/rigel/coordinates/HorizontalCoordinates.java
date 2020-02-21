package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

import java.util.Locale;

public final class HorizontalCoordinates extends SphericalCoordinates
{
    private final double az;
    private final double alt;

    private HorizontalCoordinates( double az, double alt )
    {
        this.az = az;
        this.alt = alt;
    }

    public static HorizontalCoordinates of( double az, double alt )
    {
        // throw new IllegalArgumentException
        // TO CHECK
        return new HorizontalCoordinates( az, alt );
    }

    public static HorizontalCoordinates of( double azDeg, double altDeg )
    {
        // throw new IllegalArgumentException
        // TO CHECK
        return new HorizontalCoordinates( Angle.ofDeg( azDeg ), Angle.ofDeg( altDeg ) );
    }

    public double az()
    {
        return 0;
    }

    public double azDeg()
    {
        return 0;
    }

    public String azOctantName( String n, String e, String z, String w )
    {
        return "";
    }

    public double alt()
    {
        return 0;
    }

    public double altDeg()
    {
        return 0;
    }

    public double angularDistanceTo( HorizontalCoordinates that )
    {
        return 0;
    }

    @Override
    double lon()
    {
        return 0;
    }

    @Override
    double lonDeg()
    {
        return 0;
    }

    @Override
    double lat()
    {
        return 0;
    }

    @Override
    double latDeg()
    {
        return 0;
    }

    @Override
    public String toString()
    {
        return String.format( Locale.ROOT, "(az=%.4f°, alt=%.4f°)", az, alt );
    }
}
