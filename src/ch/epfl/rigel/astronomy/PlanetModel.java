package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

import java.util.Arrays;
import java.util.List;

public enum PlanetModel implements CelestialObjectModel<Planet> {


    MERCURY("Mercure", 0.24085, 75.5671, 77.612, 0.205627,
            0.387098, 7.0051, 48.449, 6.74, -0.42, "inner"),
    VENUS("Vénus", 0.615207, 272.30044, 131.54, 0.006812,
            0.723329, 3.3947, 76.769, 16.92, -4.40, "inner"),
    EARTH("Terre", 0.999996, 99.556772, 103.2055, 0.016671,
            0.999985, 0, 0, 0, 0, "ref"),
    MARS("Mars", 1.880765, 109.09646, 336.217, 0.093348,
            1.523689, 1.8497, 49.632, 9.36, -1.52, "outer"),
    JUPITER("Jupiter", 11.857911, 337.917132, 14.6633, 0.048907,
            5.20278, 1.3035, 100.595, 196.74, -9.40, "outer"),
    SATURN("Saturne", 29.310579, 172.398316, 89.567, 0.053853,
            9.51134, 2.4873, 113.752, 165.60, -8.88, "outer"),
    URANUS("Uranus", 84.039492, 271.063148, 172.884833, 0.046321,
            19.21814, 0.773059, 73.926961, 65.80, -7.19, "outer"),
    NEPTUNE("Neptune", 165.84539, 326.895127, 23.07, 0.010483,
            30.1985, 1.7673, 131.879, 62.20, -6.87, "outer");

    private static final double ANGULAR_SPEED = Angle.TAU / 365.242191;

    public static List<PlanetModel> ALL = Arrays.asList(
            MERCURY, VENUS, EARTH, MARS, JUPITER, SATURN, URANUS, NEPTUNE );

    private final String name;
    private final double revolutionPeriod;
    private final double lonJ2010;
    private final double lonPerigee;
    private final double orbitEccentricity;
    private final double halfOrbitMajorAxis;
    private final double inclinationOrbit;
    private final double lonAscendingNode;
    private final double angularSize;
    private final double magnitude;
    private final String type;

    private final double deltaLon;
    private final double eccentricitySquared;

    PlanetModel(
            String name, double revolutionPeriod, double lonJ2010,
            double lonPerigee, double orbitEccentricity, double halfOrbitMajorAxis,
            double inclinationOrbit, double lonAscendingNode, double angularSize,
            double magnitude, String type )
    {
        this.name = name;
        this.revolutionPeriod = revolutionPeriod;
        this.lonJ2010 = lonJ2010;
        this.lonPerigee = lonPerigee;
        this.orbitEccentricity = orbitEccentricity;
        this.halfOrbitMajorAxis = halfOrbitMajorAxis;
        this.inclinationOrbit = inclinationOrbit;
        this.lonAscendingNode = lonAscendingNode;
        this.angularSize = angularSize;
        this.magnitude = magnitude;
        this.type = type;

        this.deltaLon = lonJ2010 - lonPerigee;
        this.eccentricitySquared = Math.pow( orbitEccentricity, 2 );
    }

    @Override
    public Planet at(
            double daysSinceJ2010,
            EclipticToEquatorialConversion eclipticToEquatorialConversion )
    {
        double meanAnomaly = ANGULAR_SPEED * daysSinceJ2010 / revolutionPeriod + deltaLon;
        double trueAnomaly = meanAnomaly + 2 * orbitEccentricity * Math.sin( meanAnomaly );
        /** r **/
        double radius = ( halfOrbitMajorAxis * ( 1 - eccentricitySquared ) )  /  ( 1 + orbitEccentricity * Math.cos( trueAnomaly ) );
        /** l **/
        double longitude = trueAnomaly + lonPerigee;

        double deltaLonSin = Math.sin( longitude - lonAscendingNode );
        double deltaLonCos = Math.cos( longitude - lonAscendingNode );
        double inclinationCos = Math.cos( inclinationOrbit );
        double inclinationSin = Math.sin( inclinationOrbit );

        /** Psi **/
        double latitude = Math.asin( deltaLonSin * inclinationSin );

        /** r' **/
        double projectedRadius = radius * Math.cos( latitude );
        /** l' **/
        double projectedLongitude = Math.atan2( deltaLonSin * inclinationCos, deltaLonCos ) + lonAscendingNode;

        /* EQUATORIAL POS */
        /** lambda **/
        double eclipticLon;
        if ( type == "inner" )
        {
            eclipticLon = getInnerLon( projectedRadius, projectedLongitude );
        } else {
            eclipticLon = getOuterLon( projectedRadius, projectedLongitude );
        }
        /** beta **/
        double eclipticLat = Math.atan2(
                projectedRadius * Math.tan( latitude ) * Math.sin( eclipticLon - projectedLongitude ),
                EARTH_RADIUS * Math.sin( eclipticLon - EARTH_LON )
        );

        EclipticCoordinates eclipticPos = EclipticCoordinates.of( eclipticLon, eclipticLat );
        EquatorialCoordinates equatorialPos = eclipticToEquatorialConversion.apply( eclipticPos );
        /* END OF EQUATORIAL POS */


        /* ANGULAR SIZE */
        double distance = Math.abs( Math.sqrt(
                Math.pow( EARTH_RADIUS, 2 ) + Math.pow( radius, 2 ) - 2 * EARTH_RADIUS * radius * Math.cos( longitude - EARTH_LON ) * Math.cos( latitude )
        ) );
        float planetAngularSize = (float) ( angularSize / distance );
        /* END OF ANGULAR SIZE */


        /* MAGNITUDE */
        /** F **/
        double phase = ( 1 + Math.cos( longitude - EARTH_LON ) ) / 2;
        /** m **/
        float planetMagnitude = (float) ( magnitude + 5 * Math.log10( radius * distance / Math.sqrt( phase ) ) );
        /* END OF MAGNITUDE */

        
        return new Planet( name, equatorialPos, planetAngularSize, planetMagnitude );
    }


    /**
     * Get the Ecliptic Geocentric Longitude for inner planets
     * @param planetRadius
     * @param planetLon
     * @return ecliptic longitude
     */
    private double getInnerLon( double planetRadius, double planetLon )
    {
        double delta = EARTH_LON - planetLon;
        return Math.PI + EARTH_LON + Math.atan2(
                planetRadius * Math.sin( delta ),
                EARTH_RADIUS - planetRadius * Math.cos( delta )
        );
    }

    /**
     * Get the Ecliptic Geocentric Longitude for outer planets
     * @param planetRadius
     * @param planetLon
     * @return ecliptic longitude
     */
    private double getOuterLon( double planetRadius, double planetLon )
    {
        double delta = planetLon - EARTH_LON;
        return planetLon + Math.atan2(
                EARTH_RADIUS * Math.sin( delta ),
                planetRadius - EARTH_RADIUS * Math.cos( delta )
        );
    }
}
