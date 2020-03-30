package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.io.InputStream;
import java.util.*;

public final class StarCatalogue
{

    private Map<Asterism, List<Integer>> index;
    private List<Star> stars;
    private Set<Asterism> asterisms;

    public StarCatalogue( List<Star> stars, List<Asterism> asterisms )
    {
        for ( Asterism asterism : asterisms )
        {
            Preconditions.checkArgument( stars.contains( asterism.stars() ) );
        }

        this.stars = stars;
        this.asterisms = Set.copyOf( asterisms );
        this.index = new HashMap<>();

        for ( Asterism asterism : asterisms )
        {
            List<Integer> tempList = new ArrayList<>();
            for ( Star asterismStar : asterism.stars() )
            {
                tempList.add( stars.indexOf( asterismStar ) );
            }
            index.put( asterism, tempList );
        }
    }

    public List<Star> stars(){
        return stars;
    }

    public Set<Asterism> asterisms(){
        return asterisms;
    }

    public List<Integer> asterismIndices( Asterism asterism )
    {
        Preconditions.checkArgument( asterisms.contains( asterism ) );
        return List.copyOf( index.get( asterism ) ) ;
    }

    public final static class Builder
    {

        Builder()
        {

        }

        Builder addStar( Star star ){
            return null;
        }

        // pas sur que ca soit ca qu il faut retourner ( une vue non modifiable )
        List<Star> stars()
        {
           // return Collections.unmodifiableList(stars());
            return null;
        }

        Builder addAsterism( Asterism asterism ){
            return null;
        }

        List<Asterism> asterisms()
        {
            // return Collections.unmodifiableList(asterism());
            return null;
        }

        Builder loadFrom( InputStream inputStream, Loader loader ){
            return null;
        }

        StarCatalogue build(){
            return null;
        }
    }

    interface Loader
    {
        public void load( InputStream inputStream, Builder builder );
    }
}
