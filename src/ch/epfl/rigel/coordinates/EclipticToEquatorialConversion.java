package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZonedDateTime;
import java.util.function.Function;

public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {

    // problem because Angle.ofDMS return radians and not minutes
    private final static double lastCoef = Angle.ofDMS(23, 26, 21.45 );
    private final Polynomial epsilonPolynomial = Polynomial.of( 0.00181, -0.0006, -46.815, 0 );

    public EclipticToEquatorialConversion( ZonedDateTime when ) {
        // changement de système de coordonnées entre les coordonnées écliptiques et les coordonnées équatoriales pour le couple date/heure when.
        // ca veut dire quoi lol ?
    }

    @Override
    public EquatorialCoordinates apply( EclipticCoordinates ecl )
    {
        double lambda = ecl.lon();
        double beta = ecl.lat();
        System.out.println(epsilonPolynomial);

        // need to do a conversion from the polynomial to an actual angle !!!

        //double T = Epoch.J2000.julianCenturiesUntil( ???????? );
        //double epsilon = epsilonPolynomial.at( T )
        //double alpha = Math.atan2( ( Math.sin( lambda ) * Math.cos( epsilon ) - Math.tan( beta ) * Math.sin( epsilon ) ) / Math.cos( lambda ) );
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
