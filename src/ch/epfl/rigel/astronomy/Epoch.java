package ch.epfl.rigel.astronomy;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

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
        double time = this.date.until( when, ChronoUnit.MILLIS );
        return time * 1.1574e-8; // convert in days
    }

    /**
     * @param when : the actual time
     * @return the number of julians century between the Epoch where is apply and the actual time (when)
     */
    public double julianCenturiesUntil( ZonedDateTime when )
    {
        return daysUntil(when) * 36.525; // convert in century
    }
}
