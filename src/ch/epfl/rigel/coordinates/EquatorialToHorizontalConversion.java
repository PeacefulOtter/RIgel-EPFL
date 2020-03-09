package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.time.ZonedDateTime;
import java.util.function.Function;

public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates>
{

    private final double localTime, sinPhi, cosPhi;

    private static final RightOpenInterval LON_INTERVAL =  RightOpenInterval.of( 0, Angle.TAU );
    private static final ClosedInterval LAT_INTERVAL = ClosedInterval.of( -Math.PI / 2, Math.PI / 2 );


    /**
     * change of coordinate systems from equatorial coordinates to ecliptic coordinates, at a given time and for a given location
     * Stores important variables used in apply method
     * @param when : the actual time date and hour
     * @param where : a position
     */
    public EquatorialToHorizontalConversion( ZonedDateTime when, GeographicCoordinates where )
    {
        this.localTime = SiderealTime.local( when, where );
        this.sinPhi = Math.sin( where.lat() );
        this.cosPhi = Math.cos( where.lat() );
    }

    /**
     * Apply the formula to convert Equatorial coordinates to Horizontal coordinates
     * @param equ : Equatorial Coordinates
     * @return : horizontal coordinates corresponding to equatorial coordinates (equ)
     */
    @Override
    public HorizontalCoordinates apply( EquatorialCoordinates equ )
    {
        double declination = equ.dec(); // declination equatorial coordinates
        double H = localTime - equ.ra(); // hour angle

        double sinDelta = Math.sin( declination );
        double cosDelta = Math.cos( declination );

        double height = Math.asin( sinDelta * sinPhi + cosDelta * cosPhi * Math.cos( H ) ); // hauteur coord horizontale
        double azimut = Math.atan2( ( -cosDelta * cosPhi * Math.sin( H ) ), ( sinDelta - sinPhi * Math.sin( height ) ) ); // azimut coord horizontale

        return HorizontalCoordinates.of( LON_INTERVAL.reduce( azimut ), LAT_INTERVAL.clip( height ) );
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}
