package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

public final class EclipticCoordinates extends SphericalCoordinates
{
    // Interval of the longitude
    private static final RightOpenInterval LON_INTERVAL = RightOpenInterval.of( 0, Angle.TAU );
    // Interval of the latitude
    private static final ClosedInterval LAT_INTERVAL = ClosedInterval.of( -Angle.TAU / 4, Angle.TAU / 4 );

    private EclipticCoordinates( double lon, double lat )
    {
        super( lon, lat );
    }

    /**
     * Creates an Ecliptic Coordinate
     * @param lon : longitude
     * @param lat : latitude
     * @throws IllegalArgumentException if the longitude or the latitude given is out of bound
     * @return the coordinates
     */
    public static EclipticCoordinates of( double lon, double lat )
    {
        Preconditions.checkInInterval( LON_INTERVAL, lon );
        Preconditions.checkInInterval( LAT_INTERVAL, lat );

        return new EclipticCoordinates( lon, lat );
    }

    @Override
    public double lon() { return super.lon(); }

    @Override
    public double lat() { return super.lat(); }

    @Override
    public double lonDeg() { return super.lonDeg(); }

    @Override
    public double latDeg() { return super.latDeg(); }

    @Override
    public String toString()
    {
        return String.format( Locale.ROOT, "(λ=%.4f°, β=%.4f°)", lonDeg(), latDeg() );
    }
}
