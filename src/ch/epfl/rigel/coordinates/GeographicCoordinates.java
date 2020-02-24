package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

public final class GeographicCoordinates extends SphericalCoordinates
{
    private static final RightOpenInterval lonInterval = RightOpenInterval.of( -180, 180 );
    private static final RightOpenInterval latInterval = RightOpenInterval.of( -90, 90 );


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
        return lonInterval.contains( lonDeg );
    }

    public static boolean isValidLatDeg( double latDeg )
    {
        return latInterval.contains( latDeg );
    }

    @Override
    public String toString()
    {
        return String.format( Locale.ROOT, "(lon=%.4f°, lat=%.4f°)", lonDeg(), latDeg() );
    }
}
