package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import javafx.scene.paint.Color;

import java.util.Objects;

public abstract class CelestialObject
{
    // name of the object
    private final String name;

    // Equatorial position
    private final EquatorialCoordinates equatorialPos;

    // angular size and magnitude of the object
    private final float angularSize, magnitude;

    /**
     * throw IllegalArgumentException if the angular size is negative
     * throw NullPointerException if the name or the position is null
     */
    CelestialObject( String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude )
    {
        Objects.requireNonNull( name );
        Objects.requireNonNull( equatorialPos );

        Preconditions.checkArgument( angularSize >= 0 );
        this.name = name;
        this.equatorialPos = equatorialPos;
        this.angularSize = angularSize;
        this.magnitude = magnitude;
    }

    /**
     * @return the name of the object
     */
    public String name() { return name; }

    /**
     * @return the equatorial position of the object
     */
    public EquatorialCoordinates equatorialPos() { return equatorialPos; }

    /**
     * @return the angular size of the object
     */
    public double angularSize() { return angularSize; }

    /**
     * @return the magnitude of the object
     */
    public double magnitude() { return magnitude; }


    /**
     * @return an information message about the Celestial Object, by default returns its name
     */
    public String info()
    {
        return name;
    }

    public Color[] nameColor(){

        return new Color[]{Color.WHITE, Color.BLUE};
    }
    /**
     * @return the information written for the user
     */
    @Override
    public String toString()
    {
        return info();
    }
}
