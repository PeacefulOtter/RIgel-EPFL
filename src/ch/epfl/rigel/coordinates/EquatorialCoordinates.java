package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

import java.util.Locale;

public final class EquatorialCoordinates extends SphericalCoordinates
{
    private EquatorialCoordinates( double ra, double dec )
    {
        super( ra, dec );
    }

    public static EquatorialCoordinates of( double ra, double dec )
    {
        if ( ra < 0 || ra >= Angle.TAU || dec < -Angle.TAU / 4 || dec > Angle.TAU / 4 )
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
