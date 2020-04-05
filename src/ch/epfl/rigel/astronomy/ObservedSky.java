
package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ObservedSky {

    // pas sur de cette atribut
    private final Set< CelestialObject > celestObject;
    private final Sun sun;
    private final Moon moon;

    public ObservedSky( ZonedDateTime moment, GeographicCoordinates position,
                        StereographicProjection projection, StarCatalogue catalogue ) {
        celestObject = new HashSet<>();
        EclipticToEquatorialConversion p = new EclipticToEquatorialConversion( moment );
        double m = Epoch.J2000.daysUntil(moment);

        sun = SunModel.SUN.at(m, p);
        moon = MoonModel.MOON.at(m, p);
        celestObject.add(sun);
        celestObject.add(moon);
        for ( PlanetModel planet: PlanetModel.ALL ) {
            if ( planet.equals(PlanetModel.EARTH)) continue;
            celestObject.add(planet.at(m, p));
        }

        for (Star star : catalogue.stars()) {
            celestObject.add(star);
        }
    }

    public Sun sun(){
        return new Sun(null , null, 0, 0);
    }

    public CartesianCoordinates sunPosition(){
        return null;
    }

    public List< Planet > planets() {
        return null;
    }

    public double[] planetPosition(){
        return null;
    }
}
