package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.US_ASCII;

public enum AsterismLoader implements StarCatalogue.Loader{
    INSTANCE();

    /**
     * @param inputStream : the stream who reads a file
     * @param builder : the StarCatalogue builder used to build the set of asterisms
     * @throws IOException
     * loads asterisms of the inputStream and adds them to the builder, or lifts IOException in case of input/output error
     */
    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException
    {
        try ( BufferedReader stream = new BufferedReader( new InputStreamReader( inputStream, US_ASCII ) ) )
        {
            String line ; 
            String[] fileHip;
            List<Star> asterism;

            Map<Integer, Star> hipparcosStarMap = new HashMap<>();
            for ( Star s : builder.stars() )
            {
                hipparcosStarMap.put( s.hipparcosId(), s );
            }

            while( ( line = stream.readLine() ) != null )
            {
                asterism = new ArrayList<>();
                fileHip = line.split( "," );

                for ( String hip : fileHip )
                {
                    asterism.add( hipparcosStarMap.get( Integer.parseInt( hip ) ) );
                }

                builder.addAsterism( new Asterism( asterism ) );
            }
        }
    }
}
