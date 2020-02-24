package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

abstract class SphericalCoordinates
{
    private final double lon;
    private final double lat;

    public SphericalCoordinates( double lon, double lat )
    {
        this.lon = lon;
        this.lat = lat;
    }

    double lon() {
        return lon;
    };

    double lonDeg()  {
        return Angle.toDeg( lon );
    }

    double lat() {
        return lat;
    }

    double latDeg() {
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
