package ch.epfl.sigcheck_part6;

import ch.epfl.rigel.astronomy.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
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


    @Test
    void HygDatabaseLoadTest() throws IOException{
        try (InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) {
            StarCatalogue catalogue = new StarCatalogue.Builder().loadFrom(hygStream, HygDatabaseLoader.INSTANCE).build();
            Star rigel = catalogue.stars().get(1019);
            Star bellatrix = catalogue.stars().get(1068);
            Star betelgeuse = catalogue.stars().get(1213);
            Star nameEmpty = catalogue.stars().get(1212);
            Star nameEmpty2 = catalogue.stars().get(1208);
            Star ciEmpty = catalogue.stars().get(5041);
            //hipparcosId
            assertEquals(24436, rigel.hipparcosId());
            assertEquals(25336, bellatrix.hipparcosId());
            //colorTemperature
            assertEquals(10500, rigel.colorTemperature(), 1e2);
            assertEquals(3800, betelgeuse.colorTemperature(), 1e2);
            //names from PROPER
            assertEquals("Rigel", rigel.name());
            assertEquals("Bellatrix", bellatrix.name());
            //name without PROPER and without BAYER
            assertEquals("? Aur", nameEmpty.name());
            //name without PROPER but with BAYER
            assertEquals("Xi Aur", nameEmpty2.name());
            //check of colorTemperature without CI
            assertEquals((int)(4600*(1/1.7 + 1/0.62)), ciEmpty.colorTemperature());
            //magnitudes (couldn't find any empty magnitude)
            assertEquals(0.18, rigel.magnitude(), 1e-6);
            assertEquals(0.45, betelgeuse.magnitude(), 1e-6);
            //equatorial coordinates (in radians)
            assertEquals(1.3724303693276385, rigel.equatorialPos().ra());
            assertEquals(-0.143145630755865, rigel.equatorialPos().dec());
            //hipparcosId without HIP
            assertEquals(0, ciEmpty.hipparcosId());
        }
    }

}

