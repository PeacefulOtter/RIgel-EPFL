package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public final class StarCatalogue
{
    // HashMap containing Asterisms and there star index
    private final Map<Asterism, List<Integer>> indices;
    // List of stars in the catalogue
    private final List<Star> stars;
    // Set of asterisms in the catalogue
    private final Set<Asterism> asterisms;

    /**
     *
     * @param stars: list of stars in the catalogue
     * @param asterisms: list of asterism in the catalogue
     * Build a catalogue of stars and asterisms, or throw IllegalArgumentException
     * if one of the asterisms contains a star that is not part of the star list.
     */
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

    /**
     * @return the list of stars of the catalogue
     */
    public List<Star> stars() { return List.copyOf( stars ); }

    /**
     *
     * @return the set of asterisms of the catalogue
     */
    public Set<Asterism> asterisms() { return Set.copyOf( asterisms ); }

    /**
     * @param asterism
     * @return the list of indexes in the catalogue of the stars constituting the given asterism,
     *         or throw IllegalArgumentException if the given asterism is not part of the catalogue
     */
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

        /**
         * @param star
         * @return adds the given star to the catalogue under construction, and returns the builder
         */
        public Builder addStar( Star star )
        {
            stars.add( star );
            return this;
        }

        /**
         * @return an unmodifiable but not immutable view of the stars in the catalogue under construction
         */
        public List<Star> stars() { return Collections.unmodifiableList( stars ); }

        /**
         * @param asterism
         * @return adds the given asterism to the catalogue under construction, and returns the builder
         */
        public Builder addAsterism( Asterism asterism )
        {
            asterisms.add(asterism);
            return this;
        }

        /**
         * @return an unmodifiable but not immutable view of the asterims in the catalogue under construction
         */
        public List<Asterism> asterisms() { return Collections.unmodifiableList( asterisms ); }

        /**
         * @param inputStream
         * @param loader
         * @return asks the loader to add to the catalog the stars and/or asterisms it obtains from the inputStream, and returns the builder
         * @throws IOException
         */
        public Builder loadFrom( InputStream inputStream, Loader loader ) throws IOException
        {
            loader.load( inputStream, this );
            return this;
        }

        /**
         * @return the catalogue containing the stars and asterisms added up to now by the builder
         */
        public StarCatalogue build()
        {
            return new StarCatalogue( stars, asterisms );
        }
    }


    public interface Loader
    {
        /**
         * @param inputStream
         * @param builder
         * @throws IOException
         * loads the stars and/or asterisms of the inputStream and adds them to the builder, or lifts IOException in case of input/output error
         */
        void load( InputStream inputStream, Builder builder ) throws IOException;
    }
}
