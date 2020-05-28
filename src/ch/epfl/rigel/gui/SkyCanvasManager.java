package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.Star;
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


/**
 * This class is used to bind important variables (e.g. the plane to canvas transformation,
 * the stereographic projection, etc..) to some parameters that can change during runtime
 */
public class SkyCanvasManager
{
    // Interval of the longitude in degrees
    private static final RightOpenInterval AZ_INTERVAL =  RightOpenInterval.of( 0, 360 );
    // Interval of the latitude in degrees
    private static final ClosedInterval ALT_INTERVAL = ClosedInterval.of( Angle.toDeg( -Math.PI / 2 ), Angle.toDeg( Math.PI / 2 ) );
    // FOV interval
    private static final ClosedInterval FOV_INTERVAL = ClosedInterval.of( 50, 200 );

    private static final int MAX_OBJECT_DISTANCE = 10;

    private final Canvas canvas;
    private final ObservableObjectValue<StereographicProjection> projectionBind;
    private final ObservableObjectValue<Transform> planeToCanvasBind;
    private final ObservableObjectValue<ObservedSky> observedSkyBind;
    private final ObservableObjectValue<HorizontalCoordinates> mouseHorizontalPosition;
    private final ObjectProperty<Point2D> mousePosition = new SimpleObjectProperty( new Point2D( 0, 0 ) );

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

        projectionBind = initProjectionBind( viewingParametersBean );

        planeToCanvasBind = initPlaneToCanvasBind( viewingParametersBean );

        observedSkyBind = initObservedSkyBind( dateTimeBean, observerLocationBean, catalogue );

        initKeyPressedEvent( viewingParametersBean );

        initScrollEvent( viewingParametersBean );

        initMouseMoveEvent( );

        mouseHorizontalPosition = initMouseHorizontalPosBind();


        // REQUEST FOCUS on canvas click
        canvas.setOnMouseClicked( mouseEvent -> canvas.requestFocus() );


        this.mouseAzDeg  = Bindings.createDoubleBinding( () -> mouseHorizontalPosition.get().azDeg(),  mouseHorizontalPosition );
        this.mouseAltDeg = Bindings.createDoubleBinding( () -> mouseHorizontalPosition.get().altDeg(), mouseHorizontalPosition );


        this.objectUnderMouse = initObjectUnderMouseBind();

        initEventListener( painter );
    }


    private ObservableObjectValue<StereographicProjection> initProjectionBind( ViewingParametersBean viewingParametersBean )
    {
        return Bindings.createObjectBinding( () ->
                        new StereographicProjection( viewingParametersBean.getCenter() ),
                viewingParametersBean.centerProperty() );
    }


    private ObservableObjectValue<Transform> initPlaneToCanvasBind( ViewingParametersBean viewingParametersBean )
    {
        return Bindings.createObjectBinding( () ->
                {
                    StereographicProjection projection = projectionBind.get();
                    double width = canvas.getWidth() / 2;
                    double height = canvas.getHeight() / 2;
                    System.out.println(width);
                    double scale = 1;
                    if ( width != 0d )
                    {
                        scale = projection.applyToAngle( Angle.ofDeg( viewingParametersBean.getFieldOfViewDeg().doubleValue() ) ) * viewingParametersBean.getFieldOfViewDeg();
                    }
                    // System.out.println(scale * viewingParametersBean.getFieldOfViewDeg());
                    return Transform.translate( width, height ).createConcatenation( Transform.scale( scale, -scale ) );
                },
                canvas.widthProperty(),
                canvas.heightProperty(),
                projectionBind,
                viewingParametersBean.fieldOfViewDegProperty()
        );
    }

    private ObservableObjectValue<ObservedSky> initObservedSkyBind(
            DateTimeBean dateTimeBean, ObserverLocationBean observerLocationBean, StarCatalogue catalogue )
    {
        return Bindings.createObjectBinding( () ->
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
    }

    private void initKeyPressedEvent( ViewingParametersBean viewingParametersBean )
    {
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
    }

    private void initScrollEvent( ViewingParametersBean viewingParametersBean )
    {
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
    }

    private void initMouseMoveEvent()
    {
        canvas.setOnMouseMoved( mouseEvent ->
                mousePosition.setValue( new Point2D( mouseEvent.getX(), mouseEvent.getY() ) )
        );
    }

    private ObservableObjectValue<HorizontalCoordinates> initMouseHorizontalPosBind()
    {
        return Bindings.createObjectBinding( () ->
        {
            if ( mousePosition.get() == null ) { return null; }
            // take the coordinates of the mouse and inverse planeToCanvas to have it on the plane
            Point2D mousePosTransform = planeToCanvasBind.get().inverseTransform( mousePosition.getValue() );
            return projectionBind.get().inverseApply(
                    CartesianCoordinates.of( mousePosTransform.getX(), mousePosTransform.getY() ) );
        }, planeToCanvasBind, projectionBind, mousePosition );
    }

    private ObservableStringValue initObjectUnderMouseBind()
    {
        return Bindings.createStringBinding( () ->
        {
            if ( mousePosition.getValue() == null ) { return null; }
            Point2D mousePosInPlane = planeToCanvasBind.get().inverseTransform( mousePosition.getValue() );
            CartesianCoordinates mousePos = CartesianCoordinates.of( mousePosInPlane.getX(), mousePosInPlane.getY() );
            double maxDist = projectionBind.get().applyToAngle( Angle.ofDeg( MAX_OBJECT_DISTANCE ) );
            Optional hoverStar = observedSkyBind.get().objectClosestTo( mousePos, maxDist );
            if ( hoverStar != Optional.empty() ) { return hoverStar.get().toString(); }
            return "";
        }, observedSkyBind, mousePosition, planeToCanvasBind );
    }

    private void initEventListener( SkyCanvasPainter painter )
    {
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
