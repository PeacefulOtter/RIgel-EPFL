package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

import java.util.Locale;

public final class GeographicCoordinates extends SphericalCoordinates
{
    private final double lonDeg;
    private final double latDeg;

    private GeographicCoordinates( double lon, double lat )
    {
        this.lonDeg = lon;
        this.latDeg = lat;
    }

    public static GeographicCoordinates ofDeg( double lonDeg, double latDeg )
    {
        if ( !isValidLonDeg( lonDeg ) || !isValidLatDeg( latDeg ) )
        {
            throw new IllegalArgumentException();
        }
        return new GeographicCoordinates( lonDeg, latDeg );
    }

    public static boolean isValidLonDeg( double lonDeg )
    {
        return !( lonDeg < -180 || lonDeg >= 180 );
    }

    public static boolean isValidLatDeg( double latDeg )
    {
        return !( latDeg < 0 || latDeg >= 360 );
    }

    @Override
    public double lon()
    {
        return Angle.ofDeg( lonDeg );
    }

    @Override
    public double lonDeg()
    {
        return lonDeg;
    }

    @Override
    public double lat()
    {
        return Angle.ofDeg( latDeg );
    }

    @Override
    public double latDeg()
    {
        return latDeg;
    }

    @Override
    public String toString()
    {
        return String.format( Locale.ROOT, "(lon=%.4f°, lat=%.4f°)", lonDeg, latDeg );
    }
}
