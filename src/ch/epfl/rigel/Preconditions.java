package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;

// Represents some preconditions
public final class Preconditions
{
    // avoid any creation of intances
    private Preconditions() { }

    /**
     * throw an IllegalArgumentException if the statement passed in params is false
     * @param isTrue : statement to test
     */
    public static void checkArgument( boolean isTrue )
    {
        if ( !isTrue )
        {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Check if a value is contained in the given interval
     * @param interval : the interval which the value must be contained
     * @param value : the value to be tested
     * @return the value if the interval contains the value or throw IllegalArgumentException
     */
    public static double checkInInterval( Interval interval, double value )
    {
        if ( !interval.contains( value ) )
        {
            throw new IllegalArgumentException();
        }
        return value;
    }


}
