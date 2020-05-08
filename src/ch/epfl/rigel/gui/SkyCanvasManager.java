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
import javafx.scene.input.KeyCode;
import javafx.scene.transform.Transform;

import java.util.Optional;

public class SkyCanvasManager
{
    private final Canvas canvas;
    private final ObservableObjectValue<StereographicProjection> projectionBind;
    private final ObservableObjectValue<Transform> planeToCanvasBind;
    private final ObservableObjectValue<ObservedSky> observedSkyBind;
    private final ObservableObjectValue<HorizontalCoordinates> mouseHorizontalPosition;
    private final ObjectProperty<Point2D> mousePosition = new SimpleObjectProperty( null );

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


        projectionBind = Bindings.createObjectBinding( () ->
            new StereographicProjection( viewingParametersBean.getCenter() ),
            viewingParametersBean.centerProperty()
        );


        planeToCanvasBind = Bindings.createObjectBinding( () ->
        {
            // double angle = projectionBind.get().applyToAngle( viewingParametersBean.getFieldOfViewDeg().doubleValue() );
            double width = canvas.getWidth() / 2;
            double height = canvas.getHeight() / 2;
            System.out.println("planeToCanvas");
            return Transform.affine( 1300, 0, 0, -1300, width, height );
        },
                canvas.widthProperty(),
                canvas.heightProperty(),
                projectionBind,
                viewingParametersBean.fieldOfViewDegProperty()
        );

        observedSkyBind = Bindings.createObjectBinding( () ->
        { System.out.println("observedSkyBind");
            return new ObservedSky(
                    dateTimeBean.getZonedDateTime(),
                    observerLocationBean.getCoordinates(),
                    projectionBind.get(),
                    catalogue ); },
            dateTimeBean.timeProperty(),
            dateTimeBean.dateProperty(),
            dateTimeBean.zoneProperty(),
            observerLocationBean.coordinatesProperty(),
            projectionBind
        );

        // ON KEY PRESSED, MOVE THE CENTER VIEW
        canvas.setOnKeyPressed( keyEvent ->
        {
            System.out.println("keyPressed");
            keyEvent.consume();
            KeyCode key = keyEvent.getCode();
            if (
                 key != KeyCode.UP && key != KeyCode.DOWN &&
                 key != KeyCode.RIGHT && key != KeyCode.LEFT
            )
            {
                return;
            }
            HorizontalCoordinates center = viewingParametersBean.getCenter();
            switch ( key )
            {
                case UP:
                    viewingParametersBean.setCenter( HorizontalCoordinates.of( center.az(), center.alt() + 1 ) );
                    break;
                case RIGHT:
                    viewingParametersBean.setCenter( HorizontalCoordinates.of( center.az() + 1,  center.alt() ) );
                    break;
                case DOWN:
                    viewingParametersBean.setCenter( HorizontalCoordinates.of( center.az(), center.alt() - 1 ) );
                    break;
                case LEFT:
                    viewingParametersBean.setCenter( HorizontalCoordinates.of( center.az() - 1, center.alt() ) );
                    break;
                default:
                    break;
            }
        } );


        // SCROLL POSITION EVENT
        canvas.setOnScroll( scrollEvent ->
        {
            System.out.println("on scroll");
            System.out.println( scrollEvent.getDeltaX() );
            System.out.println( scrollEvent.getDeltaY() );
            double deltaX = scrollEvent.getDeltaX();
            double deltaY = scrollEvent.getDeltaY();
            double maxScrollAxis = Math.round( deltaX ) > Math.round( deltaY ) ? deltaX : deltaY;
            int actualFov = viewingParametersBean.getFieldOfViewDeg();
            viewingParametersBean.setFieldOfViewDeg( actualFov + (int) maxScrollAxis );
        } );


        // MOUSE POSITION EVENT
        canvas.setOnMouseMoved( mouseEvent ->
        { System.out.println("mouse moved");
            mousePosition.setValue( new Point2D( mouseEvent.getX(), mouseEvent.getY() ) ); }
        );


        // MOUSE HORIZONTAL POSITION EVENT
        mouseHorizontalPosition = Bindings.createObjectBinding( () ->
        {
            // take the coordinates of the mouse and inverse planeToCanvas to have it on the plane
            if ( mousePosition.get() == null ) { return null; }
            System.out.println("mouseHorizontalPosition");
            Point2D mousePosTransform = planeToCanvasBind.get().inverseTransform( mousePosition.getValue() );
            return projectionBind.get().inverseApply(
                    CartesianCoordinates.of( mousePosTransform.getX(), mousePosTransform.getY() ) );
        }, planeToCanvasBind, projectionBind, mousePosition );


        this.mouseAzDeg = Bindings.createDoubleBinding( () ->
                mouseHorizontalPosition.get().altDeg(), mouseHorizontalPosition );
        this.mouseAltDeg = Bindings.createDoubleBinding( () ->
                mouseHorizontalPosition.get().altDeg(), mouseHorizontalPosition );


        this.objectUnderMouse = Bindings.createObjectBinding( () ->
        {
            if ( mousePosition.getValue() == null ) { return null; }
            Point2D mousePosInPlane = planeToCanvasBind.get().inverseTransform( mousePosition.getValue() );
            CartesianCoordinates mousePos = CartesianCoordinates.of( mousePosInPlane.getX(), mousePosInPlane.getY() );
            return observedSkyBind.get().objectClosestTo( mousePos, 0.05 );
        }, observedSkyBind, mousePosition, planeToCanvasBind );


        painter.drawSky(
                new ObservedSky(
                        dateTimeBean.getZonedDateTime(),
                        observerLocationBean.getCoordinates(),
                        new StereographicProjection( viewingParametersBean.getCenter() ),
                        catalogue
                ),
                new StereographicProjection( viewingParametersBean.getCenter() ),
                Transform.affine( 1300, 0, 0, -1300, 400, 300 ) );

        observedSkyBind.addListener(   ( o, oV, nV ) -> {
            System.out.println("observedSkyBind LISTENER");
            painter.drawSky( nV, projectionBind.get(), planeToCanvasBind.get() );
        } );
        projectionBind.addListener(    ( o, oV, nV ) -> {
            System.out.println("projectionBind LISTENER");
            painter.drawSky( observedSkyBind.get(), nV, planeToCanvasBind.get() );
        } );
        planeToCanvasBind.addListener( ( o, oV, nV ) -> {
            System.out.println("planeToCanvasBind LISTENER");
            painter.drawSky( observedSkyBind.get(), projectionBind.get(), nV );
        } );


        // BIND LE SKY AUX CHANGEMENT DE projection, planeToCanvas et ObservedSky
        // painter.drawSky( observedSky.get(), projection.get(), planeToCanvas.get() );

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

    public Optional<CelestialObject> getObjectUnderMouse()
    {
        return objectUnderMouse.get();
    }

    public HorizontalCoordinates getMouseHorizontalPosition()
    {
        return mouseHorizontalPosition.get();
    }

    public Canvas canvas()
    {
        return canvas;
    }
}
