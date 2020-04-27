package ch.epfl.rigel.gui;

import java.time.Duration;

public enum NamedTimeAccelerator
{
    TIMES_1( "1x", TimeAccelerator.continous( 1 ) ),
    TIMES_30(  "30x", TimeAccelerator.continous( 30 ) ),
    TIMES_300( "300x", TimeAccelerator.continous( 300 ) ),
    TIMES_3000( "3000x", TimeAccelerator.continous( 3000 ) ),
    DAY( "jour", TimeAccelerator.discrete( 60, Duration.ofHours( 24 ) ) ),
    SIDEREAL_DAY( "jour sidéral", TimeAccelerator.discrete( 60, Duration.ofHours( 23 ).plusMinutes( 56 ).plusSeconds( 4 ) ) );

    private final String name;
    private final TimeAccelerator timeAccelerator;

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
