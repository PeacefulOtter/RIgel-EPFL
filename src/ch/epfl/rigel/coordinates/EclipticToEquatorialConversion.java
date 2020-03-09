package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.Polynomial;
import ch.epfl.rigel.math.RightOpenInterval;

import java.time.ZonedDateTime;
import java.util.function.Function;

public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates>
{
    // function used to determine epsilon based on the number of julian centuries
    private static final Polynomial EPSILON_POLYNOMIAL = Polynomial.of(
            Angle.ofArcsec( 0.00181 ),
            Angle.ofArcsec( -0.0006 ),
            Angle.ofArcsec( -46.815 ),
            Angle.ofDMS(23, 26, 21.45 )  );

    // cosine and sin of the Epsilon polynomial
    private final double cosEpsilon, sinEpsilon;

    private static final RightOpenInterval LON_INTERVAL = RightOpenInterval.of( 0, Angle.TAU );
    private static final ClosedInterval LAT_INTERVAL = ClosedInterval.of( -Angle.TAU / 4, Angle.TAU / 4);

    /**
     * change of coordinate system between ecliptic and equatorial coordinates for the date/time pair when
     * Stores important variables used in apply method
     * @param when : the actual time date and hour
     */
    public EclipticToEquatorialConversion( ZonedDateTime when )
    {
        double deltaJulianCenturies = Epoch.J2000.julianCenturiesUntil( when );
        double epsilon = EPSILON_POLYNOMIAL.at( deltaJulianCenturies );
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
        double eclLon = ecl.lon();
        double eclLat = ecl.lat();

        double sinLambda = Math.sin( eclLon );
        double cosLambda = Math.cos( eclLon );

        double sinBeta = Math.sin( eclLat );
        double cosBeta = Math.cos( eclLat );
        double tanBeta = Math.tan( eclLat );

        double equatorialLon = Math.atan2( ( sinLambda * cosEpsilon - tanBeta * sinEpsilon ) , cosLambda );
        double equatorialLat = Math.asin( sinBeta * cosEpsilon + cosBeta * sinEpsilon * sinLambda );

        return EquatorialCoordinates.of( LON_INTERVAL.reduce( equatorialLon ), LAT_INTERVAL.clip( equatorialLat ) );
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
