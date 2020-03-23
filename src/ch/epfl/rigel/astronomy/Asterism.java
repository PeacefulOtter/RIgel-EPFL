package ch.epfl.rigel.astronomy;

import java.util.List;

public final class Asterism
{
    private List<Star> stars;

    public Asterism( List<Star> stars )
    {
        if ( stars.isEmpty() ) { throw new IllegalArgumentException( "List must not be empty" ); }
        this.stars = List.copyOf( stars );
    }

    // DO WE NEED TO RETURN A COPY OF THE ARRAY ???
    public List<Star> stars(){
        return stars;
    }
}
