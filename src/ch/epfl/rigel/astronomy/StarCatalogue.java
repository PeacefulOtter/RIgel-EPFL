package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.io.IOException;
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

        private List< Star > stars;
        private List< Asterism > asterisms;

        Builder() {
            stars = new ArrayList<>();
            asterisms = new ArrayList<>();
        }

        Builder addStar( Star star ) {
            stars.add(star);
            return this;
        }

        List<Star> stars() { return Collections.unmodifiableList(stars()); }

        Builder addAsterism( Asterism asterism ) {
            asterisms.add(asterism);
            return this;
        }

        List<Asterism> asterisms() { return Collections.unmodifiableList(asterisms()); }

        Builder loadFrom( InputStream inputStream, Loader loader )
        {
            try {
                loader.load( inputStream, this );
            } catch( IOException e )
            {
                e.printStackTrace();
            }
            return this;
        }

        StarCatalogue build()
        {
            return new StarCatalogue( stars, asterisms );
        }
    }

    public interface Loader
    {
        void load( InputStream inputStream, Builder builder ) throws IOException;
    }
}
