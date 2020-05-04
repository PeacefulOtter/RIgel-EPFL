package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.Asterism;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.coordinates.*;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.util.List;
import java.util.Set;

public class SkyCanvasPainter
{
    private static final Color BLUE_COLOR = Color.BLUE;
    private static final Color LIGHTGRAY_COLOR = Color.LIGHTGRAY;
    private static final Color WHITE_COLOR = Color.WHITE;
    private static final Color RED_COLOR = Color.RED;
    private static final Color YELLOW_COLOR = Color.YELLOW;
    private static final Color YELLOW_COLOR_HALO = YELLOW_COLOR.deriveColor( 0, 1, 1, 0.25 );
    private static final ClosedInterval MAGNITUDE_INTERVAL = ClosedInterval.of( -2, 5 );

    private final Canvas canvas;
    private final GraphicsContext ctx;

    public SkyCanvasPainter( Canvas canvas )
    {
        this.canvas = canvas;
        ctx = canvas.getGraphicsContext2D();
    }

    private static double magnitudeDiameter( double magnitude, StereographicProjection projection )
    {
        double clippedMagnitude = MAGNITUDE_INTERVAL.clip( magnitude );
        double sizeFactor = ( 99 - 17 * clippedMagnitude ) / 140;
        return sizeFactor * projection.applyToAngle( Angle.ofDeg( 0.5 ) );
    }

    public void clear()
    {
        ctx.setFill( Color.BLACK );
        ctx.fillRect( 0, 0, canvas.getWidth(), canvas.getHeight() );
        ctx.fill();
    }

    public void drawSky( ObservedSky sky, StereographicProjection projection, Transform planeToCanvas )
    {
        this.drawStars( sky, projection, planeToCanvas );
        this.drawPlanets( sky, projection, planeToCanvas );
        this.drawSun( sky, projection, planeToCanvas );
        this.drawMoon( sky, projection, planeToCanvas );
        this.drawHorizon( projection, planeToCanvas );
    }


    public void drawStars( ObservedSky sky, StereographicProjection projection, Transform planeToCanvas )
    {
        Set<Asterism> asterisms = sky.getAsterism();
        List<Star> stars = sky.stars();
        List<CartesianCoordinates> cartesianCoordinates = sky.starPosition();
        double[] starsCartesianCoordinates = sky.starsArrayPosition();
        int starsNumber = stars.size();

        double[] dstPts = new double[ starsNumber ];
        planeToCanvas.transform2DPoints( starsCartesianCoordinates, 0, dstPts, 0, starsNumber );

        int starCoordsIndex = 0;
        for ( Star star : stars )
        {
            int roundedColor = ( ( ( star.colorTemperature() + 499 ) / 500 ) * 500 ); // round to the nearest 500
            Color starColor = BlackBodyColor.colorForTemperature( roundedColor );
            ctx.setFill( starColor );

            double starDiameter = magnitudeDiameter( star.magnitude(), projection );
            double finalDiameter = planeToCanvas.deltaTransform( starDiameter, 0 ).getX();
            double radius = finalDiameter / 2;

            double starX = dstPts[ starCoordsIndex++ ] - radius;
            double starY = dstPts[ starCoordsIndex++ ] - radius;

            ctx.fillOval( starX, starY, finalDiameter, finalDiameter );
        }




        
        for ( Asterism asterism: asterisms )
        {
            List<Integer> asterismIndice = sky.asterismIndices( asterism );
            boolean firstAsterism = true;
            boolean lastStarOutsideCanvas = false;

            boolean currentOutsideCanvas = false;

            // ASTERISM DRAWING
            ctx.beginPath();
            for ( Integer indice : asterismIndice )
            {
                CartesianCoordinates cartesianCoords = cartesianCoordinates.get( indice );
                Point2D starPoint = planeToCanvas.transform( cartesianCoords.x(), cartesianCoords.y() );

                if ( firstAsterism )
                {
                    ctx.moveTo( starPoint.getX(), starPoint.getY() );
                    firstAsterism = false;
                    lastStarOutsideCanvas = !canvas.getBoundsInLocal().contains( starPoint );
                    continue;
                }

                // avoid drawing the asterism branches outside the canvas
                if ( !canvas.getBoundsInLocal().contains( starPoint ) )
                {
                    if ( !lastStarOutsideCanvas ) { lastStarOutsideCanvas = true; }
                    else
                    {
                        ctx.moveTo( starPoint.getX(), starPoint.getY() );
                        lastStarOutsideCanvas = false;
                        continue;
                    }
                }

                ctx.setLineWidth(1);
                ctx.setStroke( BLUE_COLOR );
                ctx.lineTo( starPoint.getX(), starPoint.getY() );
                ctx.stroke();
            }
            ctx.closePath();
        }
    }

    public void drawPlanets( ObservedSky sky, StereographicProjection projection, Transform planeToCanvas )
    {
        List<Planet> planets = sky.planets();
        double[] planetCartesianCoordinates = sky.planetPosition();
        int index = 0;

        for ( Planet planet : planets )
        {
            Point2D planetPoint = planeToCanvas.transform( planetCartesianCoordinates[ index ], planetCartesianCoordinates[ index + 1 ] );

            double planetDiameter = magnitudeDiameter( planet.magnitude(), projection );
            double finalDiameter = planeToCanvas.deltaTransform( planetDiameter, 0 ).getX();
            double radius = finalDiameter / 2;

            // System.out.println(planetPoint + " " + finalDiameter);

            ctx.setFill( LIGHTGRAY_COLOR );
            ctx.fillOval( planetPoint.getX() - radius, planetPoint.getY() - radius, finalDiameter, finalDiameter );

            index += 2;
        }
    }

    public void drawSun( ObservedSky sky, StereographicProjection projection, Transform planeToCanvas )
    {
        CartesianCoordinates sunPos = sky.sunPosition();
        Point2D sunPoint = planeToCanvas.transform( sunPos.x(), sunPos.y() );

        double sunDiameter = projection.applyToAngle( Angle.ofDeg( 0.5 ) );
        double finalDiameter = planeToCanvas.deltaTransform( sunDiameter, 0 ).getX();

        ctx.setFill( YELLOW_COLOR_HALO );
        double haloRadius = ( finalDiameter * 2.2 ) / 2;
        ctx.fillOval( sunPoint.getX() - haloRadius, sunPoint.getY() - haloRadius, finalDiameter * 2.2, finalDiameter * 2.2 );

        ctx.setFill( YELLOW_COLOR );
        double yellowRadius = ( finalDiameter + 2 ) / 2;
        ctx.fillOval( sunPoint.getX() - yellowRadius, sunPoint.getY() - yellowRadius, finalDiameter + 2, finalDiameter + 2 );

        ctx.setFill( WHITE_COLOR );
        double whiteRadius = finalDiameter / 2;
        ctx.fillOval( sunPoint.getX() - whiteRadius, sunPoint.getY() - whiteRadius, finalDiameter, finalDiameter );

    }

    public void drawMoon( ObservedSky sky, StereographicProjection projection, Transform planeToCanvas )
    {
        CartesianCoordinates moonPos = sky.moonPosition();
        System.out.println(moonPos);
        Point2D moonPoint = planeToCanvas.transform( moonPos.x(), moonPos.y() );
        System.out.println(moonPoint);

        //double moonDiameter = magnitudeDiameter( moon.magnitude(), projection );
        double projectedDiameter = projection.applyToAngle( Angle.ofDeg( 0.5 ) );
        double finalDiameter = planeToCanvas.deltaTransform( projectedDiameter, 0 ).getX();
        double radius = finalDiameter / 2;

        System.out.println(finalDiameter);
        ctx.setFill( WHITE_COLOR );
        ctx.fillOval( moonPoint.getX() - radius, moonPoint.getY() - radius, finalDiameter, finalDiameter );
    }

    public void drawHorizon( StereographicProjection projection, Transform planeToCanvas )
    {
        HorizontalCoordinates hor = HorizontalCoordinates.ofDeg( 0, 0 );
        CartesianCoordinates center = projection.circleCenterForParallel( hor );
        Point2D transformedCenter = planeToCanvas.transform( center.x(), center.y() );
        double radius = projection.circleRadiusForParallel( hor );
        double transformedRadius = Math.abs( planeToCanvas.deltaTransform( radius, 0 ).getX() );

        ctx.setStroke( RED_COLOR );
        ctx.setLineWidth( 2 );
        ctx.strokeOval(
                transformedCenter.getX() - transformedRadius,
                transformedCenter.getY() - transformedRadius,
                transformedRadius * 2, transformedRadius * 2 );

        ctx.setTextAlign( TextAlignment.CENTER );
        ctx.setTextBaseline( VPos.TOP );
        ctx.setFill( RED_COLOR );
        for ( int i = 0; i < 360; i += 45 )
        {
            HorizontalCoordinates textCoords = HorizontalCoordinates.ofDeg( i, -0.5 );
            CartesianCoordinates textCenter = projection.apply( textCoords );
            Point2D transformedTextCenter = planeToCanvas.transform( textCenter.x(), textCenter.y()  );
            String octant = textCoords.azOctantName( "N", "E", "S", "W" );
            ctx.fillText( octant, transformedTextCenter.getX(), transformedTextCenter.getY() );
        }
    }
}
