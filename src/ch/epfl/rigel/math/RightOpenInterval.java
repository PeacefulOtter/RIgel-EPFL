package ch.epfl.rigel.math;

import java.util.Locale;

public class RightOpenInterval extends Interval
{
    /**
     * @param lowerBound : the lower bound
     * @param upperBound : the upper bound
     */
    protected RightOpenInterval( double lowerBound, double upperBound )
    {
        super(lowerBound, upperBound);
    }

    /**
     * Create a new RightOpenInterval
     * @param low : the lower bound
     * @param high : the upper bound
     * @return the new interval
     */
    public static RightOpenInterval of( double low, double high ) {
        if ( low >= high ) { throw new IllegalArgumentException(); }
        return new RightOpenInterval( low, high );
    }

    /**
     * Create a symmetric interval, where 0 is the middle value
     * @param size
     * @return
     */
    public static RightOpenInterval symmetric( double size ) {
        if ( size <= 0 ) { throw new IllegalArgumentException(); }
        return new RightOpenInterval( -size/2, size/2 );
    }

    /**
     * Check if some value is inside the interval
     * @param v : the value
     * @return boolean : true if inside, false if not
     */
    @Override
    public boolean contains(double v)
    {
        return ( v >= low() && v < high() );
    }

    private double floorMod( double x, double y ) {
        return x - y * Math.floor( x / y );
    }

    /**
     * Contain any value to the interval
     * @param v : the value to contain
     * @return the contained value
     */
    public double reduce( double v )
    {
        return low() + floorMod( v - low(), high() - low() );
    }

    @Override
    public String toString()
    {
        return String.format( Locale.ROOT, "[%s,%s[", low(), high() );
    }

}


