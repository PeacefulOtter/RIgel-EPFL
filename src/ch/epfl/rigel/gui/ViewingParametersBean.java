package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.*;

/**
 * Represents the parameters determining the portion of the sky visible on the image.
 */
public class ViewingParametersBean
{
    // field of view in degrees
    private final DoubleProperty fieldOfViewDeg = new SimpleDoubleProperty( 0 );
    // center of the projection
    private final ObjectProperty<HorizontalCoordinates> center = new SimpleObjectProperty<>( null );

    /* FOV */
    public DoubleProperty fieldOfViewDegProperty() { return fieldOfViewDeg; }

    public Double getFieldOfViewDeg() { return fieldOfViewDeg.getValue(); }

    public void setFieldOfViewDeg( Double newFieldOfViewDeg )
    {
        fieldOfViewDeg.setValue( newFieldOfViewDeg );
    }

    /* Center Horizontal Coordinates */
    public ObjectProperty<HorizontalCoordinates> centerProperty() { return center; }

    public HorizontalCoordinates getCenter() { return center.getValue(); }

    public void setCenter( HorizontalCoordinates newCenter )
    {
        center.setValue( newCenter );
    }
}
