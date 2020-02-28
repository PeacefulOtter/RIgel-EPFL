package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Polynomial;

import java.security.spec.ECPoint;
import java.security.spec.RSAOtherPrimeInfo;
import java.time.ZonedDateTime;
import java.util.function.Function;

public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {

    private Polynomial epsilonPolynomial = Polynomial.of( 0.00181, -0.0006, -46.815, 232621.45 );

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

}
