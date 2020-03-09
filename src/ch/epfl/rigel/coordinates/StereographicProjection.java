package ch.epfl.rigel.coordinates;

import java.util.Locale;
import java.util.function.Function;

public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates>
{
    private final HorizontalCoordinates center;
    private final double lambda0;
    private final double phi1;
    private final double cosLambda;
    private final double sinLambda;
    private final double cosPhi;
    private final double sinPhi;

    public StereographicProjection( HorizontalCoordinates center )
    {
        this.center = center;
        lambda0 = center.lon();
        phi1 = center.lat();
        cosLambda = Math.cos( lambda0 );
        sinLambda = Math.sin( lambda0 );
        cosPhi = Math.cos( phi1 );
        sinPhi = Math.sin( phi1 );
        // /!\ Calculate as much values as possible
    }

    @Override
    public CartesianCoordinates apply( HorizontalCoordinates azAlt )
    {
        return null;
    }

    public CartesianCoordinates circleCenterForParallel( HorizontalCoordinates hor )
    {
        double cy = cosPhi / ( Math.sin( hor.alt() ) + sinPhi );
        return CartesianCoordinates.of( 0, cy );
    }

    public double circleRadiusForParallel( HorizontalCoordinates parallel )
    {
        return Math.cos( parallel.lat() ) / ( Math.sin( parallel.lon() ) + sinPhi );
    }

    public double applyToAngle( double rad )
    {
        return 2 * Math.tan( rad / 4 );
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
