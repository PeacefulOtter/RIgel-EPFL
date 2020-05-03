package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableObjectValue;

public class ObserverLocationBean
{
    private DoubleProperty latDeg = new SimpleDoubleProperty(0);
    private DoubleProperty lonDeg = new SimpleDoubleProperty(0);
    private ObservableObjectValue<GeographicCoordinates> coordinates = Bindings.createObjectBinding(() ->
            GeographicCoordinates.ofDeg(lonDeg.doubleValue(), latDeg.doubleValue()), this.lonDeg, this.latDeg);

    public void setObserLocation(double lonDeg, double latDeg){
        this.latDeg.set(latDeg);
        this.lonDeg.set(lonDeg);
    }

    public void setCoordinates( GeographicCoordinates coordinates){
        this.latDeg.set(coordinates.latDeg());
        this.lonDeg.set(coordinates.lonDeg());
    }

    public double getLatDeg() {
        return latDeg.get();
    }

    public void setLatDeg(double latDeg) {
        this.latDeg.set(latDeg);
    }

    public void setLonDeg(double lonDeg) {
        this.lonDeg.set(lonDeg);
    }

    public DoubleProperty latDegProperty() {
        return latDeg;
    }

    public double getLonDeg() {
        return lonDeg.get();
    }

    public DoubleProperty lonDegProperty() {
        return lonDeg;
    }

    public GeographicCoordinates getCoordinates() {
        return coordinates.get();
    }

    public ObservableObjectValue coordinatesProperty() {
        return coordinates;
    }
}
