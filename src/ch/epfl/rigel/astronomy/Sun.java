package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

public final class Sun extends CelestialObject
{
    private static final float MAGNITUDE = -26.7f;

    // save the ecliptic position and the mean anomaly
    private final EclipticCoordinates eclipticPos;
    private final float meanAnomaly;

    /**
     * throw NullPointerException if the ecliptique position is null
     */
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
