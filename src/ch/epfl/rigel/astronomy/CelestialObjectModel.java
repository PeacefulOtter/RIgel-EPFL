package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

/**
 * Model of any Celestial Object
 */
public interface CelestialObjectModel<O>
{
   /**
    * @param daysSinceJ2010 : number of days after the J2010 (possibly negative)
    * @param eclipticToEquatorialConversion : Conversion used to get its equatorial coordinates from its ecliptic coordinates
    * @return the object O modeled by the model for the number of days after the J2010 epoch
    * and using the given conversion.
    **/
   O at(
           double daysSinceJ2010,
           EclipticToEquatorialConversion eclipticToEquatorialConversion );
}
