package ch.epfl.rigel.astronomy;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public final class StarCatalogue {

    private HashMap<Asterism, List<Integer>> index;
    private List<Star> stars;
    private Set<Asterism> asterisms;

    public StarCatalogue(List<Star> stars, List<Asterism> asterisms){
        for ( Asterism asterism : asterisms ) {
            if (!stars.contains(asterism.stars())){
                throw new IllegalArgumentException();
            }
        }

        this.stars = stars;
        this.asterisms = Set.copyOf(asterisms);

        for (Asterism asterism : asterisms) {
            List< Integer > list = new ArrayList<>();
            for ( Star asterismStar : asterism.stars() ) {
                list.add(stars.indexOf(asterismStar));
            }
            index.put(asterism, list);
        }
    }

    public List<Star> stars(){
        return stars;
    }

    public Set<Asterism> asterisms(){
        return asterisms;
    }

    public List<Integer> asterismIndices(Asterism asterism){
        if ( !asterisms.contains(asterism)){
            throw new IllegalArgumentException();
        }

        return List.copyOf( index.get( asterism ) ) ;
    }

    public final static class Builder {

        Builder(){

        }

        Builder addStar(Star star){
            return null;
        }

        // pas sur que ca soit ca qu il faut retourner ( une vue non modifiable )
        List<Star> stars(){
           // return Collections.unmodifiableList(stars());
            return null;
        }

        Builder addAsterism(Asterism asterism){
            return null;
        }

        List<Asterism> asterisms(){
            // return Collections.unmodifiableList(asterism());
            return null;
        }

        Builder loadFrom(InputStream inputStream, Loader loader){
            return null;
        }

        StarCatalogue build( ){
            return null;
        }
    }

    interface Loader {

        public void load(InputStream inputStream, Builder builder);

    }
}
