package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;


public class ViewingParametersBean
{
    private final IntegerProperty fieldOfViewDeg = new SimpleIntegerProperty( 0 );
    private final ObjectProperty<HorizontalCoordinates> center = new SimpleObjectProperty<>( null );


    public IntegerProperty fieldOfViewDegProperty() { return fieldOfViewDeg; }

    public Integer getFieldOfViewDeg() { return fieldOfViewDeg.getValue(); }

    public void setFieldOfViewDeg( Integer newFieldOfViewDeg )
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
