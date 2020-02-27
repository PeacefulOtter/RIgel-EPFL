package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

public final class EclipticCoordinates extends SphericalCoordinates
{
    // Interval of the longitude
    private static final RightOpenInterval lonInterval = RightOpenInterval.of( 0, Angle.TAU );
    // Interval of the latitude
    private static final ClosedInterval latInterval = ClosedInterval.of( -Angle.TAU / 4, Angle.TAU / 4 );

    private EclipticCoordinates( double lon, double lat )
    {
        super( lon, lat );
    }

    // methode of construction
    // throw exception if the interval not contains the values
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
