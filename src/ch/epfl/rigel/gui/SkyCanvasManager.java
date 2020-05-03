package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.transform.Transform;

import java.util.Optional;

public class SkyCanvasManager
{
    public ObservableDoubleValue mouseAzDeg, mouseAltDeg;
    public ObservableObjectValue<Optional<CelestialObject>> objectUnderMouse ;

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

        ObservableObjectValue<Transform> planeToCanvas = Bindings.createObjectBinding(() -> {
            double angleTodouble = projection.get().applyToAngle(viewingParametersBean.getFieldOfViewDeg().doubleValue());
            double width = canvas.getWidth()/2;
            double height = canvas.getHeight()/2;
            return Transform.affine(angleTodouble, 0, 0, - angleTodouble, width, height);
            } , canvas.widthProperty(), canvas.heightProperty(), projection, viewingParametersBean.fieldOfViewDegProperty());

        ObservableObjectValue<ObservedSky> observedSky = Bindings.createObjectBinding(()->
                new ObservedSky(dateTimeBean.getZonedDateTime(), observerLocationBean.getCoordinates(), projection.get(), catalogue),
                dateTimeBean.timeProperty(), dateTimeBean.dateProperty(), dateTimeBean.zoneProperty(), projection, observerLocationBean.coordinatesProperty());

        ObjectProperty<Point2D> mousePosition = new SimpleObjectProperty(null);
        canvas.setOnMouseMoved(mouseEvent -> mousePosition.setValue(new Point2D(mouseEvent.getX(), mouseEvent.getY())));

        ObservableObjectValue<HorizontalCoordinates> mouseHorizontalPosition = Bindings.createObjectBinding(() -> {
//            take the coordinates of the mouse and inverse planeToCanvas to have it on the plane
            Point2D mousePosTransform = planeToCanvas.get().inverseTransform(mousePosition.getValue());
            HorizontalCoordinates mouseHorizontalPos = projection.get().inverseApply(CartesianCoordinates.of(mousePosTransform.getX(), mousePosTransform.getY()));
            return mouseHorizontalPos;

        } , planeToCanvas, projection, mousePosition);

        this.mouseAzDeg = Bindings.createDoubleBinding(() -> mouseHorizontalPosition.get().altDeg(), mouseHorizontalPosition);
        this.mouseAltDeg = Bindings.createDoubleBinding(() -> mouseHorizontalPosition.get().altDeg(), mouseHorizontalPosition);

        this.objectUnderMouse = Bindings.createObjectBinding(() -> {
            Point2D mousePosInPlane = planeToCanvas.get().inverseTransform(mousePosition.getValue());
            CartesianCoordinates mousePos = CartesianCoordinates.of(mousePosInPlane.getX(), mousePosInPlane.getY());
            return observedSky.get().objectClosestTo(mousePos, 0.5);
        }, observedSky, mousePosition, planeToCanvas);
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
