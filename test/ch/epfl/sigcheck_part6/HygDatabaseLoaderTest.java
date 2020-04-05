package ch.epfl.sigcheck_part6;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

import static ch.epfl.rigel.astronomy.Epoch.J2010;
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


    @Test
    void variousMoonModelAtValues()
    {
        System.out.println(MoonModel.MOON.at(-2313, new EclipticToEquatorialConversion(
                ZonedDateTime.of(LocalDate.of(2003,  Month.SEPTEMBER, 1), LocalTime.of(0,0), ZoneOffset.UTC))).equatorialPos().raHr());
        System.out.println(MoonModel.MOON.at(-2313, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003,  Month.SEPTEMBER, 1),LocalTime.of(0,0), ZoneOffset.UTC))).equatorialPos().dec());
        System.out.println(MoonModel.MOON.at(J2010.daysUntil(ZonedDateTime.of(LocalDate.of(1979, 9, 1),LocalTime.of(0, 0),
                ZoneOffset.UTC)), new EclipticToEquatorialConversion(ZonedDateTime.of(
                LocalDate.of(1979, 9, 1),LocalTime.of(0, 0),ZoneOffset.UTC))).
                angularSize());
        System.out.println(MoonModel.MOON.at(J2010.daysUntil(ZonedDateTime.of(LocalDate.of(2003, 9, 1),LocalTime.of(0, 0),
                ZoneOffset.UTC)), new EclipticToEquatorialConversion(ZonedDateTime.of( LocalDate.of(2003, 9, 1),
                LocalTime.of(0, 0),ZoneOffset.UTC))).info());
    }


    @Test
    void variousTestsAndReadablePrintfOnCompletelyFinishedStarCatalogue() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            InputStream asterismStream = getClass()
                    .getResourceAsStream(ASTERISM_CATALOGUE_NAME);
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE).loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();
            Star rigel = null;
            for (Star s : catalogue.stars()) {
                if (s.name().equalsIgnoreCase("rigel"))
                    rigel = s;
            }
            assertNotNull(rigel);

            List<Star> allStar = new ArrayList<Star>();
            allStar.addAll(catalogue.stars());

            System.out.println("LIST OF STARS :");
            for(Star s : allStar){
                System.out.printf("%6d ",s.hipparcosId());
            } //should print out the same star IDS as in the fichier (check visually)
            System.out.println();
            System.out.println();

            System.out.println("ASTERISMS : ");
            int i;

            //vérifier visuellement en utilisant CTRL-F que les astérismes contenu dans ASTERISMS sont bien les memes
            //flemme de coder une méthode qui vérifie automatiquement
            for(Asterism asterism : catalogue.asterisms()){
                List<Integer> cAstInd = catalogue.asterismIndices(asterism);
                i = 0;
                for(Star star : asterism.stars()){
                    System.out.print("Hip : ");
                    System.out.printf("%6d",star.hipparcosId());
                    System.out.print("  foundHipparcos : ");
                    System.out.printf("%6d", allStar.get(cAstInd.get(i)).hipparcosId());

                /*TEST : l'index stoqué dans asterismIndices renvoie le meme hipparcosId que
                l'index stoqué dans l'astérisme voulu : */
                    assertEquals(allStar.get(cAstInd.get(i)).hipparcosId(), star.hipparcosId());
                    System.out.print(" ||| ");
                    i++;
                }
                System.out.println();
            }


            for (Asterism asterism : catalogue.asterisms()) {
                int nbIndices = catalogue.asterismIndices(asterism).size();
                int nbStars = asterism.stars().size();
                assertEquals(nbIndices, nbStars);
            }


        }
    }

}

