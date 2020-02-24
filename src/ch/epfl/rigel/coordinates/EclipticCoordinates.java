package ch.epfl.rigel.coordinates;

import java.util.Locale;
import ch.epfl.rigel.math.Angle;

public final class EclipticCoordinates extends SphericalCoordinates
{

    private EclipticCoordinates( double lon, double lat )
    {
        super( lon, lat );
    }

    public static EclipticCoordinates of( double lon, double lat )
    {
        if ( lon < 0 || lon >= Angle.TAU || lat < -Angle.TAU / 4 || lat > Angle.TAU / 4 )
        {
            throw new IllegalArgumentException();
        }        return new EclipticCoordinates( lon, lat );
    }

    @Override
    public String toString()
    {
        return String.format( Locale.ROOT, "(λ=%.4f°, β=%.4f°)", lonDeg(), latDeg() );
    }
}
