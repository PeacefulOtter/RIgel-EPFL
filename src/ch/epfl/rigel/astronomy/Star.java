package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

public final class Star extends CelestialObject
{
    private final static ClosedInterval COLOR_INTERVAL = ClosedInterval.of( -0.5, 5.5 );

    private final int hipparcosId;
    private final float colorIndex;

    public Star( int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex )
    {
        super( name, equatorialPos, 0, magnitude );

        if( hipparcosId < 0 || COLOR_INTERVAL.contains( colorIndex ) )
        {
            throw new IllegalArgumentException( "HipparcosID must be positive / Color index must be between -0.5 and 5.5 included" );
        }

        this.colorIndex = colorIndex;
        this.hipparcosId = hipparcosId;
    }

    public int hipparcosId() { return  hipparcosId; }

    public int colorTemperature()
    {
        double d = 0.92 * colorIndex;
        return (int) Math.floor( 4600 * ( ( 1 / d + 1.7 ) + 1 / ( d + 0.62 ) ) );
    }
}
