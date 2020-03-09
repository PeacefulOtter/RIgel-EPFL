package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

public final class Sun extends CelestialObject
{
    private static final float MAGNITUDE = -26.7f;
    //private static final double ANGULAR_SPEED = Angle.TAU / 365.242191;
    //private static final double LON_J2010 = Angle.ofDeg( 279.557208 );
    //private static final double LON_PERIGEE = Angle.ofDeg( 283.112438 );
    //private static final double ECCENTRICITY = Angle.ofArcsec( 0.016705 );


    // save the ecliptic position and the mean anomaly
    private final EclipticCoordinates eclipticPos;
    private final float meanAnomaly;

    public Sun( EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos, float angularSize, float meanAnomaly )
    {
        super( "Soleil", equatorialPos, angularSize, MAGNITUDE );
        if ( eclipticPos == null )
        {
            throw new NullPointerException( "EclipticPos is NotNull" );
        }

        this.eclipticPos = eclipticPos;
        this.meanAnomaly = meanAnomaly;
    }

    // ecliptic position getter
    public EclipticCoordinates eclipticPos() { return eclipticPos; }
    // mean anomaly getter
    public double meanAnomaly() { return meanAnomaly; }
}
