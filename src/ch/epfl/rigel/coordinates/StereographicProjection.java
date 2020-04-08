package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;
import java.util.function.Function;

/**
 * Represents a Stereographic Projection of Horizontal Coordinates
 */
public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates>
{
    // Interval of the longitude in radians
    private static final RightOpenInterval AZ_INTERVAL =  RightOpenInterval.of( 0, Angle.TAU );
    // Interval of the latitude in radians
    private static final ClosedInterval ALT_INTERVAL = ClosedInterval.of( -Math.PI / 2, Math.PI / 2 );

    // Horizontal Coordinates of the center
    private final HorizontalCoordinates center;

    private final double lambda0;
    private final double phi1;
    private final double cosPhi1;
    private final double sinPhi1;

    /**
     * Returns a stereographic projection centered in "center"
     * @param center : the center of the stereographic projection
     */
    public StereographicProjection( HorizontalCoordinates center )
    {
        this.center = center;
        // store some variables we will need for calculations
        lambda0 = center.lon();
        phi1 = center.lat();
        cosPhi1 = Math.cos( phi1 );
        sinPhi1 = Math.sin( phi1 );
    }

    /**
     * @param hor : a point in HorizontalCoordinates
     * @return :the coordinates of the centre of the circle corresponding to the projection of the parallel passing through the point hor
     */
    public CartesianCoordinates circleCenterForParallel( HorizontalCoordinates hor )
    {
        if ( ( Math.sin( hor.alt() ) + sinPhi1 ) == 0 )
        {
            return CartesianCoordinates.of(0, Double.POSITIVE_INFINITY );
        }
        double cy = cosPhi1 / ( Math.sin( hor.alt() ) + sinPhi1 );
        return CartesianCoordinates.of( 0, cy );
    }

    /**
     *
     * @param parallel
     * @return : the radius of the circle corresponding to the projection of the parallel passing through the coordinate point hor
     */
    public double circleRadiusForParallel( HorizontalCoordinates parallel )
    {
        if ( Math.sin( parallel.lon() ) + sinPhi1 == 0 )
        {
            return Double.POSITIVE_INFINITY;
        }
        return Math.cos( parallel.lat() ) / ( Math.sin( parallel.lat() ) + sinPhi1 );
    }

    /**
     * @param rad the angular size of the sphere
     * @return : the projected diameter of a sphere of angular size rad centred at the centre of projection, assuming that it is on the horizon
     */
    public double applyToAngle( double rad )
    {
        return 2 * Math.tan( rad / 4 );
    }

    /**
     * @param azAlt the Horizontal coordinates point
     * @return : the Cartesian coordinates of the projection of the horizontal coordinate point azAlt
     */
    @Override
    public CartesianCoordinates apply( HorizontalCoordinates azAlt )
    {
        double lon = azAlt.az();
        double lat = azAlt.alt();
        double cosPhi = Math.cos( lat );
        double sinPhi = Math.sin( lat ) ;
        double sinDeltaLambda = Math.sin( lon - lambda0 );
        double cosDeltaLambda = Math.cos( lon - lambda0 ) ;

        double d = 1 / ( 1 + sinPhi * sinPhi1 + cosPhi * cosPhi1 * cosDeltaLambda );
        double x = d * cosPhi * sinDeltaLambda;
        double y = d * ( sinPhi * cosPhi1 - cosPhi * sinPhi1 * cosDeltaLambda );

        return CartesianCoordinates.of( x, y );
    }

    /**
     * @param xy Cartesian coordinates of a point
     * @return : the horizontal coordinates of the point whose projection is the point of Cartesian coordinates xy
     */
    public HorizontalCoordinates inverseApply( CartesianCoordinates xy )
    {
        double x = xy.x();
        double y = xy.y();
        double p = Math.sqrt( Math.pow( x, 2 ) + Math.pow( y, 2 ) );
        double cosC = ( 1 - Math.pow( p, 2 ) ) / ( Math.pow( p, 2 ) + 1 );
        double sinC = ( 2 * p ) / ( Math.pow( p, 2 ) + 1 ) ;

        double lambda = Math.atan2( x * sinC, p * cosPhi1 * cosC - y * sinPhi1 * sinC ) + lambda0;
        double phi = Math.asin( cosC * sinPhi1 + ( y * sinC * cosPhi1 / p ) );

        return HorizontalCoordinates.of( AZ_INTERVAL.reduce( lambda ), ALT_INTERVAL.clip( phi ) );
    }

    @Override
    public boolean equals( Object o ) { throw new UnsupportedOperationException(); }

    @Override
    public int hashCode() { throw new UnsupportedOperationException(); }

    @Override
    public String toString()
    {
        return String.format( Locale.ROOT, "StereographicProjection(x=%.4f, y=%.4f)", center.lon(), center.lat());
    }
}
