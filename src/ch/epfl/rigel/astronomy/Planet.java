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

    @Override
    public Color[] nameColor(){
        return new Color[]{Color.valueOf("edf5f7"), Color.valueOf("20859e")};
    }
}
