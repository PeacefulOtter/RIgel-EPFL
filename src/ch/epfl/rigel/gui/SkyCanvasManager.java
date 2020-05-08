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
    private final Canvas canvas;

    private final ObservableObjectValue<StereographicProjection> projection;
    private final ObservableObjectValue<Transform> planeToCanvas;
    private final ObservableObjectValue<ObservedSky> observedSky;
    private final ObjectProperty<Point2D> mousePosition;
    private final ObservableObjectValue<HorizontalCoordinates> mouseHorizontalPosition;

    public final ObservableDoubleValue mouseAzDeg, mouseAltDeg;
    public final ObservableObjectValue<Optional<CelestialObject>> objectUnderMouse;


    public SkyCanvasManager(
            StarCatalogue catalogue,
            DateTimeBean dateTimeBean,
            ObserverLocationBean observerLocationBean,
            ViewingParametersBean viewingParametersBean )
    {
        canvas = new Canvas( 800, 600 );
        SkyCanvasPainter painter = new SkyCanvasPainter( canvas );


        projection = Bindings.createObjectBinding( () ->
            new StereographicProjection( viewingParametersBean.getCenter() ),
            viewingParametersBean.centerProperty() );


        planeToCanvas = Bindings.createObjectBinding( () ->
        {
            double angle = projection.get().applyToAngle( viewingParametersBean.getFieldOfViewDeg().doubleValue() );
            double width = canvas.getWidth() / 2;
            double height = canvas.getHeight() / 2;
            return Transform.affine( angle, 0, 0, -angle, width, height );
        }, canvas.widthProperty(), canvas.heightProperty(), projection, viewingParametersBean.fieldOfViewDegProperty() );

        observedSky = Bindings.createObjectBinding( () ->
                        new ObservedSky( dateTimeBean.getZonedDateTime(), observerLocationBean.getCoordinates(), projection.get(), catalogue ),
                dateTimeBean.timeProperty(),
                dateTimeBean.dateProperty(),
                dateTimeBean.zoneProperty(),
                projection,
                observerLocationBean.coordinatesProperty() );


        // SCROLL POSITION EVENT
        canvas.setOnScroll( scrollEvent ->
        {
            System.out.println( scrollEvent.getDeltaX() );
            System.out.println( scrollEvent.getDeltaY() );
            double deltaX = scrollEvent.getDeltaX();
            double deltaY = scrollEvent.getDeltaY();
            double maxScrollAxis = Math.round( deltaX ) > Math.round( deltaY )
                    ? deltaX : deltaY;
            int actualFov = viewingParametersBean.getFieldOfViewDeg();
            viewingParametersBean.setFieldOfViewDeg( actualFov + (int) maxScrollAxis );
        } );


        // MOUSE POSITION EVENT
        mousePosition = new SimpleObjectProperty( null );
        canvas.setOnMouseMoved( mouseEvent ->
        {
            mousePosition.setValue( new Point2D( mouseEvent.getX(), mouseEvent.getY() ) );
        } );


        // MOUSE HORIZONTAL POSITION EVENT
        mouseHorizontalPosition = Bindings.createObjectBinding( () ->
        {
            // take the coordinates of the mouse and inverse planeToCanvas to have it on the plane
            Point2D mousePosTransform = planeToCanvas.get().inverseTransform( mousePosition.getValue() );
            HorizontalCoordinates mouseHorizontalPos = projection.get().inverseApply(
                    CartesianCoordinates.of( mousePosTransform.getX(), mousePosTransform.getY() ) );
            return mouseHorizontalPos;

        }, planeToCanvas, projection, mousePosition );


        this.mouseAzDeg = Bindings.createDoubleBinding( () ->
                mouseHorizontalPosition.get().altDeg(), mouseHorizontalPosition );
        this.mouseAltDeg = Bindings.createDoubleBinding( () ->
                mouseHorizontalPosition.get().altDeg(), mouseHorizontalPosition );


        this.objectUnderMouse = Bindings.createObjectBinding( () ->
        {
            Point2D mousePosInPlane = planeToCanvas.get().inverseTransform( mousePosition.getValue() );
            CartesianCoordinates mousePos = CartesianCoordinates.of( mousePosInPlane.getX(), mousePosInPlane.getY() );
            return observedSky.get().objectClosestTo( mousePos, 0.5 );
        }, observedSky, mousePosition, planeToCanvas );

        


        // BIND LE SKY AUX CHANGEMENT DE projection, planeToCanvas et ObservedSky
        painter.drawSky( observedSky.get(), projection.get(), planeToCanvas.get() );
    }


    public double getMouseAzDeg()
    {
        return mouseAzDeg.get();
    }

    public double getMouseAltDeg()
    {
        return mouseAltDeg.get();
    }

    public ObservableObjectValue objectUnderMouseProperty()
    {
        return objectUnderMouse;
    }

    public Canvas canvas()
    {
        return canvas;
    }
}
