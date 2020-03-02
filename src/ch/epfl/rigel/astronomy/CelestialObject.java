package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

public abstract class CelestialObject
{
    private final String name;
    private final EquatorialCoordinates equatorialPos;
    private final float angularSize, magnitude;

    CelestialObject( String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude )
    {
        Objects.requireNonNull( name );
        Objects.requireNonNull( equatorialPos );
        if ( angularSize < 0 )
        {
            throw new IllegalArgumentException( "angular size must be positive or 0" );
        }
        this.name = name;
        this.equatorialPos = equatorialPos;
        this.angularSize = angularSize;
        this.magnitude = magnitude;
    }

    public String name()
    {
        return name;
    }

    public EquatorialCoordinates equatorialPos()
    {
        return equatorialPos;
    }

    public double angularSize()
    {
        return angularSize;
    }

    public double magnitude()
    {
        return magnitude;
    }

    /*


    MUST BE OVERIDDEN IN SUBCLASSES
    String info(), qui retourne un (court) texte informatif au sujet de l'objet, destiné à être montré à l'utilisateur.




     */
    public String info()
    {
        return name();
    }

    @Override
    public String toString()
    {
        return info();
    }
}
