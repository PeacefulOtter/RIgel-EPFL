
package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;

import java.time.ZonedDateTime;
import java.util.Set;

public class ObservedSky {

    // pas sur de cette atribut
    private Set< CelestialObject > celestObject;

    public ObservedSky( ZonedDateTime moment, GeographicCoordinates position,
                        StereographicProjection projection, StarCatalogue catalogue ){
        //ce constructeur calcule la position projetée dans le plan de tous
        //les objets célestes, à savoir : le Soleil, la Lune, les planètes du système solaire — à l'exception de la Terre — et les étoiles du catalogue.
    }





}
