package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.Asterism;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.coordinates.*;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;

import java.util.*;

/**
 * Represents a sky painter : each method draws a part of the sky
 */
public class SkyCanvasPainter
{
    // Maximum horizon radius
    private static final double MAX_HORIZON_RADIUS = 1E10;

    private static final Color BLUE_COLOR = Color.BLUE;
    private static final Color LIGHTGRAY_COLOR = Color.LIGHTGRAY;
    private static final Color WHITE_COLOR = Color.WHITE;
    private static final Color RED_COLOR = Color.RED;
    private static final Color YELLOW_COLOR = Color.YELLOW;
    private static final Color YELLOW_COLOR_HALO = YELLOW_COLOR.deriveColor( 0, 1, 1, 0.25 );
    // Magnitude interval
    private static final ClosedInterval MAGNITUDE_INTERVAL = ClosedInterval.of( -2, 5 );
    private static final HorizontalCoordinates HORIZON_COORDINATES = HorizontalCoordinates.ofDeg( 0, 0 );
    private static final double HALF_DEG_RAD = Angle.ofDeg( 0.5 );
    private static final double OCTANT_ALT_DEG = -0.5;
    private static final int ASTERISM_WIDTH = 1;
    private static final int HORIZON_WIDTH = 2;

    private final Canvas canvas;
    private final GraphicsContext ctx;
    private final BlackBodyColor blackBodyColor;
    private final Map<String, HorizontalCoordinates> octants;

    public SkyCanvasPainter( Canvas canvas )
    {
        this.canvas = canvas;
        ctx = canvas.getGraphicsContext2D();
        // creates an instance of BlackBodyColor to get all the colors once
        blackBodyColor = new BlackBodyColor();

        // register each octant ( its name and horizontal coordinates )
        octants = new HashMap<>();
        for ( int i = 0; i < 360; i += 45 )
        {
            HorizontalCoordinates octantCoordinates = HorizontalCoordinates.ofDeg( i, OCTANT_ALT_DEG );
            octants.put( octantCoordinates.azOctantName( "N", "E", "S", "O" ), octantCoordinates );
        }
    }

    /**
     * Calculates the diameter based on the star's / planet's magnitude
     * @param magnitude : the star magnitude
     * @param projection : the stereographic projection
     * @return the size of the disc representing a celestial object as a function of its magnitude
     */
    private static double magnitudeDiameter( double magnitude, StereographicProjection projection )
    {
        double clippedMagnitude = MAGNITUDE_INTERVAL.clip( magnitude );
        double sizeFactor = ( 99 - 17 * clippedMagnitude ) / 140;
        return sizeFactor * projection.applyToAngle( HALF_DEG_RAD );
    }

    /**
     * Draws all the parts of the sky
     * @param sky : the sky we are drawing into
     * @param projection : the stereographic projection
     * @param planeToCanvas : the transformation from the plane to the canvas coordinates
     */
    public void drawSky( ObservedSky sky, StereographicProjection projection, Transform planeToCanvas )
    {
        this.clear();
        this.drawStars( sky, projection, planeToCanvas );
        this.drawPlanets( sky, projection, planeToCanvas );
        this.drawSun( sky, projection, planeToCanvas );
        this.drawMoon( sky, projection, planeToCanvas );
        this.drawHorizon( projection, planeToCanvas );
    }

    /**
     * Clears the canvas
     */
    public void clear()
    {
        ctx.clearRect( 0, 0, canvas.getWidth(), canvas.getHeight() );
        ctx.setFill( Color.BLACK );
        ctx.fillRect( 0, 0, canvas.getWidth(), canvas.getHeight() );
        ctx.fill();
    }

    /**
     * Draws the stars and asterisms
     * @param sky : the sky we are drawing into
     * @param projection : the stereographic projection
     * @param planeToCanvas : the transformation from the plane to the canvas coordinates
     */
    public void drawStars( ObservedSky sky, StereographicProjection projection, Transform planeToCanvas )
    {
        // get the asterisms and the stars
        Set<Asterism> asterisms = sky.getAsterism();
        List<Star> stars = sky.stars();
        List<CartesianCoordinates> cartesianCoordinates = sky.starPosition();
        double[] starsCartesianCoordinates = sky.starsArrayPosition();
        int starsNumber = stars.size();

        // transform all the stars coordinates into the canvas coordinate system
        double[] dstPts = new double[ starsNumber * 2 ];
        planeToCanvas.transform2DPoints( starsCartesianCoordinates, 0, dstPts, 0, starsNumber );

        int starCoordsIndex = 0;
        for ( Asterism asterism: asterisms )
        {
            List<Integer> asterismIndices = sky.asterismIndices( asterism );
            boolean firstStar = true;
            boolean lastInsideCanvas = true;
            boolean currentInsideCanvas;

            ctx.beginPath();
            for ( Integer indice : asterismIndices )
            {
                CartesianCoordinates cartesianCoords = cartesianCoordinates.get( indice );
                Point2D starPoint = planeToCanvas.transform( cartesianCoords.x(), cartesianCoords.y() );

                // true if the star is inside the canvas, false otherwise
                currentInsideCanvas = canvas.getBoundsInLocal().contains( starPoint );

                // we don't draw any line when we are at the first star of the asterism
                if ( firstStar )
                {
                    ctx.moveTo( starPoint.getX(), starPoint.getY() );
                    firstStar = false;
                    lastInsideCanvas = currentInsideCanvas;
                    continue;
                }

                // avoid drawing the asterism branches outside the canvas
                if ( !currentInsideCanvas && !lastInsideCanvas )
                {
                    ctx.moveTo( starPoint.getX(), starPoint.getY() );
                    continue;
                }

                ctx.setLineWidth( ASTERISM_WIDTH );
                ctx.setStroke( BLUE_COLOR );
                ctx.lineTo( starPoint.getX(), starPoint.getY() );
                ctx.stroke();

                lastInsideCanvas = currentInsideCanvas;
            }
            ctx.closePath();
        }

        // then we draw the stars
        for ( Star star : stars )
        {
            // round to the nearest 500
            int roundedColor = ( ( ( star.colorTemperature() + 499 ) / 500 ) * 500 );
            // get the corresponding color thanks to the BlackBodyColor class
            Color starColor = blackBodyColor.colorForTemperature( roundedColor );
            ctx.setFill( starColor );
            // get the diameter based on the star's magnitude
            double starDiameter = magnitudeDiameter( star.magnitude(), projection );
            // transform the diameter into the canvas coordinate system
            double finalDiameter = planeToCanvas.deltaTransform( starDiameter, 0 ).getX();
            double radius = finalDiameter / 2;
            double starX = dstPts[ starCoordsIndex++ ] - radius;
            double starY = dstPts[ starCoordsIndex++ ] - radius;
            // draw the star as a disk
            ctx.fillOval( starX, starY, finalDiameter, finalDiameter );
        }
    }

    /**
     * Draws the planets
     * @param sky : the sky we are drawing into
     * @param projection : the stereographic projection
     * @param planeToCanvas : the transformation from the plane to the canvas coordinates
     */
    public void drawPlanets( ObservedSky sky, StereographicProjection projection, Transform planeToCanvas )
    {
        // get all the planets
        List<Planet> planets = sky.planets();
        double[] planetCartesianCoordinates = sky.planetPosition();
        int index = 0;

        for ( Planet planet : planets )
        {
            // transform the planet coordinates into the canvas coordinate system
            Point2D planetPoint = planeToCanvas.transform(
                    planetCartesianCoordinates[ index ], planetCartesianCoordinates[ ++index ] );
            // get the diameter based on its magnitude
            double planetDiameter = magnitudeDiameter( planet.magnitude(), projection );
            // transform the diameter into the canvas coordinate system
            double finalDiameter = planeToCanvas.deltaTransform( planetDiameter, 0 ).getX();
            double radius = finalDiameter / 2;

            ctx.setFill( LIGHTGRAY_COLOR );
            ctx.fillOval( planetPoint.getX() - radius, planetPoint.getY() - radius, finalDiameter, finalDiameter );

            index++;
        }
    }

    /**
     * Draws the sun
     * @param sky : the sky we are drawing into
     * @param projection : the stereographic projection
     * @param planeToCanvas : the transformation from the plane to the canvas coordinates
     */
    public void drawSun( ObservedSky sky, StereographicProjection projection, Transform planeToCanvas )
    {
        // get the sun position
        CartesianCoordinates sunPos = sky.sunPosition();
        // transform the sun coordinates into the canvas coordinate system
        Point2D sunPoint = planeToCanvas.transform( sunPos.x(), sunPos.y() );

        double sunDiameter = projection.applyToAngle( HALF_DEG_RAD );
        double finalDiameter = planeToCanvas.deltaTransform( sunDiameter, 0 ).getX();

        // draw three layers of disk to simulate a bright sun
        ctx.setFill( YELLOW_COLOR_HALO );
        double haloRadius = ( finalDiameter * 2.2 ) / 2;
        ctx.fillOval( sunPoint.getX() - haloRadius, sunPoint.getY() - haloRadius,
                finalDiameter * 2.2, finalDiameter * 2.2 );

        ctx.setFill( YELLOW_COLOR );
        double yellowRadius = ( finalDiameter + 2 ) / 2;
        ctx.fillOval( sunPoint.getX() - yellowRadius, sunPoint.getY() - yellowRadius,
                finalDiameter + 2, finalDiameter + 2 );

        ctx.setFill( WHITE_COLOR );
        double whiteRadius = finalDiameter / 2;
        ctx.fillOval( sunPoint.getX() - whiteRadius, sunPoint.getY() - whiteRadius,
                finalDiameter, finalDiameter );
    }

    /**
     * Draws the moon
     * @param sky : the sky we are drawing into
     * @param projection : the stereographic projection
     * @param planeToCanvas : the transformation from the plane to the canvas coordinates
     */
    public void drawMoon( ObservedSky sky, StereographicProjection projection, Transform planeToCanvas )
    {
        // get the moon coordinates
        CartesianCoordinates moonPos = sky.moonPosition();
        // transform the moon coordinates into the canvas coordinate system
        Point2D moonPoint = planeToCanvas.transform( moonPos.x(), moonPos.y() );
        // get the diameter based on its magnitude
        double projectedDiameter = projection.applyToAngle( HALF_DEG_RAD );
        // transform the diameter into the canvas coordinate system
        double finalDiameter = planeToCanvas.deltaTransform( projectedDiameter, 0 ).getX();
        double radius = finalDiameter / 2;

        ctx.setFill( WHITE_COLOR );
        ctx.fillOval( moonPoint.getX() - radius, moonPoint.getY() - radius, finalDiameter, finalDiameter );
    }

    /**
     * Draws the Horizon and the
     * @param projection : the stereographic projection
     * @param planeToCanvas : the transformation from the plane to the canvas coordinates
     */
    public void drawHorizon( StereographicProjection projection, Transform planeToCanvas )
    {
        // get the center position using the projection
        CartesianCoordinates center = projection.circleCenterForParallel( HORIZON_COORDINATES );
        // transform it into the canvas coordinate system
        Point2D transformedCenter = planeToCanvas.transform( center.x(), center.y() );
        // get the radius
        double radius = projection.circleRadiusForParallel( HORIZON_COORDINATES );
        // transform it into the canvas coordinate system and prevent it from being negative
        double transformedRadius = Math.abs( planeToCanvas.deltaTransform( radius, 0 ).getX() );

        ctx.setStroke( RED_COLOR );
        ctx.setLineWidth( HORIZON_WIDTH );

        // avoids infinite radius
        if ( transformedRadius < MAX_HORIZON_RADIUS )
        {
            ctx.strokeOval(
                    transformedCenter.getX() - transformedRadius,
                    transformedCenter.getY() - transformedRadius,
                    transformedRadius * 2, transformedRadius * 2 );
        }
        else
        {
            // if radius is very big, then simply draw a line
            ctx.strokeLine( 0, canvas.getHeight() / 2, canvas.getWidth(), canvas.getHeight() / 2 );
        }

        ctx.setTextAlign( TextAlignment.CENTER );
        ctx.setTextBaseline( VPos.TOP );
        ctx.setFill( RED_COLOR );

        // draw an octant every 45 degrees
        octants.forEach( ( octantName, octantCoordinates ) -> {
            // Apply the projection to the coordinates
            CartesianCoordinates textCenter = projection.apply( octantCoordinates );
            // and transform the coordinates into the canvas coordinate system
            Point2D transformedTextCenter = planeToCanvas.transform( textCenter.x(), textCenter.y()  );
            ctx.fillText( octantName, transformedTextCenter.getX(), transformedTextCenter.getY() );
        } );
    }
}
