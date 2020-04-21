package ch.epfl.sigcheck_part6;

import ch.epfl.rigel.astronomy.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

final class SignatureChecks_6 {

    private static final String HYG_CATALOGUE_NAME = "/hygdata_v3.csv";
    private static final String ASTERISM_CATALOGUE_NAME = "/asterisms.txt";
    void checkMoonModel() {
        Enum<MoonModel> m1 = MoonModel.MOON;
        CelestialObjectModel<Moon> m2 = MoonModel.MOON;
    }

    void checkStarCatalogue() throws IOException {
        List<Star> sl = null;
        Star s = null;
        Asterism a = null;
        List<Asterism> al = null;
        Set<Asterism> as = null;
        List<Integer> il;
        StarCatalogue c = new StarCatalogue(sl, al);
        sl = c.stars();
        as = c.asterisms();
        il = c.asterismIndices(a);

        InputStream i = null;
        StarCatalogue.Loader l = null;
        StarCatalogue.Builder b = new StarCatalogue.Builder();
        b = b.addStar(s);
        sl = b.stars();
        b = b.addAsterism(a);
        al = b.asterisms();
        b = b.loadFrom(i, l);
        c = b.build();

        l.load(i, b);
    }

    void checkHygDatabaseLoader() {
        StarCatalogue.Loader l = HygDatabaseLoader.INSTANCE;
    }

    void checkAsterismLoader() {
        StarCatalogue.Loader l = AsterismLoader.INSTANCE;
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
                System.out.print(s.hipparcosId() + " ");
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
                    System.out.print(star.hipparcosId());
                    System.out.print("  foundHipparcos : ");
                    System.out.print(allStar.get(cAstInd.get(i)).hipparcosId());

                /*TEST : l'index stoqué dans asterismIndices renvoie le meme hipparcosId que
                l'index stoqué dans l'astérisme voulu : */
                    assertEquals(allStar.get(cAstInd.get(i)).hipparcosId(), star.hipparcosId());
                    System.out.print(" ||| ");
                    i++;
                }
                System.out.println();
            }
        }
    }
}
