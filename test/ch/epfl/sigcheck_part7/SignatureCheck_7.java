package ch.epfl.sigcheck_part7;

import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.gui.BlackBodyColor;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.Arrays;

final class SignatureCheck_7
{
    @Test
    void BlackBodyColorTest()
    {
        System.out.println( BlackBodyColor.colorForTemperature( 7000 ) );
        System.out.println( BlackBodyColor.colorForTemperature( 3000 ) );
        System.out.println( BlackBodyColor.colorForTemperature( 15000 ) );
    }

    private InputStream resourceStream(String resourceName) {
        return getClass().getResourceAsStream(resourceName);
    }

    @Test
    void ObservedSkyTest() throws IOException
    {
        StarCatalogue catalogue;
        try ( InputStream hs = resourceStream("/hygdata_v3.csv") ) {
            catalogue = new StarCatalogue.Builder()
                    .loadFrom(hs, HygDatabaseLoader.INSTANCE)
                    .build(); }
        ZonedDateTime when =
                ZonedDateTime.parse("2020-02-17T20:15:00+01:00");
        GeographicCoordinates where =
                GeographicCoordinates.ofDeg(6.57, 46.52);
        HorizontalCoordinates projCenter =
                HorizontalCoordinates.ofDeg(180, 45);
        StereographicProjection projection =
                new StereographicProjection(projCenter);
        ObservedSky sky = new ObservedSky(when, where, projection, catalogue);

        System.out.println( sky.moonPosition() );
        System.out.println( sky.sunPosition() );
        System.out.println( Arrays.toString( sky.planetPosition() ) );
        System.out.println( sky.objectClosestTo( CartesianCoordinates.of( 3, 3 ), 10 ).get().info() );
    }
}
