package ch.epfl.rigel.coordinates;

import java.util.Locale;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

public final class EclipticCoordinates extends SphericalCoordinates
{
    private static final RightOpenInterval lonInterval = RightOpenInterval.of( 0, Angle.TAU );
    private static final ClosedInterval latInterval = ClosedInterval.of( -Angle.TAU, Angle.TAU);

    private EclipticCoordinates( double lon, double lat )
    {
        super( lon, lat );
    }

    public static EclipticCoordinates of( double lon, double lat )
    {
        if ( !lonInterval.contains(lon) || !latInterval.contains(lat))
        {
            throw new IllegalArgumentException();
        }
        return new EclipticCoordinates( lon, lat );
    }

    @Override
    public String toString()
    {
        return String.format( Locale.ROOT, "(λ=%.4f°, β=%.4f°)", lonDeg(), latDeg() );
    }
}
