package ch.epfl.rigel.gui;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a named time accelerator, i.e. a pair (name, accelerator)
 * The members of this type are the accelerators that will be available to the user
 * and the names will be those displayed on the screen.
 */
public enum NamedTimeAccelerator
{
    TIMES_1( "1x", TimeAccelerator.continuous( 1 ) ),
    TIMES_30(  "30x", TimeAccelerator.continuous( 30 ) ),
    TIMES_300( "300x", TimeAccelerator.continuous( 300 ) ),
    TIMES_3000( "3000x", TimeAccelerator.continuous( 3000 ) ),
    DAY( "jour", TimeAccelerator.discrete( 60, Duration.ofHours( 24 ) ) ),
    SIDEREAL_DAY( "jour sidéral", TimeAccelerator.discrete( 60, Duration.ofHours( 23 ).plusMinutes( 56 ).plusSeconds( 4 ) ) );

    private final String name;
    private final TimeAccelerator timeAccelerator;

    // Hashmap associating TimeAccelerator names to the TimeAccelerator
    public final static Map<String, TimeAccelerator> ACCELERATORS = new HashMap<>()
    {
        {
            put( TIMES_1.getName(), TIMES_1.getAccelerator() );
            put( TIMES_30.getName(), TIMES_30.getAccelerator() );
            put( TIMES_300.getName(), TIMES_300.getAccelerator() );
            put( TIMES_3000.getName(), TIMES_3000.getAccelerator() );
            put( DAY.getName(), DAY.getAccelerator() );
            put( SIDEREAL_DAY.getName(), SIDEREAL_DAY.getAccelerator() );
        }
    };

    NamedTimeAccelerator( String name, TimeAccelerator timeAccelerator )
    {
        this.name = name;
        this.timeAccelerator = timeAccelerator;
    }

    public String getName() { return name; }

    public TimeAccelerator getAccelerator() { return timeAccelerator; }

    @Override
    public String toString() { return getName(); }
}
