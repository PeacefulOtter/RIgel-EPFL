package ch.epfl.rigel.astronomy;

import java.util.List;

public final class Asterism {
    private List<Star> stars;

    public Asterism(List<Star> stars){
        if ( stars.isEmpty()){
            throw new IllegalArgumentException();
        }
        this.stars = List.copyOf(stars); // est ce que copyof est bien (immuable) ?
    }

    public List<Star> stars(){
        return stars;
    }
}
