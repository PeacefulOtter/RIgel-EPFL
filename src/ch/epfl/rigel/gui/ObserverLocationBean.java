package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableObjectValue;

public class ObserverLocationBean
{
    private DoubleProperty latDeg, lonDeg;

    private ObservableObjectValue<GeographicCoordinates> coordinates;
    // peut etre pas mettre de constructeur et mettre des setter comme dans viewingParameters
    public ObserverLocationBean(double lonDeg, double latDeg) {
        this.latDeg = new SimpleDoubleProperty( latDeg );
        this.lonDeg = new SimpleDoubleProperty( lonDeg );
        coordinates = Bindings.createObjectBinding(() -> GeographicCoordinates.ofDeg(lonDeg, latDeg), this.lonDeg, this.latDeg);
    }

    public double getLatDeg() {
        return latDeg.get();
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
