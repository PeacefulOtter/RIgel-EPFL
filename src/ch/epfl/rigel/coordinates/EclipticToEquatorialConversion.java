package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZonedDateTime;
import java.util.function.Function;

public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates>
{
    private final static double LAST_COEF = Angle.ofDMS(23, 26, 21.45 );
    private final static Polynomial EPSILON_POLYNOMIAL = Polynomial.of( 0.00181, -0.0006, -46.815, 0 );
    private final double cosEpsilon;
    private final double sinEpsilon;

    /**
     * changement de système de coordonnées entre les coordonnées écliptiques et les coordonnées équatoriales pour le couple date/heure when.
     * @param when
     */
    public EclipticToEquatorialConversion( ZonedDateTime when )
    {
        double daysT = Epoch.J2000.daysUntil( when );
        double epsilon = Angle.ofDMS( 0, 0, EPSILON_POLYNOMIAL.at( daysT ) ) + LAST_COEF;
        cosEpsilon = Math.cos( epsilon );
        sinEpsilon = Math.sin( epsilon );
    }

    @Override
    public EquatorialCoordinates apply( EclipticCoordinates ecl )
    {
        double sinLambda = Math.sin( ecl.lon() );
        double cosLambda = Math.cos( ecl.lon() );

        double sinBeta = Math.sin( ecl.lat() );
        double cosBeta = Math.cos( ecl.lat() );
        double tanBeta = Math.tan( ecl.lat() );

        double lon = Math.atan2( ( sinLambda * cosEpsilon - tanBeta * sinEpsilon ) / cosLambda, 1 );
        double lat = Math.asin( sinBeta * cosEpsilon + cosBeta * sinEpsilon * sinLambda );

        return EquatorialCoordinates.of( lon, lat );
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
