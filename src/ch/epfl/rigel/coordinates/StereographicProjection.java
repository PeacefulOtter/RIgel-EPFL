package ch.epfl.rigel.coordinates;

import java.util.Locale;
import java.util.function.Function;

public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates>
{
    private final HorizontalCoordinates center;
    private final double lambda0;
    private final double phi1;
    private final double cosLambda0;
    private final double sinLambda0;
    private final double cosPhi1;
    private final double sinPhi1;

    public StereographicProjection( HorizontalCoordinates center )
    {
        this.center = center;
        lambda0 = center.lon();
        phi1 = center.lat();
        cosLambda0 = Math.cos( lambda0 );
        sinLambda0 = Math.sin( lambda0 );
        cosPhi1 = Math.cos( phi1 );
        sinPhi1 = Math.sin( phi1 );
    }

    public CartesianCoordinates circleCenterForParallel( HorizontalCoordinates hor )
    {
        if ( ( Math.sin( hor.alt() ) + sinPhi1 ) == 0){
            return CartesianCoordinates.of(0, Double.POSITIVE_INFINITY); // a voir coordonne infinie ??
        }
        double cy = cosPhi1 / ( Math.sin( hor.alt() ) + sinPhi1 );
        return CartesianCoordinates.of( 0, cy );
    }

    public double circleRadiusForParallel( HorizontalCoordinates parallel )
    {
        if ( Math.sin( parallel.lon() ) + sinPhi1 == 0){
            return Double.POSITIVE_INFINITY;
        }
        return Math.cos( parallel.lat() ) / ( Math.sin( parallel.lat() ) + sinPhi1 );
    }

    public double applyToAngle( double rad )
    {
        return 2 * Math.tan( rad / 4 );
    }

    @Override
    public CartesianCoordinates apply( HorizontalCoordinates azAlt )
    {
        double cosLambda = Math.cos( azAlt.az() );
        double sinLambda = Math.sin( azAlt.az() );
        double cosPhi = Math.cos( azAlt.alt() );
        double sinPhi = Math.sin( azAlt.alt() );
        double sinDeltaLambda = Math.sin(azAlt.az() - lambda0);
        double cosDeltaLambda = Math.cos(azAlt.az() - lambda0) ;

        double d = 1 / (1 + sinPhi * sinPhi1 + cosPhi * cosPhi1 * cosDeltaLambda);
        // peut etre mettre une valeur infinie

        double x = d * cosPhi * sinDeltaLambda;
        double y = d * (sinPhi * cosPhi1 - cosPhi * sinPhi1 * cosDeltaLambda);

        return CartesianCoordinates.of( x, y );
    }

    public HorizontalCoordinates inverseApply( CartesianCoordinates xy )
    {
        double x = xy.x();
        double y = xy.y();
        double p = Math.sqrt( Math.pow(x, 2) + Math.pow(y, 2) );
        double cosC = (1 - Math.pow(p, 2)) / (Math.pow(p, 2) + 1);
        double sinC = (2 * p) / (Math.pow(p, 2) + 1) ;

        double lambda = Math.atan2(x * sinC, p * cosPhi1 * cosC - y * sinPhi1 * sinC) + lambda0;
        double phi = Math.asin(cosC * sinPhi1 + (y * sinC * cosPhi1 / p));
        /**
                BOUND TO INTERVAL
        **/
        return HorizontalCoordinates.of(lambda, phi);
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
