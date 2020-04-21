package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.Asterism;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.coordinates.EquatorialToHorizontalConversion;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.ClosedInterval;
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
        return sizeFactor * 2 * Math.tan( 0.5 / 4 );
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
        ctx.beginPath();
        for ( Asterism asterism: asterisms )
        {
            List<Star> stars = asterism.stars();
            boolean firstAsterism = true;
            for ( Star star: stars )
            {
                EquatorialCoordinates equatorialPos = star.equatorialPos();

                // ASTERISM DRAWING
                ctx.setFill( BLUE_COLOR );
                ctx.setLineWidth(1);
                if ( firstAsterism )
                {
                    ctx.lineTo( equatorialPos.ra(), equatorialPos.ra() );
                    firstAsterism = false;
                }
                ctx.moveTo( equatorialPos.ra(), equatorialPos.dec() );

                // STAR DRAWING
                Color starColor = BlackBodyColor.colorForTemperature( star.colorTemperature() );
                double starDiameter = magnitudeDiameter( star.magnitude() );
                ctx.setFill( starColor );
                ctx.fillOval( equatorialPos.ra(), equatorialPos.dec(), starDiameter, starDiameter );
            }

        }
        ctx.closePath();
    }

    public void drawPlanets( ObservedSky sky, StereographicProjection projection, Transform planeToCanvas ) {}

    public void drawSun( ObservedSky sky, StereographicProjection projection, Transform planeToCanvas ) {}

    public void drawMoon( ObservedSky sky, StereographicProjection projection, Transform planeToCanvas ) {}

    public void drawHorizon() {}
}
