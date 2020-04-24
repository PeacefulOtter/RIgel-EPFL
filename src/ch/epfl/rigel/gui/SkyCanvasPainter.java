package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.Asterism;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.coordinates.*;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

import java.util.List;
import java.util.Set;

public class SkyCanvasPainter
{
    private static final Color BLUE_COLOR = Color.BLUE;
    private static final Color LIGHTGRAY_COLOR = Color.LIGHTGRAY;
    private static final Color WHITE_COLOR = Color.WHITE;
    private static final Color RED_COLOR = Color.RED;
    private static final Color YELLOW_COLOR = Color.YELLOW;
    private static final ClosedInterval MAGNITUDE_INTERVAL = ClosedInterval.of( -2, 5 );

    private final Canvas canvas;
    private final GraphicsContext ctx;

    public SkyCanvasPainter( Canvas canvas )
    {
        this.canvas = canvas;
        ctx = canvas.getGraphicsContext2D();
    }

    private static double angularSizeDiameter( double angularSize )
    {
        return 2 * Math.tan( angularSize / 4 );
    }

    private static double magnitudeDiameter( double magnitude )
    {
        double clippedMagnitude = MAGNITUDE_INTERVAL.clip( magnitude );
        double sizeFactor = ( 99 - 17 * clippedMagnitude ) / 140;
        return sizeFactor * 2 * Math.tan( Angle.ofDeg( 0.5 ) / 4 );
    }

    public void clear()
    {
        ctx.setFill( Color.BLACK );
        ctx.fillRect( 0, 0, canvas.getWidth(), canvas.getHeight() );
        ctx.fill();
    }

    public void drawStars( ObservedSky sky, StereographicProjection projection, Transform planeToCanvas )
    {
        Set<Asterism> asterisms = sky.getAsterism();
        List<Star> stars = sky.stars();
        List<CartesianCoordinates> cartesianCoordinates = sky.starPosition();

        /*for ( Asterism asterism: asterisms )
        {
            List<Integer> asterismIndice = sky.asterismIndices( asterism );
            boolean firstAsterism = true;

            // ASTERISM DRAWING
            ctx.beginPath();
            for ( Integer indice : asterismIndice )
            {
                CartesianCoordinates cartesianCoords = cartesianCoordinates.get( indice );
                Point2D canvasPoint = planeToCanvas.transform( cartesianCoords.x(), cartesianCoords.y() );

                // avoid drawing the asterism branches outside the canvas
                if ( !ctx.getCanvas().getBoundsInLocal().contains( canvasPoint ) ) continue;

                ctx.setLineWidth(1);
                ctx.setStroke( BLUE_COLOR );
                if ( firstAsterism )
                {
                    ctx.moveTo( canvasPoint.getX(), canvasPoint.getY() );
                    firstAsterism = false;
                } else
                {
                    ctx.lineTo( canvasPoint.getX(), canvasPoint.getY() );
                }
                ctx.stroke();
            }
            ctx.closePath();
        }*/

        for ( int i = 0; i < stars.size(); i++ )
        {
            Star star = stars.get( i );
            CartesianCoordinates cartesianCoords = cartesianCoordinates.get( i );
            Point2D canvasPoint = planeToCanvas.transform( cartesianCoords.x(), cartesianCoords.y() );

            int roundedColor = ( ( ( star.colorTemperature() + 499 ) / 500 ) * 500 ); // round to the nearest 500
            Color starColor = BlackBodyColor.colorForTemperature( roundedColor );
            double starDiameter = magnitudeDiameter( star.magnitude() );
            double projectedDiameter = projection.applyToAngle( starDiameter );
            double finalDiameter = planeToCanvas.deltaTransform( projectedDiameter, 0 ).getX();

            // DEBUG : System.out.println( canvasPoint + " " + starDiameter +" " + projectedDiameter + " " + finalDiameter );

            ctx.setFill( starColor );
            ctx.fillOval( canvasPoint.getX(), canvasPoint.getY(), finalDiameter, finalDiameter );
        }

        System.out.println(canvas.getWidth() + " " + canvas.getHeight() );
    }

    public void drawPlanets( ObservedSky sky, StereographicProjection projection, Transform planeToCanvas ) {}

    public void drawSun( ObservedSky sky, StereographicProjection projection, Transform planeToCanvas ) {}

    public void drawMoon( ObservedSky sky, StereographicProjection projection, Transform planeToCanvas ) {}

    public void drawHorizon() {}
}
