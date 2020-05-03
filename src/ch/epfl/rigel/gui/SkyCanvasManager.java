package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

public class SkyCanvasManager
{
    Canvas canvas = new Canvas( 800, 600 );
    public DoubleProperty mouseAzDeg = new SimpleDoubleProperty( 0 );
    public DoubleProperty mouseAltDeg = new SimpleDoubleProperty( 0 );
    public ObservableObjectValue<CelestialObject> objectUnderMouse = new SimpleObjectProperty<>( null );

    public SkyCanvasManager(
            StarCatalogue catalogue,
            DateTimeBean dateTimeBean,
            ObserverLocationBean observerLocationBean,
            ViewingParametersBean viewingParametersBean )
    {
        SkyCanvasPainter painter = new SkyCanvasPainter( canvas );

        ObservableObjectValue<StereographicProjection> projection = Bindings.createObjectBinding( () ->
                new StereographicProjection( viewingParametersBean.getCenter() ),
                viewingParametersBean.centerProperty() );


        ObservableObjectValue<javafx.scene.transform.Affine> planeToCanvas = Bindings.createObjectBinding( () ->
                Transform.affine(
                        projection.get().applyToAngle( viewingParametersBean.getFieldOfViewDeg().doubleValue() ),
                        0, 0,
                        -projection.get().applyToAngle( viewingParametersBean.getFieldOfViewDeg().doubleValue() ),
                        canvas.getWidth() / 2, canvas.getHeight() / 2 ),
                canvas.widthProperty(), canvas.heightProperty(), projection, viewingParametersBean.fieldOfViewDegProperty() );

        ObservableObjectValue<ObservedSky> observedSky = Bindings.createObjectBinding( () ->
                new ObservedSky( dateTimeBean.getZonedDateTime(), observerLocationBean.getCoordinates(), projection.get(), catalogue ),
                dateTimeBean.timeProperty(), dateTimeBean.dateProperty(), dateTimeBean.zoneProperty(), projection, observerLocationBean.coordinatesProperty() );

        // MOUSE POSITION EVENT
        ObjectProperty<Point2D> mousePosition = new SimpleObjectProperty( null );
        canvas.setOnMouseMoved( mouseEvent -> {
            mousePosition.setValue( new Point2D( mouseEvent.getX(), mouseEvent.getY() ) );
        } );

        // SCROLL POSITION EVENT
        canvas.setOnScroll( scrollEvent -> {
            System.out.println( scrollEvent.getDeltaX() );
            System.out.println( scrollEvent.getDeltaY() );
            double deltaX = scrollEvent.getDeltaX();
            double deltaY = scrollEvent.getDeltaY();
            double maxScrollAxis = Math.round( deltaX ) > Math.round( deltaY )
                    ? deltaX : deltaY;
            int actualFov = viewingParametersBean.getFieldOfViewDeg();
            viewingParametersBean.setFieldOfViewDeg( actualFov + (int) maxScrollAxis );
        } );

        // ObservableObjectValue mouseHorizontalPosition = Bindings.createObjectBinding( () -> , planeToCanvas, projection, mousePosition );
    }

    public DoubleProperty mouseAzDegProperty() { return mouseAzDeg; }

    public double getMouseAzDeg() { return mouseAzDeg.get(); }

    public void setMouseAzDeg( double mouseAzDeg ) { this.mouseAzDeg.set( mouseAzDeg ); }


    public DoubleProperty mouseAltDegProperty() { return mouseAltDeg; }

    public double getMouseAltDeg() { return mouseAltDeg.get(); }

    public void setMouseAltDeg( double mouseAltDeg ) { this.mouseAltDeg.set( mouseAltDeg ); }


    public ObservableObjectValue objectUnderMouseProperty() { return objectUnderMouse; }

    public CelestialObject getObjectUnderMouse() { return objectUnderMouse.get(); }



    public Canvas canvas() { return canvas; }
}
