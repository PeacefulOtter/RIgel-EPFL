package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * Represents any planet
 */
public final class Planet extends CelestialObject
{
    public Planet( String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude )
    {
        super( name, equatorialPos, angularSize, magnitude );
    }
}
