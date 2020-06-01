package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

import java.util.Locale;
/**
 * Represents the Moon at a given moment
 */
public final class Moon extends CelestialObject
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
        // make sure the phase is inside the specified interval
        Preconditions.checkInInterval( PHASE_INTERVAL, phase );
        this.phase = phase;
    }

    private void setInfo(MoonInfo info) {
        super.setInfo("");
    }

    @Override
    public String info()
    {
        return String.format( Locale.ROOT, "Lune (%.1f%s)", phase * 100, "%" );
    }
}
