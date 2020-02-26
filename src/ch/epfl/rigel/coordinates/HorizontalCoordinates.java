package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

public final class HorizontalCoordinates extends SphericalCoordinates
{
    // Interval of longitude
    private static final RightOpenInterval azInterval =  RightOpenInterval.of( 0, Angle.TAU );
    // Interval of latitude
    private static final ClosedInterval altInterval = ClosedInterval.of( -Math.PI / 2, Math.PI / 2 );

    // Interval of longitude in degrees
    private static final RightOpenInterval azDegInterval =  RightOpenInterval.of( 0, 360 );

    // Interval of latitude in degrees
    private static final ClosedInterval altDegInterval = ClosedInterval.of( -90, 90 );

    private HorizontalCoordinates( double az, double alt )
    {
        super( az, alt );
    }

    // methode of construction
    // throw exception if the interval not contains the values
    public static HorizontalCoordinates of( double az, double alt )
    {
        if ( !azInterval.contains( az ) || !altInterval.contains( alt ) ) {
            throw new IllegalArgumentException();
        }
        return new HorizontalCoordinates( az, alt );
    }

    // methode of construction in degrees
    // throw exception if the interval in degrees not contains the values
    public static HorizontalCoordinates ofDeg( double azDeg, double altDeg )
    {
        if ( !azDegInterval.contains( azDeg ) || !altDegInterval.contains( altDeg ) ) {
            throw new IllegalArgumentException();
        }
        return new HorizontalCoordinates( Angle.ofDeg( azDeg ), Angle.ofDeg( altDeg ) );
    }

    public double az()
    {
        return lon();
    }

    public double azDeg()
    {
        return lonDeg();
    }

    public double alt() { return lat(); }

    public double altDeg() { return latDeg(); }

    /**
     * @return the direction (N, E, S, W) of the longitude angle
     */
    public String azOctantName( String n, String e, String s, String w )
    {
        double azDeg = azDeg();
        if ( azDeg <= 22.5 ||     azDeg >  337.5 ) { return n; }
        else if( azDeg > 22.5  && azDeg <= 67.5 )  { return n + e; }
        else if( azDeg > 67.5  && azDeg <= 112.5 ) { return e; }
        else if( azDeg > 112.5 && azDeg <= 157.5 ) { return s + e; }
        else if( azDeg > 157.5 && azDeg <= 202.5 ) { return s; }
        else if( azDeg > 202.5 && azDeg <= 247.5 ) { return s + w; }
        else if( azDeg > 247.5 && azDeg <= 292.5 ) { return w; }
        else if( azDeg > 292.5 && azDeg <= 337.5 ) { return n + w; }
        else { throw new IllegalArgumentException(); }
    }

    public double angularDistanceTo( HorizontalCoordinates that )
    {
        return Math.acos(Math.sin(that.alt()) * Math.sin(this.alt()) + Math.cos(that.alt()) * Math.cos(this.alt()) * Math.cos(that.az() - this.az()));
    }


    @Override
    public String toString()
    {
        return String.format( Locale.ROOT, "(az=%.4f°, alt=%.4f°)", az(), alt() );
    }
}
