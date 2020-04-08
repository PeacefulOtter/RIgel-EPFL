package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

/**
 * Parent class of all classes that represents spherical coordinates
 */
abstract class SphericalCoordinates
{
    // longitude and latitude
    private final double lon, lat;

    public SphericalCoordinates( double lon, double lat )
    {
        this.lon = lon;
        this.lat = lat;
    }

    /**
     * @return longitude in radians
     */
    public double lon() { return lon; }

    /**
     * @return the longitude in degrees
     */
    public double lonDeg() { return Angle.toDeg( lon ); }

    /**
     * @return the latitude in radians
     */
    public double lat() { return lat; }

    /**
     * @return the latitude in degrees
     */
    public double latDeg() { return Angle.toDeg( lat ); }

    @Override
    public final int hashCode() { throw new UnsupportedOperationException(); }

    @Override
    public final boolean equals( Object obj ) { throw new UnsupportedOperationException(); }
}
