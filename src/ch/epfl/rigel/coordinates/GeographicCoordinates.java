package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

import java.util.Locale;

public final class GeographicCoordinates extends SphericalCoordinates
{
    private final double lon;
    private final double lat;

    private GeographicCoordinates( double lon, double lat )
    {
        this.lon = lon;
        this.lat = lat;
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
        return Angle.ofDeg( lon );
    }

    @Override
    public double lonDeg()
    {
        return lon;
    }

    @Override
    public double lat()
    {
        return Angle.ofDeg( lat );
    }

    @Override
    public double latDeg()
    {
        return lat;
    }

    @Override
    public String toString()
    {
        return String.format( Locale.ROOT, "(lon=%.4f°, lat=%.4f°)", lon, lat );
    }
}
