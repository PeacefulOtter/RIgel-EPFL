package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.StereographicProjection;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.transform.Transform;

public class SkyCanvasManager
{
    public DoubleProperty mouseAzDeg, mouseAltDeg;
    public ObservableObjectValue<CelestialObject> objectUnderMouse ;

    public SkyCanvasManager(
            StarCatalogue catalogue,
            DateTimeBean dateTimeBean,
            ObserverLocationBean observerLocationBean,
            ViewingParametersBean viewingParametersBean )
    {
        Canvas canvas = new Canvas( 800, 600 );
        SkyCanvasPainter painter = new SkyCanvasPainter( canvas );
        ObservableObjectValue<StereographicProjection> projection = Bindings.createObjectBinding(() ->
                new StereographicProjection(viewingParametersBean.getCenter()), viewingParametersBean.centerProperty());

        ObservableObjectValue planeToCanvas = Bindings.createObjectBinding(() ->
                Transform.affine( projection.get().applyToAngle( viewingParametersBean.getFieldOfViewDeg().doubleValue() ), 0, 0,
                        -  projection.get().applyToAngle( viewingParametersBean.getFieldOfViewDeg().doubleValue() ), canvas.getWidth()/2, canvas.getHeight()/2 ),
                canvas.widthProperty(), canvas.heightProperty(), projection, viewingParametersBean.fieldOfViewDegProperty());

        ObservableObjectValue<ObservedSky> observedSky = Bindings.createObjectBinding(()->
                new ObservedSky(dateTimeBean.getZonedDateTime(), observerLocationBean.getCoordinates(), projection.get(), catalogue),
                dateTimeBean.timeProperty(), dateTimeBean.dateProperty(), dateTimeBean.zoneProperty(), projection, observerLocationBean.coordinatesProperty());

        ObjectProperty mousePosition = new SimpleObjectProperty(null);
        canvas.setOnMouseMoved(mouseEvent -> mousePosition.setValue(new Point2D(mouseEvent.getX(), mouseEvent.getY())));

//        ObservableObjectValue mouseHorizontalPosition = Bindings.createObjectBinding(() -> , planeToCanvas, projection, mousePosition);

    }

    public ObjectProperty objectUnderMouseProperty()
    {
        return null;
    }

    public Canvas canvas()
    {
        return null;
    }
}
