package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;

import java.time.*;

public final class DateTimeBean
{
    private ObjectProperty<LocalDate> dateProperty = null;
    private ObjectProperty<LocalTime> timeProperty = null;
    private ObjectProperty<ZoneId> zoneProperty = null;

    public ObjectProperty<LocalDate> dateProperty() { return dateProperty; }

    public LocalDate getDate() { return dateProperty.getValue(); }

    public void setDate( LocalDate date )
    {
        dateProperty.setValue( LocalDate.of( date.getYear(), date.getMonthValue(), date.getDayOfMonth() ) );
    }


    public ObjectProperty<LocalTime> timeProperty() { return timeProperty; }

    public LocalTime getTime() { return timeProperty.getValue(); }

    public void setTime( LocalTime time )
    {
        timeProperty.setValue( LocalTime.of( time.getHour(), time.getMinute(), time.getSecond(), time.getNano() ) );
    }


    public ObjectProperty<ZoneId> zoneProperty() { return zoneProperty; }

    public ZoneId getZone() { return zoneProperty.getValue(); }

    public void setZone( ZoneId zone ) { zoneProperty.setValue( zone ); }


    public ZonedDateTime getZonedDateTime() { return ZonedDateTime.of( LocalDateTime.of( getDate(), getTime() ), getZone() ); }

    public void setZonedDateTime( ZonedDateTime zonedDateTime )
    {
        setDate( zonedDateTime.toLocalDate() );
        setTime( zonedDateTime.toLocalTime() );
        setZone( zonedDateTime.getZone() );
    }
}
