package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;

public final class Preconditions
{
    // avoid any creation of intances
    private Preconditions()
    {
    }

    public static void checkArgument( boolean isTrue )
    {
        if ( !isTrue )
        {
            throw new IllegalArgumentException();
        }
    }

    public static double checkInInterval( Interval interval, double value )
    {
        if ( !interval.contains( value ) )
        {
            throw new IllegalArgumentException();
        }
        return value;
    }


}
