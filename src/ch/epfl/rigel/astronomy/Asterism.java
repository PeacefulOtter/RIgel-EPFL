package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.util.List;

public final class Asterism
{
    private List<Star> stars;

    public Asterism( List<Star> stars )
    {
        Preconditions.checkArgument( !stars.isEmpty() );
        this.stars = List.copyOf( stars );
    }

    // DO WE NEED TO RETURN A COPY OF THE ARRAY ???
    public List<Star> stars(){
        return stars;
    }
}
