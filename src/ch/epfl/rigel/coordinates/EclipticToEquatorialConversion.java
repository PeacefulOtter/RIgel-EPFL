package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZonedDateTime;
import java.util.function.Function;

public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates>
{
    // the last coeff of Epsilon polynomial
    private final static double LAST_COEF = Angle.ofDMS(23, 26, 21.45 );

    // function used to determine epsilon based on the number of julians century
    private final static Polynomial EPSILON_POLYNOMIAL = Polynomial.of( 0.00181, -0.0006, -46.815, 0 );

    // cosinus of the Epsilon polynomial
    private final double cosEpsilon;

    // sinus of the Epsilon polynomial
    private final double sinEpsilon;

    /**
     * change of coordinate system between ecliptic and equatorial coordinates for the date/time pair when
     * Stores important variables used in apply method
     * @param when : the actual time date and hour
     */
    public EclipticToEquatorialConversion( ZonedDateTime when )
    {
        double daysT = Epoch.J2000.daysUntil( when );
        double epsilon = Angle.ofDMS( 0, 0, EPSILON_POLYNOMIAL.at( daysT ) ) + LAST_COEF;
        cosEpsilon = Math.cos( epsilon );
        sinEpsilon = Math.sin( epsilon );
    }

    /**
     * Apply the formula to convert Ecliptic coordinates to Equatorial coordinates
     * @param ecl : Ecliptic Coordinates
     * @return : Equatorial Coordinates corresponding to the Ecliptic Coordinates input
     */
    @Override
    public EquatorialCoordinates apply( EclipticCoordinates ecl )
    {
        double sinLambda = Math.sin( ecl.lon() );
        double cosLambda = Math.cos( ecl.lon() );

        double sinBeta = Math.sin( ecl.lat() );
        double cosBeta = Math.cos( ecl.lat() );
        double tanBeta = Math.tan( ecl.lat() );

        double lon = Math.atan2( ( sinLambda * cosEpsilon - tanBeta * sinEpsilon ) , cosLambda );
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
