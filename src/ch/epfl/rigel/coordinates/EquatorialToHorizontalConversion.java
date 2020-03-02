package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;

import java.time.ZonedDateTime;
import java.util.function.Function;

public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates>
{
    private final double Sl, sinPhi, cosPhi;

    public EquatorialToHorizontalConversion( ZonedDateTime when, GeographicCoordinates where )
    {
        this.Sl = SiderealTime.local( when, where );
        this.sinPhi = Math.sin( where.lat() );
        this.cosPhi = Math.cos( where.lat() );
    }

    @Override
    public HorizontalCoordinates apply( EquatorialCoordinates equ )
    {
        double delta = equ.dec(); // delinaison coord equatorial
        double H = Sl - equ.ra(); // angle horaire

        double sinDelta = Math.sin( delta );
        double cosDelta = Math.cos( delta );

        double height = Math.asin( sinDelta * sinPhi + cosDelta * cosPhi * Math.cos( H ) ); // hauteur coord horizontale
        double azimut = Math.atan2( ( -cosDelta * cosPhi * Math.sin( H ) ), ( sinDelta - sinPhi * Math.sin( height ) ) ); // azimut coord horizontale

        return HorizontalCoordinates.of( azimut, height );
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
