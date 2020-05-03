package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

public final class ClosedInterval extends Interval
{
    /**
     * ClosedInterval Constructor
     * @param lowerBound
     * @param upperBound
     */
    private ClosedInterval( double lowerBound, double upperBound )
    {
        super( lowerBound, upperBound );
    }

    /**
     * Create a closed interval
     * @param low : lower bound
     * @param high : upper bound
     * @throws IllegalArgumentException if low is greater or equal to high
     * @return the interval
     */
    public static ClosedInterval of( double low, double high )
    {
        Preconditions.checkArgument(  low >= high );
        return new ClosedInterval( low, high );
    }

    /**
     * Create a symmetric interval -> the middle value is 0
     * @param size : the interval size
     * @return the interval
     */
    public static ClosedInterval symmetric( double size )
    {
        Preconditions.checkArgument(   size <= 0 );
        return new ClosedInterval( -size / 2, size / 2 );
    }

    /**
     * Contain a value to the interval
     * @param v : the value we want to contain
     * @return the contained value
     */
    public double clip( double v )
    {
        if ( v <= low() ) { return low(); }
        else if ( v >= high() ) { return high(); }
        return v;
    }

    /**
     * Check if some value is inside the interval or not
     * @param v : the value we want to check
     * @return boolean : true if inside, false if not
     */
    @Override
    public boolean contains( double v )
    {
        return ( v >= low() && v <= high() );
    }


    @Override
    public String toString()
    {
        return String.format( Locale.ROOT, "[%s,%s]", low(), high() );
    }
}

