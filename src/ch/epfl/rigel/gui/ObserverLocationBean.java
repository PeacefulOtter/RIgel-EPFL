package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableObjectValue;
/**
 * Represents the position of the observer in Geographic Coordinates
 */
public class ObserverLocationBean
{
    // longitude of the observer in degrees
    private final DoubleProperty lonDegObserver = new SimpleDoubleProperty( 0 );
    // latitude of the observer in degrees
    private final DoubleProperty latDegObserver = new SimpleDoubleProperty( 0 );
    // the geographic coordinates, bound to the observer longitude and latitude
    private final ObservableObjectValue<GeographicCoordinates> coordinates = Bindings.createObjectBinding( () ->
        GeographicCoordinates.ofDeg( lonDegObserver.getValue(), latDegObserver.getValue() ),
        this.lonDegObserver, this.latDegObserver );

    /* Longitude */
    public DoubleProperty lonDegProperty()
    {
        return lonDegObserver;
    }

    public double getLonDeg()
    {
        return lonDegObserver.getValue();
    }

    public void setLonDeg( double lonDeg )
    {
        this.lonDegObserver.setValue( lonDeg );
    }

    /* Latitude */
    public DoubleProperty latDegProperty()
    {
        return latDegObserver;
    }

    public double getLatDeg()
    {
        return latDegObserver.getValue();
    }

    public void setLatDeg( double latDeg )
    {
        this.latDegObserver.setValue( latDeg );
    }


    /* Geographic Coordinates */
    public ObservableObjectValue<GeographicCoordinates> coordinatesProperty()
    {
        return coordinates;
    }

    public GeographicCoordinates getCoordinates()
    {
        return coordinates.getValue();
    }

    public void setCoordinates( GeographicCoordinates geographicCoordinates )
    {
        setLonDeg( geographicCoordinates.lonDeg() );
        setLatDeg( geographicCoordinates.latDeg() );
    }
}
