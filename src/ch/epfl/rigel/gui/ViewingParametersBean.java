package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.*;


public class ViewingParametersBean
{
    // field of view in degrees
    private final DoubleProperty fieldOfViewDeg = new SimpleDoubleProperty( 0 );
    // center of the projection
    private final ObjectProperty<HorizontalCoordinates> center = new SimpleObjectProperty<>( null );


    public DoubleProperty fieldOfViewDegProperty() { return fieldOfViewDeg; }

    public Double getFieldOfViewDeg() { return fieldOfViewDeg.getValue(); }

    public void setFieldOfViewDeg( Double newFieldOfViewDeg )
    {
        fieldOfViewDeg.setValue( newFieldOfViewDeg );
    }

    public ObjectProperty<HorizontalCoordinates> centerProperty() { return center; }

    public HorizontalCoordinates getCenter() { return center.getValue(); }

    public void setCenter( HorizontalCoordinates newCenter )
    {
        center.setValue( newCenter );
    }
}
