package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
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

import java.util.ArrayList;
import java.util.List;
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
    // Arrow keys
    private static final List<KeyCode> ARROW_KEYS = new ArrayList<>() { {
        add( KeyCode.UP ); add( KeyCode.DOWN ); add( KeyCode.RIGHT ); add( KeyCode.LEFT );
    } };
    // maximum object distance of the ObjectClosestTo
    private static final double MAX_OBJECT_DISTANCE = Angle.ofDeg( 10 );
    // Angles to move when pressing an arrow key
    private static final int LONGITUDE_DISTANCE = 10;
    private static final int LATITUDE_DISTANCE = 5;
    // canvas size
    private static final int CANVAS_WIDTH = 1000;
    private static final int CANVAS_HEIGHT = 600;

    private final Canvas canvas;
    private final ObservableObjectValue<StereographicProjection> projectionBind;
    private final ObservableObjectValue<Transform> planeToCanvasBind;
    private final ObservableObjectValue<ObservedSky> observedSkyBind;
    private final ObservableObjectValue<HorizontalCoordinates> mouseHorizontalPosition;
    private final ObjectProperty<Point2D> mousePosition = new SimpleObjectProperty<>( new Point2D( 0, 0 ) );

    public final ObservableDoubleValue mouseAzDeg, mouseAltDeg;
    public final ObservableStringValue objectUnderMouse;

    /**
     * creates a number of properties and links
     * add a listener to be informed of mouse cursor movements, and store its position in a property
     * add a listener to detect mouse clicks on the canvas and make it the recipient of keyboard events
     * add a listener to react to mouse wheel and/or trackpad movements and change the field of view accordingly
     * sets up a listener to react to cursor key presses and change the projection center accordingly
     * add listeners to be informed of changes in the links and properties affecting the drawing of the sky, and in this case ask the painter to redraw it.
     *
     * @param catalogue : StarCatalogue containing all the stars and asterisms
     * @param dateTimeBean : the instant of observation (date, time, time zone)
     * @param observerLocationBean : position of the observer
     * @param viewingParametersBean : containing the parameters determining the portion of the sky visible on the image.
     */
    public SkyCanvasManager(
            StarCatalogue catalogue,
            DateTimeBean dateTimeBean,
            ObserverLocationBean observerLocationBean,
            ViewingParametersBean viewingParametersBean )
    {
        canvas = new Canvas( CANVAS_WIDTH, CANVAS_HEIGHT );
        SkyCanvasPainter painter = new SkyCanvasPainter( canvas );

        projectionBind = initProjectionBind( viewingParametersBean );

        planeToCanvasBind = initPlaneToCanvasBind( viewingParametersBean );

        observedSkyBind = initObservedSkyBind( dateTimeBean, observerLocationBean, catalogue );

        initKeyPressedEvent( viewingParametersBean );

        initScrollEvent( viewingParametersBean );

        initMouseMoveEvent();

        mouseHorizontalPosition = initMouseHorizontalPosBind();

        // request focus on canvas click
        canvas.setOnMouseClicked( mouseEvent -> canvas.requestFocus() );

        this.mouseAzDeg  = Bindings.createDoubleBinding( () -> mouseHorizontalPosition.get().azDeg(),  mouseHorizontalPosition );
        this.mouseAltDeg = Bindings.createDoubleBinding( () -> mouseHorizontalPosition.get().altDeg(), mouseHorizontalPosition );

        this.objectUnderMouse = initObjectUnderMouseBind();

        initEventListener( painter );
    }

    /**
     * Initiate a bind containing the stereographic projection.
     * Bound to the center of the ViewingParametersBean.
     * @param viewingParametersBean : The ViewingParametersBean required to get the center property
     * @return an ObservableObjectValue of the StereographicProjection
     */
    private ObservableObjectValue<StereographicProjection> initProjectionBind( ViewingParametersBean viewingParametersBean )
    {
        return Bindings.createObjectBinding( () ->
                        new StereographicProjection( viewingParametersBean.getCenter() ),
                viewingParametersBean.centerProperty() );
    }

    /**
     * Initiate a bind containing the transformation corresponding to the transition from the stereographic
     * projection plane to the canvas.
     * Bound to the canvas size, the stereographic projection and the FOV
     * @param viewingParametersBean : The ViewingParametersBean required to get the center property
     * @return an ObservableObjectValue of the Transformation
     */
    private ObservableObjectValue<Transform> initPlaneToCanvasBind( ViewingParametersBean viewingParametersBean )
    {
        return Bindings.createObjectBinding( () ->
                {
                    StereographicProjection projection = projectionBind.get();
                    double halfWidth = canvas.getWidth() / 2;
                    double halfHeight = canvas.getHeight() / 2;
                    double scale = 1;
                    // If the width is 0 (which it is the case when the program starts)
                    // the scale becomes also 0 and it throws an exception
                    // an if statement here is easier rather then a try/catch block
                    if ( halfWidth > 0 )
                    {
                        double radFOV = Angle.ofDeg( viewingParametersBean.getFieldOfViewDeg() );
                        scale = canvas.getWidth() / projection.applyToAngle( radFOV );
                    }
                    // create 'two' consecutive transformations, a translation and a scaling
                    return Transform.translate( halfWidth, halfHeight ).createConcatenation( Transform.scale( scale, -scale ) );
                },
                canvas.widthProperty(),
                canvas.heightProperty(),
                projectionBind,
                viewingParametersBean.fieldOfViewDegProperty()
        );
    }

    /**
     * Initiate a bind containing the actual ObservedSky.
     * Bound to the DateTimeBean properties, the observer coordinates and the stereographic projection
     * @param dateTimeBean : the DateTimeBean
     * @param observerLocationBean : the ObserverLocationBean
     * @param catalogue : the StarCatalogue containing the stars and asterisms
     * @return an ObservableObjectValue of the ObservedSky
     */
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

    /**
     * Initiate the key event : change the projection center when the user presses the cursor keys
     * @param viewingParametersBean : the ViewingParametersBean
     */
    private void initKeyPressedEvent( ViewingParametersBean viewingParametersBean )
    {
        canvas.setOnKeyPressed( keyEvent ->
        {
            KeyCode key = keyEvent.getCode(); // get the key
            // check if the key pressed is an arrow key, if it is not, then just exit the function
            if ( !ARROW_KEYS.contains( key ) ) { return; }

            keyEvent.consume(); // stops the event propagation.
            HorizontalCoordinates center = viewingParametersBean.getCenter();

            switch ( key )
            {
                case UP:
                    viewingParametersBean.setCenter(
                            HorizontalCoordinates.ofDeg(
                                    center.azDeg(), ALT_INTERVAL.clip( center.altDeg() + LATITUDE_DISTANCE ) ) );
                    break;
                case RIGHT:
                    viewingParametersBean.setCenter(
                            HorizontalCoordinates.ofDeg(
                                    AZ_INTERVAL.reduce( center.azDeg() + LONGITUDE_DISTANCE ),  center.altDeg() ) );
                    break;
                case DOWN:
                    viewingParametersBean.setCenter(
                            HorizontalCoordinates.ofDeg(
                                    center.azDeg(), ALT_INTERVAL.clip( center.altDeg() - LATITUDE_DISTANCE ) ) );
                    break;
                case LEFT:
                    viewingParametersBean.setCenter(
                            HorizontalCoordinates.ofDeg(
                                    AZ_INTERVAL.reduce( center.azDeg() - LONGITUDE_DISTANCE ), center.altDeg() ) );
                    break;
                default:
                    break;
            }
        } );
    }

    /**
     * Initiate the scroll event : change the field of view when the user manipulates the mouse wheel and/or the trackpad
     * @param viewingParametersBean : the ViewingParametersBean to get the FOV
     */
    private void initScrollEvent( ViewingParametersBean viewingParametersBean )
    {
        canvas.setOnScroll( scrollEvent ->
        {
            double deltaX = scrollEvent.getDeltaX();
            double deltaY = scrollEvent.getDeltaY();
            double maxScrollAxis = Math.abs( deltaX ) > Math.abs( deltaY ) ? deltaX : deltaY;
            double actualFov = viewingParametersBean.getFieldOfViewDeg();
            viewingParametersBean.setFieldOfViewDeg( FOV_INTERVAL.clip(  actualFov + maxScrollAxis ) );
        } );
    }

    /**
     * Initiate the mouse move event : get the position of the mouse in the canvas
     */
    private void initMouseMoveEvent()
    {
        canvas.setOnMouseMoved( mouseEvent ->
                mousePosition.setValue( new Point2D( mouseEvent.getX(), mouseEvent.getY() ) )
        );
    }

    /**
     * Initiate a bind containing the position of the mouse cursor in the horizontal coordinate system (az/alt)
     * @return an ObservableObjectValue of the mouse horizontal coordinates
     */
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


    /**
     * Initiate the bind containing the celestial object closest to the cursor.
     * it follows the mouse movements and export, via properties, the position of its cursor in the horizontal coordinate system,
     * and the celestial object closest to this cursor.
     * Bound to the ObservedSky, the mouse position and the planeToCanvas transformation
     * @return an ObservableStringValue which is the name of the object closest to the mouse
     */
    private ObservableStringValue initObjectUnderMouseBind()
    {
        return Bindings.createStringBinding( () ->
        {
            if ( mousePosition.getValue() == null ) { return null; }
            Point2D mousePosInPlane = planeToCanvasBind.get().inverseTransform( mousePosition.getValue() );
            CartesianCoordinates mousePos = CartesianCoordinates.of( mousePosInPlane.getX(), mousePosInPlane.getY() );
            double maxDist = projectionBind.get().applyToAngle( MAX_OBJECT_DISTANCE );
            Optional<CelestialObject> closestCelestialObject = observedSkyBind.get().objectClosestTo( mousePos, maxDist );
            // if there is an object close the mouse, return the celestial objects name
            if ( closestCelestialObject.isPresent() ) { return closestCelestialObject.get().toString(); }
            // else, return an empty string
            return "";
        }, observedSkyBind, mousePosition, planeToCanvasBind );
    }

    /**
     * Add a listener to the Observable object observedSky and planeToCanvas to redraw the canvas
     * when they change
     */
    private void initEventListener( SkyCanvasPainter painter )
    {
        observedSkyBind.addListener( ( o, oV, nV ) -> {
            painter.drawSky( nV, projectionBind.get(), planeToCanvasBind.get() );
        } );

        planeToCanvasBind.addListener( ( o, oV, nV ) -> {
            painter.drawSky( observedSkyBind.get(), projectionBind.get(), nV );
        } );
    }


    /* Getters */
    public double getMouseAzDeg() { return mouseAzDeg.get(); }

    public double getMouseAltDeg() { return mouseAltDeg.get(); }

    public ObservableObjectValue<HorizontalCoordinates> mouseHorizontalPositionProperty() { return mouseHorizontalPosition; }

    public ObservableStringValue objectUnderMouseProperty() { return objectUnderMouse; }

    public String getObjectUnderMouse() { return objectUnderMouse.get(); }

    public HorizontalCoordinates getMouseHorizontalPosition() { return mouseHorizontalPosition.get(); }

    public Canvas canvas() { return canvas; }
}
