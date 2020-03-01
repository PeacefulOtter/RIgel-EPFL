package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;

import java.time.ZonedDateTime;
import java.util.function.Function;

public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates>
{
    private final double Sl;

    public EquatorialToHorizontalConversion( ZonedDateTime when, GeographicCoordinates where )
    {
        // add something there idk what
        Sl = SiderealTime.local( when, where );
    }

    @Override
    public HorizontalCoordinates apply( EquatorialCoordinates equ )
    {
        double delta = equ.dec(); // delinaison coord equatorial
        double phi = equ.ra(); // latitude de l'observateur
        double H = Sl - phi; // angle horaire
        double h = Math.asin( Math.sin(delta) * Math.sin(phi) + Math.cos(delta) * Math.cos(phi) * Math.cos(H) ); // hauteur coord horizontale
        double A = Math.atan2( ( -Math.cos(delta) * Math.cos(phi) * Math.sin(H) ) / ( Math.sin(delta) - Math.sin(phi) * Math.sin(h) ), 1 ); // azimut coord horizontale
        return HorizontalCoordinates.of( A, h );
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
