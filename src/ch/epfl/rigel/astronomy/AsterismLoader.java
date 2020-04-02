package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.US_ASCII;

public enum AsterismLoader implements StarCatalogue.Loader{
    INSTANCE();

    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException
    {
        try ( BufferedReader stream = new BufferedReader( new InputStreamReader( inputStream, US_ASCII ) ) )
        {
            String line ; 
            String[] starInfo;

            while( ( line = stream.readLine() ) != null )
            {
                List<Star> asterism = new ArrayList<>();
                starInfo = line.split( "," );
                for ( String hip : starInfo ) {
                    for ( Star star : builder.stars() ) {
                        if( star.hipparcosId() == Integer.parseInt(hip)){
                            asterism.add(star);
                        }
                    }
                }
                builder.addAsterism(new Asterism(asterism));
            }
        }
    }
}
