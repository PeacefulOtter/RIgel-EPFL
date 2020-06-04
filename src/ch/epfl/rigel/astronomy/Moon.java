package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.scene.paint.Color;

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

    @Override
    public String info()
    {
        return String.format( Locale.ROOT, "Lune (%.1f%s)", phase * 100, "%" );
    }


    public Color getBackgroundColor() { return Color.web( "#f1d0f5" ); }

    public Color getTextColor() { return Color.web( "#c43ed6" ); }

}


