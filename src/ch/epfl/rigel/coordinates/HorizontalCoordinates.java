package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

public final class HorizontalCoordinates extends SphericalCoordinates
{
    // Interval of the longitude in radians
    private static final RightOpenInterval AZ_INTERVAL =  RightOpenInterval.of( 0, Angle.TAU );
    // Interval of the latitude in radians
    private static final ClosedInterval ALT_INTERVAL = ClosedInterval.of( -Math.PI / 2, Math.PI / 2 );

    private HorizontalCoordinates( double az, double alt )
    {
        super( az, alt );
    }

    /**
     * Creates an Horizontal Coordinate
     * @param az : azimuth
     * @param alt : altitude
     * @throws IllegalArgumentException if the azimuth or the altitude given is out of bound
     * @return the coordinates
     */
    public static HorizontalCoordinates of( double az, double alt )
    {
        Preconditions.checkInInterval(AZ_INTERVAL, az);
        Preconditions.checkInInterval(ALT_INTERVAL, alt);
        return new HorizontalCoordinates( az, alt );
    }

    /**
     * Creates an Horizontal Coordinate
     * @param azDeg : azimuth in degrees
     * @param altDeg : altitude in degrees
     * @throws IllegalArgumentException if the azimuth or the altitude given is out of bound
     * @return the coordinates
     */
    public static HorizontalCoordinates ofDeg( double azDeg, double altDeg )
    {
        double az = Angle.ofDeg( azDeg ), alt = Angle.ofDeg( altDeg );
        Preconditions.checkInInterval(AZ_INTERVAL, az);
        Preconditions.checkInInterval(ALT_INTERVAL, alt);
        return new HorizontalCoordinates( az, alt );
    }

    public double az() { return lon(); }

    public double azDeg() { return lonDeg(); }

    public double alt() { return lat(); }

    public double altDeg() { return latDeg(); }

    /**
     * @return the direction (N, E, S, W) depending on the longitude
     */
    public String azOctantName( String n, String e, String s, String w )
    {
        int octant = (int) Math.round( azDeg() / 45 );
        switch ( octant )
        {
            case 1: return n + e;
            case 2: return e;
            case 3: return s + e;
            case 4: return s;
            case 5: return s + w;
            case 6: return w;
            case 7: return n + w;
        }
        return n; // if octant = 0 or = 8
    }

    /**
     * Calculates the angular distance between two HorizontalCoordinates
     * @param that : the other coordinates
     * @return the angular distance
     */
    public double angularDistanceTo( HorizontalCoordinates that )
    {
        return Math.acos( Math.sin( that.alt() ) * Math.sin( this.alt() ) + Math.cos( that.alt() ) * Math.cos( this.alt() ) * Math.cos( that.az() - this.az() ) );
    }


    @Override
    public String toString()
    {
        return String.format( Locale.ROOT, "(az=%.4f°, alt=%.4f°)", azDeg(), altDeg() );
    }
}
