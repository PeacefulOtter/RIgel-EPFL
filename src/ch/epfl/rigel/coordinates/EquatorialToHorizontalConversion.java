package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;

import java.time.ZonedDateTime;
import java.util.function.Function;

public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates>
{

    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where )
    {
    }

    @Override
    public HorizontalCoordinates apply( EquatorialCoordinates equ )
    {
        double delta = equ.dec(); // delinaison coord equatorial ??
        double phi = equ.ra(); // latitude de l'observateur
        double H = SiderealTime.greenwich() - equ.ra(); // angle horaire - Sl = temps sidéral, alpha =
        double h; // hauteur coord horizontale = Math.asin( sin(delta) * sin(phi) + cos(delta) * cos(phi) * cos(H) );
        double A; // azimut coord horizontale = Math.atan2( ( -cos(delta) * cos(phi) * sin(H) ) / ( sin(delta) - sin(phi) * sin(h) ) );
        return null;
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
