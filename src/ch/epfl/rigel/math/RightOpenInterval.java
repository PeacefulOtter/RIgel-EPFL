package ch.epfl.rigel.math;

import java.util.Locale;

public class RightOpenInterval extends Interval
{
    protected RightOpenInterval( double lowerBound, double upperBound )
    {
        super(lowerBound, upperBound);
    }

    public static RightOpenInterval of( double low, double high ) {
        if ( low >= high ) { throw new IllegalArgumentException(); }
        return new RightOpenInterval( low, high );
    }

    public static RightOpenInterval symmetric( double size ) {
        if ( size <= 0 ) { throw new IllegalArgumentException(); }
        return new RightOpenInterval( -size/2, size/2 );
    }

    @Override
    public boolean contains(double v)
    {
        return ( v >= low() && v < high() );
    }

    private double floorMod( double x, double y ) {
        return x - y * Math.floor( x / y );
    }

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


