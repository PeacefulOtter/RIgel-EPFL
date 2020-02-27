package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

public final class GeographicCoordinates extends SphericalCoordinates
{
    // Interval of longitude in degrees
    private static final RightOpenInterval lonDegInterval = RightOpenInterval.of( -180, 180 );
    // Interval of latitude in degrees
    private static final RightOpenInterval latDegInterval = RightOpenInterval.of( -90, 90 );


    private GeographicCoordinates( double lonDeg, double latDeg )
    {
        super( Angle.ofDeg( lonDeg ), Angle.ofDeg( latDeg ) );
    }

    // methode of construction
    // throw exception if the interval not contains the values
    public static GeographicCoordinates ofDeg( double lonDeg, double latDeg )
    {
        if ( !isValidLonDeg( lonDeg ) || !isValidLatDeg( latDeg ) )
        {
            throw new IllegalArgumentException( "Invalid longitude or latitude" );
        }
        return new GeographicCoordinates( lonDeg, latDeg );
    }

    // return if the values is contains by the interval
    public static boolean isValidLonDeg( double lonDeg )
    {
        return lonDegInterval.contains( lonDeg );
    }

    // return if the values is contains by the interval
    public static boolean isValidLatDeg( double latDeg )
    {
        return latDegInterval.contains( latDeg );
    }

    @Override
    public String toString()
    {
        return String.format( Locale.ROOT, "(lon=%.4f°, lat=%.4f°)", lonDeg(), latDeg() );
    }
}
