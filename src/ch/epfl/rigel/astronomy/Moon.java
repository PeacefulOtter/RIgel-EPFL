package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

import java.util.Locale;

public class Moon extends CelestialObject
{
    // phase Interval
    private static final ClosedInterval PHASE_INTERVAL = ClosedInterval.of( 0, 1 );

    private final float phase;

    /**
     * throw IllegalArgumentException if the phase is not contains by the Interval
     */
    public Moon( EquatorialCoordinates equatorialPos, float angularSize, float magnitude, float phase )
    {
        super( "Lune", equatorialPos, angularSize, magnitude );
        Preconditions.checkInInterval( PHASE_INTERVAL, phase );
        this.phase = phase;
    }

    @Override
    public String info()
    {
        return String.format( Locale.ROOT, "Lune (%.1f%s)", phase * 100, "%" );
    }
}
