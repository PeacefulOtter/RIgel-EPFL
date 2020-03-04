package ch.epfl.rigel.coordinates;

import java.util.Locale;
import java.util.function.Function;

public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates>
{
    private final HorizontalCoordinates center;

    public StereographicProjection( HorizontalCoordinates center )
    {
        this.center = center;
        // /!\ Calculate as much values as possible
    }

    @Override
    public CartesianCoordinates apply( HorizontalCoordinates azAlt )
    {
        return null;
    }

    public CartesianCoordinates circleCenterForParallel( HorizontalCoordinates hor )
    {
        return null;
    }

    public double circleRadiusForParallel( HorizontalCoordinates parallel )
    {
        return 0;
    }

    public double applyToAngle( double rad )
    {
        return 0;
    }

    public HorizontalCoordinates inverseApply( CartesianCoordinates xy )
    {
        return null;
    }

    @Override
    public boolean equals(Object o)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString()
    {
        return String.format( Locale.ROOT, "StereographicProjection(x=%.4f, y=%.4f)", center.lon(), center.lat());
    }
}
