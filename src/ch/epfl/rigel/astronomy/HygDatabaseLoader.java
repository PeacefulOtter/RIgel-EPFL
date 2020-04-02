package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.US_ASCII;

public enum HygDatabaseLoader implements StarCatalogue.Loader
{
    INSTANCE();

    private final static int HIP_INDEX = 1;
    private final static int PROPER_INDEX = 6;
    private final static int MAG_INDEX = 13;
    private final static int CI_INDEX = 16;
    private final static int RARAD_INDEX = 23;
    private final static int DECRAD_INDEX = 24;
    private final static int BAYER_INDEX = 27;
    private final static int CON_INDEX = 29;

    @Override
    public void load( InputStream inputStream, StarCatalogue.Builder builder ) throws IOException
    {
        try ( BufferedReader stream = new BufferedReader( new InputStreamReader( inputStream, US_ASCII ) ) )
        {

            String line = stream.readLine(); // read the first line now because we don't need it (returns the columns name)
            String[] starInfo;

            while( ( line = stream.readLine() ) != null )
            {
                starInfo = line.split( "," );

                int hipparcosId = ( starInfo[ HIP_INDEX ].equals( "" ) ) ? 0 : Integer.parseInt( starInfo[ HIP_INDEX ] );

                String name = ( starInfo[ PROPER_INDEX ].equals( "" ) ) ?
                        starInfo[ BAYER_INDEX ] + " " + starInfo[ CON_INDEX ] :
                        starInfo[ PROPER_INDEX ];

                EquatorialCoordinates equatorialPos = EquatorialCoordinates.of(
                        Double.parseDouble( starInfo[ RARAD_INDEX ] ),
                        Double.parseDouble( starInfo[ DECRAD_INDEX ] ) );

                float magnitude = ( starInfo[ MAG_INDEX ].equals( "" ) ) ? 0 : Float.parseFloat( starInfo[ MAG_INDEX ] );

                float colorIndex = ( starInfo[ CI_INDEX ].equals( "" ) ) ? 0 : Float.parseFloat( starInfo[ CI_INDEX ] );

                builder.addStar( new Star( hipparcosId, name, equatorialPos, magnitude, colorIndex ) );
            }
        }
    }
}
