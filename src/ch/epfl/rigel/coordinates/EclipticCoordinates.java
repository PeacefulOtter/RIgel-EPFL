package ch.epfl.rigel.coordinates;

import java.util.Locale;

public final class EclipticCoordinates extends SphericalCoordinates
{

    private EclipticCoordinates( double lon, double lat )
    {
        super( lon, lat );
    }

    public EclipticCoordinates of( double lon, double lat )
    {
        // throw new IllegalArgumentException
        return new EclipticCoordinates( lon, lat );
    }

    @Override
    public String toString()
    {
        return String.format( Locale.ROOT, "(λ=%.4f°, β=%.4f°)", lonDeg(), latDeg() );
    }
}
