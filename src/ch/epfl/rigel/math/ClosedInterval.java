package ch.epfl.rigel.math;

import java.util.Locale;

public final class ClosedInterval extends Interval
{
    private ClosedInterval( double lowerBound, double upperBound )
    {
        super( lowerBound, upperBound );
    }

    public static ClosedInterval of( double low, double high ) {
        if ( low >= high ) { throw new IllegalArgumentException(); }
        return new ClosedInterval( low, high );
    }

    public static ClosedInterval symmetric( double size ) {
        if ( size <= 0 ) { throw new IllegalArgumentException(); }
        return new ClosedInterval( -size/2, size/2 );
    }

    public double clip( double v ) {
        if ( v <= low() ) { return low(); }
        else if ( v >= high() ) { return high(); }
        return v;
    }

    @Override
    boolean contains(double v)
    {
        return ( v >= low() && v <= high() );
    }

    @Override
    public String toString()

    {
        return String.format( Locale.ROOT, "[%s,%s]", low(), high() );
    }
}

