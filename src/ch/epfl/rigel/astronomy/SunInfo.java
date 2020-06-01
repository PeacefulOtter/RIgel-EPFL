package ch.epfl.rigel.astronomy;

public enum SunInfo {

    // double degress, double earth distance, double radius,  double masse, double surface
    SUN( 5505, 149.6, 696340, 1.989, 6.0877 );

    // temperature in degrees of the Sun
    private final double degrees;
    // distance between eaarth in millions km
    private final double earthDistance;
    // radius of the Sun  in km
    private final double radius;
    // masse de la Sun in 10^30 kg
    private final double masse;
    // surfacce de la Sun in 10e12 km2
    private final double surface;

    SunInfo(double degrees, double earthDistance, double radius, double masse, double surface ) {
        this.degrees = degrees;
        this.earthDistance = earthDistance;
        this.radius = radius;
        this.masse = masse;
        this.surface = surface;
    }
}
