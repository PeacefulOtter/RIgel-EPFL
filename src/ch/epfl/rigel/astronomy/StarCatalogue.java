package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public final class StarCatalogue
{

    private final Map<Asterism, List<Integer>> indices;
    private final List<Star> stars;
    private final Set<Asterism> asterisms;

    public StarCatalogue( List<Star> stars, List<Asterism> asterisms )
    {
        for ( Asterism asterism : asterisms )
        {
            for ( Star star : asterism.stars() )
            {
                Preconditions.checkArgument( stars.contains( star ) );
            }
        }

        this.stars = List.copyOf( stars );
        this.asterisms = Set.copyOf( asterisms );
        this.indices = new HashMap<>();

        for ( Asterism asterism : asterisms )
        {
            List<Integer> tempList = new ArrayList<>();
            for ( Star asterismStar : asterism.stars() )
            {
                tempList.add( stars.indexOf( asterismStar ) );
            }
            indices.put( asterism, tempList );
        }
    }

    // return a copy ??
    public List<Star> stars(){
        return stars;
    }
    // return a copy ??
    public Set<Asterism> asterisms(){
        return asterisms;
    }

    // return a copy ??
    public List<Integer> asterismIndices( Asterism asterism )
    {
        Preconditions.checkArgument( asterisms.contains( asterism ) );
        return List.copyOf( indices.get( asterism ) ) ;
    }

    public final static class Builder
    {

        private List<Star> stars;
        private List<Asterism> asterisms;

        public Builder()
        {
            this.stars = new ArrayList<>();
            this.asterisms = new ArrayList<>();
        }


        public Builder addStar( Star star )
        {
            stars.add( star );
            return this;
        }

        public List<Star> stars() { return Collections.unmodifiableList( stars ); }


        public Builder addAsterism( Asterism asterism )
        {
            asterisms.add(asterism);
            return this;
        }

        public List<Asterism> asterisms() { return Collections.unmodifiableList( asterisms ); }


        public Builder loadFrom( InputStream inputStream, Loader loader ) throws IOException
        {
            loader.load( inputStream, this );
            return this;
        }

        public StarCatalogue build()
        {
            return new StarCatalogue( stars, asterisms );
        }
    }

    public interface Loader
    {
        void load( InputStream inputStream, Builder builder ) throws IOException;
    }
}
