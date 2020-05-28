package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableStringValue;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.Transform;

import java.util.Optional;

public class SkyCanvasManager
{
    // Interval of the longitude in degrees
    private static final RightOpenInterval AZ_INTERVAL =  RightOpenInterval.of( 0, 360 );
    // Interval of the latitude in degrees
    private static final ClosedInterval ALT_INTERVAL = ClosedInterval.of( Angle.toDeg( -Math.PI / 2 ), Angle.toDeg( Math.PI / 2 ) );
    // FOV interval
    private static final ClosedInterval FOV_INTERVAL = ClosedInterval.of( 50, 200 );

    private final Canvas canvas;
    private final ObservableObjectValue<StereographicProjection> projectionBind;
    private final ObservableObjectValue<Transform> planeToCanvasBind;
    private final ObservableObjectValue<ObservedSky> observedSkyBind;
    private final ObservableObjectValue<HorizontalCoordinates> mouseHorizontalPosition;
    private final ObjectProperty<Point2D> mousePosition = new SimpleObjectProperty( null );

    public final ObservableDoubleValue mouseAzDeg, mouseAltDeg;
    public final ObservableStringValue objectUnderMouse;


    public SkyCanvasManager(
            StarCatalogue catalogue,
            DateTimeBean dateTimeBean,
            ObserverLocationBean observerLocationBean,
            ViewingParametersBean viewingParametersBean )
    {
        canvas = new Canvas( 800, 600 );
        SkyCanvasPainter painter = new SkyCanvasPainter( canvas );


        projectionBind = Bindings.createObjectBinding( () -> {
            return new StereographicProjection( viewingParametersBean.getCenter() );
        }, viewingParametersBean.centerProperty() );


        planeToCanvasBind = Bindings.createObjectBinding( () ->
        {
            double angle = viewingParametersBean.getFieldOfViewDeg().doubleValue() * 10;
            double width = canvas.getWidth() / 2;
            double height = canvas.getHeight() / 2;
            System.out.println("planeToCanvas Bind   " + width + " " + height + " " + angle );
            return Transform.affine( angle, 0, 0, -angle, width, height );
        },
                canvas.widthProperty(),
                canvas.heightProperty(),
                projectionBind,
                viewingParametersBean.fieldOfViewDegProperty()
        );

        observedSkyBind = Bindings.createObjectBinding( () ->

            new ObservedSky(
                    dateTimeBean.getZonedDateTime(),
                    observerLocationBean.getCoordinates(),
                    projectionBind.get(),
                    catalogue ),
            dateTimeBean.timeProperty(),
            dateTimeBean.dateProperty(),
            dateTimeBean.zoneProperty(),
            observerLocationBean.coordinatesProperty(),
            projectionBind
        );

        // ON KEY PRESSED, MOVE THE CENTER VIEW
        canvas.setOnKeyPressed( keyEvent ->
        {
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
                    viewingParametersBean.setCenter(
                            HorizontalCoordinates.ofDeg(
                                    center.azDeg(), ALT_INTERVAL.clip( center.altDeg() + 5 ) ) );
                    break;
                case RIGHT:
                    viewingParametersBean.setCenter(
                            HorizontalCoordinates.ofDeg(
                                    AZ_INTERVAL.reduce( center.azDeg() + 10 ),  center.altDeg() ) );
                    break;
                case DOWN:
                    viewingParametersBean.setCenter(
                            HorizontalCoordinates.ofDeg(
                                    center.azDeg(), ALT_INTERVAL.clip( center.altDeg() - 5 ) ) );
                    break;
                case LEFT:
                    viewingParametersBean.setCenter(
                            HorizontalCoordinates.ofDeg(
                                    AZ_INTERVAL.reduce( center.azDeg() - 10 ), center.altDeg() ) );
                    break;
                default:
                    break;
            }
        } );


        // SCROLL POSITION EVENT
        canvas.setOnScroll( scrollEvent ->
        {
            double deltaX = scrollEvent.getDeltaX();
            double deltaY = scrollEvent.getDeltaY();
            double maxScrollAxis = Math.abs( deltaX ) > Math.abs( deltaY ) ? deltaX : deltaY;
            System.out.println(maxScrollAxis);
            double actualFov = viewingParametersBean.getFieldOfViewDeg();
            viewingParametersBean.setFieldOfViewDeg( FOV_INTERVAL.clip(  actualFov + maxScrollAxis ) );
            System.out.println("fov =  " + viewingParametersBean.fieldOfViewDegProperty().get());
        } );


        // MOUSE POSITION EVENT
        canvas.setOnMouseMoved( mouseEvent ->
            mousePosition.setValue( new Point2D( mouseEvent.getX(), mouseEvent.getY() ) )
        );

        // MOUSE HORIZONTAL POSITION EVENT
        mouseHorizontalPosition = Bindings.createObjectBinding( () ->
        {
            if ( mousePosition.get() == null ) { return null; }
            // take the coordinates of the mouse and inverse planeToCanvas to have it on the plane
            Point2D mousePosTransform = planeToCanvasBind.get().inverseTransform( mousePosition.getValue() );
            return projectionBind.get().inverseApply(
                    CartesianCoordinates.of( mousePosTransform.getX(), mousePosTransform.getY() ) );
        }, planeToCanvasBind, projectionBind, mousePosition );


        // REQUEST FOCUS on canvas click
        canvas.setOnMouseClicked( mouseEvent -> canvas.requestFocus() );


        this.mouseAzDeg = Bindings.createDoubleBinding( () ->
                mouseHorizontalPosition.get().azDeg(), mouseHorizontalPosition );
        this.mouseAltDeg = Bindings.createDoubleBinding( () ->
                mouseHorizontalPosition.get().altDeg(), mouseHorizontalPosition );


        this.objectUnderMouse = Bindings.createStringBinding( () ->
        {
            if ( mousePosition.getValue() == null ) { return null; }
            Point2D mousePosInPlane = planeToCanvasBind.get().inverseTransform( mousePosition.getValue() );
            CartesianCoordinates mousePos = CartesianCoordinates.of( mousePosInPlane.getX(), mousePosInPlane.getY() );
            Optional hoverStar = observedSkyBind.get().objectClosestTo( mousePos, 0.003 );
            if ( hoverStar != Optional.empty() ) { return hoverStar.get().toString(); }
            return "";
        }, observedSkyBind, mousePosition, planeToCanvasBind );


        observedSkyBind.addListener(   ( o, oV, nV ) -> {
            painter.drawSky( nV, projectionBind.get(), planeToCanvasBind.get() );
        } );


        planeToCanvasBind.addListener( ( o, oV, nV ) -> {
            painter.drawSky( observedSkyBind.get(), projectionBind.get(), nV );
        } );
    }


    public double getMouseAzDeg()
    {
        return mouseAzDeg.get();
    }

    public double getMouseAltDeg()
    {
        return mouseAltDeg.get();
    }

    public ObservableObjectValue<HorizontalCoordinates> mouseHorizontalPositionProperty() { return mouseHorizontalPosition; }

    public ObservableObjectValue objectUnderMouseProperty()
    {
        return objectUnderMouse;
    }

    public String getObjectUnderMouse()
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
