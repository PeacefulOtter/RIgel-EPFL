package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.CelestialObjectModel;
import ch.epfl.rigel.astronomy.Planet;

import java.util.Arrays;
import java.util.List;

public enum PlanetModel implements CelestialObjectModel<Planet> {


    MERCURY("Mercure", 0.24085, 75.5671, 77.612, 0.205627,
            0.387098, 7.0051, 48.449, 6.74, -0.42),
    VENUS("Vénus", 0.615207, 272.30044, 131.54, 0.006812,
            0.723329, 3.3947, 76.769, 16.92, -4.40),
    EARTH("Terre", 0.999996, 99.556772, 103.2055, 0.016671,
            0.999985, 0, 0, 0, 0),
    MARS("Mars", 1.880765, 109.09646, 336.217, 0.093348,
            1.523689, 1.8497, 49.632, 9.36, -1.52),
    JUPITER("Jupiter", 11.857911, 337.917132, 14.6633, 0.048907,
            5.20278, 1.3035, 100.595, 196.74, -9.40),
    SATURN("Saturne", 29.310579, 172.398316, 89.567, 0.053853,
            9.51134, 2.4873, 113.752, 165.60, -8.88),
    URANUS("Uranus", 84.039492, 271.063148, 172.884833, 0.046321,
            19.21814, 0.773059, 73.926961, 65.80, -7.19),
    NEPTUNE("Neptune", 165.84539, 326.895127, 23.07, 0.010483,
            30.1985, 1.7673, 131.879, 62.20, -6.87);


    public static List<PlanetModel> ALL = Arrays.asList(MERCURY, VENUS, EARTH, MARS, JUPITER, SATURN, URANUS, NEPTUNE);

    private String frenchName;
    private double revolutionPeriod;
    private double lonJ2010;
    private double lonPerigee;
    private double orbitEccentricity;
    private double halfOrbitMajorAxis;
    private double inclinationOrbit;
    private double lonAscendingNode;
    private double angularSize;
    private double magnitude;
    // j ai mis des types double par default pour tous
    PlanetModel ( String frenchName, double revolutionPeriod, double lonJ2010, double lonPerigee,
                  double orbitEccentricity, double halfOrbitMajorAxis, double inclinationOrbit, double lonAscendingNode,
                  double angularSize, double magnitude){
        this.frenchName = frenchName;
        this.revolutionPeriod = revolutionPeriod;
        this.lonJ2010 = lonJ2010;
        this.lonPerigee = lonPerigee;
        this.orbitEccentricity = orbitEccentricity;
        this.halfOrbitMajorAxis = halfOrbitMajorAxis;
        this.inclinationOrbit = inclinationOrbit;
        this.lonAscendingNode = lonAscendingNode;
        this.angularSize = angularSize;
        this.magnitude = magnitude;

    }

    @Override
    public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        return null;
    }
}
