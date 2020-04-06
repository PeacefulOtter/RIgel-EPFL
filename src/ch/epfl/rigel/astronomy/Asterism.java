package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.util.List;

/**
 * Represents an asterism, namely multiple stars stored in a list
 */
public final class Asterism
{
    private List<Star> stars;

    public Asterism( List<Star> stars )
    {
        // avoid having an empty asterism
        Preconditions.checkArgument( !stars.isEmpty() );
        this.stars = List.copyOf( stars );
    }

    // return a copy of the stars
    public List<Star> stars() { return List.copyOf( stars ); }
}
