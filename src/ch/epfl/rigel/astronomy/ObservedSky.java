
package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

public class ObservedSky
{
    private final List<PlanetModel> planetsWithoutEarth;
    private Set<CelestialObject> celestObject;

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
        //ce constructeur calcule la position projetée dans le plan de tous
        //les objets célestes, à savoir : le Soleil, la Lune, les planètes du système solaire — à l'exception de la Terre — et les étoiles du catalogue.
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
