package ch.epfl.rigel.astronomy;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public enum Epoch {
    J2000( LocalDateTime.of( 2000, Month.JANUARY, 1, 12, 0 ) ),
    J2010( LocalDateTime.of( 2010, Month.JANUARY, 1, 0, 0 ).minusDays(1) );

    private final LocalDateTime date;

    Epoch( LocalDateTime date )
    {
        this.date = date;
    }

    public double daysUntil( ZonedDateTime when )
    {
        return when.until( this.date, ChronoUnit.MILLIS );
    }

    public double julianCenturiesUntil( ZonedDateTime when )
    {
        return when.until( this.date, ChronoUnit.CENTURIES ); // ???
    }
}
