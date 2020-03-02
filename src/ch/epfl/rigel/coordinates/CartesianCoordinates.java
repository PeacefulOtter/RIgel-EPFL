package ch.epfl.rigel.coordinates;

import java.util.Locale;

public final class CartesianCoordinates
{
    private final double x, y;

    private CartesianCoordinates( double x, double y )
    {
        this.x = x;
        this.y = y;
    }

    public static CartesianCoordinates of(double x, double y )
    {
        return new CartesianCoordinates( x, y );
    }

    double x()
    {
        return x;
    }

    double y()
    {
        return y;
    }

    @Override
    public int hashCode()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString()
    {
        return String.format( Locale.ROOT, "(x=%.4f, y=%.4f)", x(), y() );
    }
}
