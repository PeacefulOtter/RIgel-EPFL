package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.scene.paint.Color;


/**
 * Represents a Star as a CelestialObject
 */
public final class Star extends CelestialObject
{
    private final static ClosedInterval COLOR_INTERVAL = ClosedInterval.of( -0.5, 5.5 );
    // Hipparcos number of the Star
    private final int hipparcosId;
    // index of the color
    private final float colorIndex;

    private final int colorTemperature;

    /**
     * @param hipparcosId: Hipparcos number of the Star
     * @param name : star name
     * @param equatorialPos : star equatorial position
     * @param magnitude : star magnitude
     * @param colorIndex : star color index
     * Throw IllegalArgumentException if the Hipparcos number is negative, or if the color index is not in the range [-0.5, 5.5].
     */
    public Star( int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex )
    {
        super( name, equatorialPos, 0, magnitude );
        Preconditions.checkArgument( hipparcosId >= 0 );
        Preconditions.checkInInterval( COLOR_INTERVAL, colorIndex );

        this.colorIndex = colorIndex;
        this.hipparcosId = hipparcosId;

        double d = 0.92 * colorIndex;
        colorTemperature = (int) Math.floor( 4600 * (  ( 1 / ( d + 1.7 ) ) + ( 1 / ( d + 0.62 ) )  ) );
    }

    /**
     * @return the hipparcos id of the star
     */
    public int hipparcosId() { return  hipparcosId; }

    /**
     * @return the color Temperature of the Star in Kelvin using the color Index
     */
    public int colorTemperature() { return colorTemperature; }

    public Color getBackgroundColor() { return Color.web( "#edf7f3" ); }

    public Color getTextColor() { return Color.web( "#209e67" ); }
}
