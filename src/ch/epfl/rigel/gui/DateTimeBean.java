package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.*;

/**
 * containing the instant of observation, the triplet (date, time, time zone) of observation.
 * For all properties there is a getProperty, a getValue and a set
 * All properties value can be get/set at one time using a ZonedDateTime which is very convenient
 */
public final class DateTimeBean
{
    private final ObjectProperty<LocalDate> dateProperty = new SimpleObjectProperty<>( null );
    private final ObjectProperty<LocalTime> timeProperty = new SimpleObjectProperty<>( null );
    private final ObjectProperty<ZoneId>    zoneProperty = new SimpleObjectProperty<>( null );


    /** Date **/
    public ObjectProperty<LocalDate> dateProperty() { return dateProperty; }

    public LocalDate getDate() { return dateProperty.getValue(); }

    public void setDate( LocalDate date ) { dateProperty.set( date ); }


    /** Time **/
    public ObjectProperty<LocalTime> timeProperty() { return timeProperty; }

    public LocalTime getTime() { return timeProperty.getValue(); }

    public void setTime( LocalTime time ) { timeProperty.setValue( time ); }


    /** Zone **/
    public ObjectProperty<ZoneId> zoneProperty() { return zoneProperty; }

    public ZoneId getZone() { return zoneProperty.getValue(); }

    public void setZone( ZoneId zone ) { zoneProperty.setValue( zone ); }


    /** ZonedDateTime **/
    public ZonedDateTime getZonedDateTime() { return ZonedDateTime.of( LocalDateTime.of( getDate(), getTime() ), getZone() ); }

    public void setZonedDateTime( ZonedDateTime zonedDateTime )
    {
        setDate( zonedDateTime.toLocalDate() );
        setTime( zonedDateTime.toLocalTime() );
        setZone( zonedDateTime.getZone() );
    }
}
