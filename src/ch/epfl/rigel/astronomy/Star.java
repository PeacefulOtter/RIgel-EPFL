package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

public final class Star extends CelestialObject
{
    private final static ClosedInterval COLOR_INTERVAL = ClosedInterval.of( -0.5, 5.5 );
    // Hipparcos number of the Star
    private final int hipparcosId;
    // index of the color
    private final float colorIndex;

    /**
     * @param hipparcosId: Hipparcos number of the Star
     * @param name
     * @param equatorialPos
     * @param magnitude
     * @param colorIndex
     * Throw IllegalArgumentException if the Hipparcos number is negative, or if the colour index is not in the range [-0.5, 5.5].
     */
    public Star( int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex )
    {
        super( name, equatorialPos, 0, magnitude );
        Preconditions.checkArgument( hipparcosId >= 0 );
        Preconditions.checkInInterval( COLOR_INTERVAL, colorIndex );

        this.colorIndex = colorIndex;
        this.hipparcosId = hipparcosId;
    }

    /**
     * @return the hipparcos number of the star
     */
    public int hipparcosId() { return  hipparcosId; }

    /**
     * @return the color Temperature of the Star in Kelvin using the color Index
     */
    public int colorTemperature()
    {
        double d = 0.92 * colorIndex;
        return (int) Math.floor( 4600 * (  ( 1 / ( d + 1.7 ) ) + ( 1 / ( d + 0.62 ) )  ) );
    }
}
