package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import javafx.scene.paint.Color;

/**
 * Represents any planet
 */
public final class Planet extends CelestialObject
{
    public Planet( String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude )
    {
        super( name, equatorialPos, angularSize, magnitude );
    }

    public Color getBackgroundColor() { return Color.web( "#edf5f7" ); }

    public Color getTextColor() { return Color.web( "#20859e" ); }

}
