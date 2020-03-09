package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

import java.util.Locale;

public class Moon extends CelestialObject
{
    private static final ClosedInterval PHASE_INTERVAL = ClosedInterval.of( 0, 1 );
    private float phase;

    public Moon( EquatorialCoordinates equatorialPos, float angularSize, float magnitude, float phase )
    {
        super( "Lune", equatorialPos, angularSize, magnitude );
        if ( !PHASE_INTERVAL.contains( phase ) )
        {
            throw new IllegalArgumentException( "phase must be between O and 1 included" );
        }
        this.phase = phase;
    }

    @Override
    public String info()
    {
        return String.format( Locale.ROOT, "Lune (%.1f%s)", phase * 100, "%" );
    }
}
