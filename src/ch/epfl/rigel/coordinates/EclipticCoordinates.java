package ch.epfl.rigel.coordinates;

import java.util.Locale;

public final class EclipticCoordinates extends SphericalCoordinates
{
    private final double lon;
    private final double lat;

    private EclipticCoordinates( double lon, double lat )
    {
        this.lon = lon;
        this.lat = lat;
    }

    public EclipticCoordinates of( double lon, double lat )
    {
        // throw new IllegalArgumentException
        return new EclipticCoordinates( lon, lat );
    }

    @Override
    public double lon()
    {
        return 0;
    }

    @Override
    public double lonDeg()
    {
        return 0;
    }

    @Override
    public double lat()
    {
        return 0;
    }

    @Override
    public double latDeg()
    {
        return 0;
    }

    @Override
    public String toString()
    {
        return String.format( Locale.ROOT, "(λ=%.4f°, β=%.4f°)", lon, lat );
    }
}
