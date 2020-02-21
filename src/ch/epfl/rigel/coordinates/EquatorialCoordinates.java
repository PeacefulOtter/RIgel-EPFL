package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.platform.commons.util.ExceptionUtils;

import java.util.Locale;

public final class EquatorialCoordinates extends SphericalCoordinates
{
    private final double ra;
    private final double dec;

    private EquatorialCoordinates( double ra, double dec )
    {
        this.ra = ra;
        this.dec = dec;
    }

    public static EquatorialCoordinates of( double ra, double dec )
    {
        // throw new IllegalArgumentException
        return new EquatorialCoordinates( ra, dec );
    }

    public double ra()
    {
        return ra;
    }

    public double raDeg()
    {
        return ra;
    }

    public double raHr()
    {
        return ra;
    }

    public double dec()
    {
        return dec;
    }

    public double decDeg()
    {
        return dec;
    }

    @Override
    double lon()
    {
        return 0;
    }

    @Override
    double lonDeg()
    {
        return 0;
    }

    @Override
    double lat()
    {
        return 0;
    }

    @Override
    double latDeg()
    {
        return 0;
    }

    @Override
    public String toString()
    {
        return String.format( Locale.ROOT, "(ra=%.4f°, dec=%.4f°)", ra, dec );
    }

    //  Notez bien que l'ascension droite est exprimée en heures !  ????
}
