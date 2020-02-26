package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

abstract class SphericalCoordinates
{
    // longitude
    private final double lon;
    // latitude
    private final double lat;

    public SphericalCoordinates( double lon, double lat )
    {
        this.lon = lon;
        this.lat = lat;
    }

    public double lon() {
        return lon;
    };

    public double lonDeg()  {
        return Angle.toDeg( lon );
    }

    public double lat() {
        return lat;
    }

    public double latDeg() {
        return Angle.toDeg( lat );
    }

    @Override
    public final int hashCode()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean equals(Object obj)
    {
        throw new UnsupportedOperationException();
    }
}
