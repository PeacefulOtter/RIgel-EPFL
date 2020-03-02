package ch.epfl.rigel.astronomy;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

/**
 * set two astronomique Epoch
 */
public enum Epoch {
    J2000( LocalDateTime.of( 2000, Month.JANUARY, 1, 12, 0 ) ),
    J2010( LocalDateTime.of( 2010, Month.JANUARY, 1, 0, 0 ).minusDays(1) );

    private final LocalDateTime date;

    /**
     * @param date : LocalDateTime
     */
    Epoch( LocalDateTime date )
    {
        this.date = date;
    }

    /**
     * @param when: the actual time
     * @return the number of days between the Epoch where is apply and the actual time (when)
     */
    public double daysUntil( ZonedDateTime when )
    {
        return when.until( this.date, ChronoUnit.MILLIS );
    }

    /**
     * @param when : the actual time
     * @return the number of julians century between the Epoch where is apply and the actual time (when)
     */
    public double julianCenturiesUntil( ZonedDateTime when )
    {
        return when.until( this.date, ChronoUnit.CENTURIES );
    }
}
