package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

public final class GeographicCoordinates extends SphericalCoordinates
{
    // Interval of the longitude in degrees
    private static final RightOpenInterval lonDegInterval = RightOpenInterval.of( -180, 180 );
    // Interval of the latitude in degrees
    private static final RightOpenInterval latDegInterval = RightOpenInterval.of( -90, 90 );


    private GeographicCoordinates( double lonDeg, double latDeg )
    {
        // store the longitude and latitude in radians
        super( Angle.ofDeg( lonDeg ), Angle.ofDeg( latDeg ) );
    }

    /**
     * Creates a Geographic Coordinate
     * @param lonDeg : longitude in degrees
     * @param latDeg : latitude in degrees
     * @throws IllegalArgumentException if the longitude or the latitude given is out of bound
     * @return the coordinate
     */
    public static GeographicCoordinates ofDeg( double lonDeg, double latDeg )
    {
        if ( !isValidLonDeg( lonDeg ) || !isValidLatDeg( latDeg ) )
        {
            throw new IllegalArgumentException( "Invalid longitude or latitude" );
        }
        return new GeographicCoordinates( lonDeg, latDeg );
    }

    /**
     * Check if the longitude is contained in the lonDegInterval
     * @param lonDeg : the longitude in degrees
     * @return true if contained, false if not
     */
    public static boolean isValidLonDeg( double lonDeg )
    {
        return lonDegInterval.contains( lonDeg );
    }

    /**
     * Check if the latitude is contained in the latDegInterval
     * @param latDeg : the latitude in degrees
     * @return true if contained, false if not
     */
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
