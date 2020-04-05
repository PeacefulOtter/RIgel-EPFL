
package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ObservedSky
{
    private final List<PlanetModel> planetsWithoutEarth;
    private final Set<CelestialObject> celestialObjects;
    private final Sun sun;
    private final Moon moon;

    public ObservedSky(
            ZonedDateTime moment,
            GeographicCoordinates position,
            StereographicProjection projection,
            StarCatalogue catalogue )
    {
        planetsWithoutEarth = PlanetModel.ALL;
        planetsWithoutEarth.remove( PlanetModel.EARTH );

        EclipticToEquatorialConversion conversion = new EclipticToEquatorialConversion( moment );
        double daysUntil = Epoch.J2000.daysUntil( moment );

        Sun sun = SunModel.SUN.at( daysUntil, conversion );

        celestialObjects = new HashSet<>();
        EclipticToEquatorialConversion p = new EclipticToEquatorialConversion( moment );
        double m = Epoch.J2000.daysUntil(moment);

        sun = SunModel.SUN.at(m, p);
        moon = MoonModel.MOON.at(m, p);
        celestialObjects.add(sun);
        celestialObjects.add(moon);
        for ( PlanetModel planet: PlanetModel.ALL ) {
            if ( planet.equals(PlanetModel.EARTH)) continue;
            celestialObjects.add(planet.at(m, p));
        }

        for (Star star : catalogue.stars()) {
            celestialObjects.add(star);
        }
    }


    public Sun sun(){
        return new Sun(null , null, 0, 0);
    }

    public CartesianCoordinates sunPosition(){
        return null;
    }

    public Moon moon()
    {
        return null;
    }

    public CartesianCoordinates moonPosition()
    {
        return null;
    }

    public List< Planet > planets() {
        return null;
    }

    public double[] planetPosition(){
        return null;
    }
}
