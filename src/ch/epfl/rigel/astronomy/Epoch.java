package ch.epfl.rigel.astronomy;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZonedDateTime;

public enum Epoch {
    J2000(LocalDateTime.of(2000, Month.JANUARY, 1, 12, 0)),
    J2010(LocalDateTime.of(2010, Month.JANUARY, 1).minusDays(1));

    private LocalDateTime date ;
    private Epoch( LocalDateTime date){
        this.date = date;
    }

    public double daysUntil(ZonedDateTime when){

    }

    public double julianCenturiesUntil(ZonedDateTime when){

    }
}
