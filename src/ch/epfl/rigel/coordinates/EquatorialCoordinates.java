package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

public final class EquatorialCoordinates extends SphericalCoordinates
{
    // Interval of the longitude
    private static final RightOpenInterval RA_INTERVAL = RightOpenInterval.of( 0, Angle.TAU );
    // Interval of the latitude
    private static final ClosedInterval DEC_INTERVAL = ClosedInterval.of( -Angle.TAU / 4, Angle.TAU / 4);

    private EquatorialCoordinates( double ra, double dec )
    {
        super( ra, dec );
    }

    /**
     * Creates an Equatorial Coordinate
     * @param ra : right ascension
     * @param dec : declination
     * @throws IllegalArgumentException if the right ascension or the declination given is out of bound
     * @return the coordinates
     */
    public static EquatorialCoordinates of( double ra, double dec )
    {
        Preconditions.checkInInterval(RA_INTERVAL, ra);
        Preconditions.checkInInterval(DEC_INTERVAL, dec);
        return new EquatorialCoordinates( ra, dec );
    }

    public double ra()
    {
        return lon();
    }

    public double raDeg()
    {
        return lonDeg();
    }

    // converts the right ascension to hours
    public double raHr()
    {
        return Angle.toHr( lon() );
    }

    public double dec()
    {
        return lat();
    }

    public double decDeg()
    {
        return latDeg();
    }


    @Override
    public String toString()
    {
        return String.format( Locale.ROOT, "(ra=%.4fh, dec=%.4f°)", raHr(), decDeg() );
    }
}
