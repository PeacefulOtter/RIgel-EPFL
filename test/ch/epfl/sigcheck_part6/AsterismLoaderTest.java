package ch.epfl.sigcheck_part6;

import ch.epfl.rigel.astronomy.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;


public class AsterismLoaderTest
{
    private static final String HYG_CATALOGUE_NAME =
            "/asterisms.txt";

    @Test
    void hygDatabaseContainsRigel() throws IOException
    {
        HygDatabaseLoaderTest test = new HygDatabaseLoaderTest();
        test.hygDatabaseContainsRigel();

        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, AsterismLoader.INSTANCE)
                    .build();
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
}
