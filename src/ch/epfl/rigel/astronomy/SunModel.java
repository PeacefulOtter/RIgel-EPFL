package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

import javax.naming.ServiceUnavailableException;

public enum SunModel implements CelestialObjectModel<Sun> {

    SUN();

    private SunModel()
    {
    }

    @Override
    public Sun at(
            double daysSinceJ2010,
            EclipticToEquatorialConversion eclipticToEquatorialConversion )
    {
        // eclipticToEquatorialConversion.apply( EclipticCoordinates )
        return null;
    }
}
