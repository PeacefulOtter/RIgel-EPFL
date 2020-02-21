package ch.epfl.rigel.coordinates;

abstract class SphericalCoordinates
{
    abstract double lon();

    abstract double lonDeg();

    abstract double lat();

    abstract double latDeg();

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
