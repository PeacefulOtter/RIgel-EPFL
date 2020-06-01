package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.math.ClosedInterval;

public enum PlanetInfo {

    MERCURY( 427, -173, 91, 2440, 3.7, 75 ),
    VENUS( 490, 446, 42, 6052, 8.9, 460 ),
    MARS(36, -140, 78,3389, 3.7, 145),
    JUPITER(-118,-129,628,69911, 24.8,61420),
    SATURN(-139,-201,1275,58232,10.4 ,42700),
    URANUS(-208 ,-212 ,2723,25362 ,8.9 ,8083),
    NEPTUNE(-210,-220,4351,24622 ,11.2 ,7618);


    // temperature in Degrees of the planet
    private final ClosedInterval temperature;
    // distance between earth in million of km
    private final double earthDistance;
    // radius of the planet in km
    private final double radius;
    // gravity on the planet m/se2
    private final double gravity;
    // surfacce de la planet in million km2
    private final double surface;

    PlanetInfo( double maxDegrees, double minDegrees, double earthDistance, double radius, double gravity,  double surface ) {
        temperature = ClosedInterval.of(minDegrees, maxDegrees);
        this.earthDistance = earthDistance;
        this.radius = radius;
        this.gravity = gravity;
        this.surface = surface;
    }
}


