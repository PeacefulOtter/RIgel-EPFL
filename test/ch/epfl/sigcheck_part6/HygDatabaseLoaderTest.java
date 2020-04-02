package ch.epfl.sigcheck_part6;

import ch.epfl.rigel.astronomy.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class HygDatabaseLoaderTest {
    private static final String HYG_CATALOGUE_NAME = "/hygdata_v3.csv";
    private static final String ASTERISM_CATALOGUE_NAME = "/asterisms.txt";

    @Test
    void hygDatabaseIsCorrectlyInstalled() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            assertNotNull(hygStream);
        }
    }

    /*Star rigel = null;
            System.out.println(catalogue.stars().size());
            for (Star s : catalogue.stars()) {
                //System.out.print(s.name() + " ");
                //System.out.print( s.hipparcosId() + " " );
                //System.out.print( s.equatorialPos().ra() + " " + s.equatorialPos().dec() + " " );
                //System.out.println( s.colorTemperature() );
                if (s.name().equalsIgnoreCase("rigel"))
                    rigel = s;
            }*/

    @Test
    public void hygDatabaseContainsRigel() throws IOException
    {
        StarCatalogue.Builder builder = new StarCatalogue.Builder();
        StarCatalogue catalogue;
        try ( InputStream hygStream = getClass().getResourceAsStream( HYG_CATALOGUE_NAME ) )
        {
            builder.loadFrom(hygStream, HygDatabaseLoader.INSTANCE);
        }
        try ( InputStream asterismStream = getClass().getResourceAsStream( ASTERISM_CATALOGUE_NAME ) )
        {
            builder.loadFrom(asterismStream, AsterismLoader.INSTANCE);
        }

        catalogue = builder.build();
        System.out.println(catalogue.asterisms().size());
        for ( Asterism asterism: catalogue.asterisms() )
        {
            for ( Star s : asterism.stars() )
            {
                System.out.print( s.hipparcosId() + " " );
            }
            System.out.println();
        }
    }
}

