package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableObjectValue;

public class ObserverLocationBean
{
    private final DoubleProperty lonDegObservator = new SimpleDoubleProperty( 0 );
    private final DoubleProperty latDegObservator = new SimpleDoubleProperty( 0 );
    private final ObservableObjectValue<GeographicCoordinates> coordinates = Bindings.createObjectBinding( () ->
        GeographicCoordinates.ofDeg( lonDegObservator.getValue(), latDegObservator.getValue() ),
        this.lonDegObservator, this.latDegObservator );


    public void setObserverLocation( double lonDeg, double latDeg )
    {
        this.lonDegObservator.setValue( lonDeg );
        this.latDegObservator.setValue( latDeg );
    }


    public DoubleProperty lonDegProperty()
    {
        return lonDegObservator;
    }

    public double getLonDeg()
    {
        return lonDegObservator.getValue();
    }

    public void setLonDeg( double lonDeg )
    {
        this.lonDegObservator.setValue( lonDeg );
    }


    public DoubleProperty latDegProperty()
    {
        return latDegObservator;
    }

    public double getLatDeg()
    {
        return latDegObservator.getValue();
    }

    public void setLatDeg( double latDeg )
    {
        this.latDegObservator.setValue( latDeg );
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

    public ObservableObjectValue coordinatesProperty()
    {
        return coordinates;
    }
}
