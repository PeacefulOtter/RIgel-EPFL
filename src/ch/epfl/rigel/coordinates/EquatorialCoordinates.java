package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

public final class EquatorialCoordinates extends SphericalCoordinates
{
    // Interval of the longitude
    private static final RightOpenInterval radInterval = RightOpenInterval.of( 0, Angle.TAU );
    // Interval of the latitude
    private static final ClosedInterval decInterval = ClosedInterval.of( -Angle.TAU/4, Angle.TAU/4);

    private EquatorialCoordinates( double ra, double dec )
    {
        super( ra, dec );
    }

    // methode of construction
    // throw exception if the interval not contains the values
    public static EquatorialCoordinates of( double ra, double dec )
    {
        if ( !radInterval.contains(ra) || !decInterval.contains(dec) )
        {
            throw new IllegalArgumentException();
        }
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

    public double raHr()
    {
        return Angle.toHr(lon());
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
