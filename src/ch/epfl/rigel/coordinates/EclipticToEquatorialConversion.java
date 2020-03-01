package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZonedDateTime;
import java.util.function.Function;

public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {

    // problem because Angle.ofDMS return radians and not minutes
    private final static double LAST_COEF = Angle.ofDMS(23, 26, 21.45 );
    private final Polynomial epsilonPolynomial = Polynomial.of( 0.00181, -0.0006, -46.815, 0 );
    private double cosEpsilon;
    private double sinEpsilon;
    public EclipticToEquatorialConversion( ZonedDateTime when ) {
        double daysT = Epoch.J2000.daysUntil( when );
        double epsilon = Angle.ofDeg( epsilonPolynomial.at(daysT) ) + LAST_COEF;
        cosEpsilon = Math.cos( epsilon );
        sinEpsilon = Math.sin( epsilon );
        // changement de système de coordonnées entre les coordonnées écliptiques et les coordonnées équatoriales pour le couple date/heure when.
        // ca veut dire quoi lol ?
    }

    @Override
    public EquatorialCoordinates apply( EclipticCoordinates ecl )
    {
        double sinLambda = Math.sin( ecl.lon() );
        double cosLambda = Math.cos( ecl.lon() );

        double sinBeta = Math.sin( ecl.lat() );
        double cosBeta = Math.cos( ecl.lat() );
        double tanBeta = Math.tan( ecl.lat() );

        double lon = Math.atan2((sinLambda * cosEpsilon - tanBeta * sinEpsilon) / cosLambda, 1);
        double lat = Math.asin(sinBeta * cosEpsilon + cosBeta * sinEpsilon * sinLambda);

        return EquatorialCoordinates.of(lon, lat);
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
