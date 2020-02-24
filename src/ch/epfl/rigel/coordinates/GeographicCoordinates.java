package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

import java.util.Locale;

public final class GeographicCoordinates extends SphericalCoordinates
{
    private GeographicCoordinates( double lonDeg, double latDeg )
    {
        super( Angle.ofDeg( lonDeg ), Angle.ofDeg( latDeg ) );
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
    public String toString()
    {
        return String.format( Locale.ROOT, "(lon=%.4f°, lat=%.4f°)", lonDeg(), latDeg() );
    }
}
