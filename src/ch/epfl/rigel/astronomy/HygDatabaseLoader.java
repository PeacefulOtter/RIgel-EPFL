package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.US_ASCII;

public enum HygDatabaseLoader implements StarCatalogue.Loader {

    INSTANCE();

    @Override
    public void load( InputStream inputStream, StarCatalogue.Builder builder ) throws IOException
    {
        // hip, proper, (or bayer+con), rarad decrad, mag || 0, ci || 0

        try ( BufferedReader stream = new BufferedReader( new InputStreamReader( inputStream, US_ASCII ) ) )
        {
            // stream.readLine
        }
    }
}
