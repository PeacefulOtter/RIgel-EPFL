package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.math.ClosedInterval;

public enum MoonInfo {
    Moon( 120 , -180,384400, 1737, 1.62,  4.53, 38 );

    // temperature in Degrees of the planet
    private final ClosedInterval temperature;
    // distance between earth in km
    private final double earthDistance;
    // radius of the Moon in km
    private final double radius;
    // gravity on the moon
    private final double gravity;
    // age of the moon in billion of years
    private final double age;
    // surfacce de la Sun in million km2
    private final double surface;

    MoonInfo(double maxDegrees, double minDegrees, double earthDistance, double radius, double gravity,  double age, double surface ) {
        temperature = ClosedInterval.of(minDegrees, maxDegrees);
        this.earthDistance = earthDistance;
        this.radius = radius;
        this.gravity = gravity;
        this.age = age;
        this.surface = surface;
    }
}
