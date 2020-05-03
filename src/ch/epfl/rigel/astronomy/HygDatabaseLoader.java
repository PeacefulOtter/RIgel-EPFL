package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * Represents a HYG catalogue loader
 */
public enum HygDatabaseLoader implements StarCatalogue.Loader
{
    INSTANCE();

    // column index used to access the data we need in the file
    private final static int HIP_INDEX = 1;
    private final static int PROPER_INDEX = 6;
    private final static int MAG_INDEX = 13;
    private final static int CI_INDEX = 16;
    private final static int RARAD_INDEX = 23;
    private final static int DECRAD_INDEX = 24;
    private final static int BAYER_INDEX = 27;
    private final static int CON_INDEX = 29;

    /**
     * Adds to the catalogue builder all the stars obtained from the HYG catalogue using the content of a
     * csv file columns
     * @param inputStream : the stream who reads a file
     * @param builder : the builder used to build the list of stars
     * @throws IOException
     */
    @Override
    public void load( InputStream inputStream, StarCatalogue.Builder builder ) throws IOException
    {
        // using the try-with-resource method so we do not need to close the stream explicitly
        try ( BufferedReader stream = new BufferedReader( new InputStreamReader( inputStream, US_ASCII ) ) )
        {
            String line = stream.readLine(); // read the first line now because we don't need it (it is the columns name)
            String[] starInfo; // stores each line read

            while( ( line = stream.readLine() ) != null )
            {
                starInfo = line.split( "," );
                String hipIndex = starInfo[ HIP_INDEX ];
                String properIndex = starInfo[ PROPER_INDEX ];
                String bayerIndex = starInfo[ BAYER_INDEX ];
                String magIndex = starInfo[ MAG_INDEX ];
                String ciIndex = starInfo[ CI_INDEX ];

                // get the hipparcos ID (0 by default)
                int hipparcosId = ( hipIndex.equals( "" ) ) ? 0 : Integer.parseInt( hipIndex );

                // get the star name, if the proper name is empty, it consists of a concatenation between
                // the "Bayer" name and the "Con" name. (? by default)
                String name = ( properIndex.equals( "" ) ) ?
                        ( bayerIndex.equals( "" ) ? "?" : bayerIndex ) + " " + starInfo[ CON_INDEX ] :
                        properIndex;

                // get the star equatorial coordinates
                EquatorialCoordinates equatorialPos = EquatorialCoordinates.of(
                        Double.parseDouble( starInfo[ RARAD_INDEX ] ),
                        Double.parseDouble( starInfo[ DECRAD_INDEX ] ) );

                // get the star magnitude (0 by default)
                float magnitude = ( magIndex.equals( "" ) ) ? 0 : Float.parseFloat( magIndex );

                // get the star color index (0 by default)
                float colorIndex = ( ciIndex.equals( "" ) ) ? 0 : Float.parseFloat( ciIndex );

                // create a new star based on what we read and add it to the list of stars through the builder.
                builder.addStar( new Star( hipparcosId, name, equatorialPos, magnitude, colorIndex ) );
            }
        }
    }
}
