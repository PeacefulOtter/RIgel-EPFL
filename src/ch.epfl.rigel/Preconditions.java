package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;

public final class Preconditions
{
    private Preconditions()
    {
    }

    void checkArgument( boolean isTrue )
    {
        if ( !isTrue )
        {
            throw new IllegalArgumentException();
        }
    }

    double checkInInterval( Interval interval, double value )
    {
        if ( value < interval.low() || value > interval.high() )
        {
            throw new IllegalArgumentException();
        }
        return value;
    }


}
