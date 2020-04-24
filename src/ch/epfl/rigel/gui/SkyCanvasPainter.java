package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
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
        return sizeFactor * 2 * Math.tan( 0.5 / 4 );
    }

    public void clear()
    {

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
                if ( !firstAsterism )
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

    public void drawPlanets( ObservedSky sky, StereographicProjection projection, Transform planeToCanvas ) {
        List<Planet> planets = sky.planets();
        double[] planetCartesianCoordinates = sky.planetPosition();
        int index = 0;
        for (Planet planet : planets)
        {
            double planetDiameter = magnitudeDiameter( planet.magnitude() );
            planetDiameter = projection.applyToAngle(planetDiameter);
            planetDiameter = planeToCanvas.transform( planetDiameter, 0 ).getX();
            Point2D planetPoint = planeToCanvas.transform(planetCartesianCoordinates[index], planetCartesianCoordinates[index + 1]);
            index += 2;

            ctx.setFill( LIGHTGRAY_COLOR );
            ctx.fillOval( planetPoint.getX(), planetPoint.getY(), planetDiameter, planetDiameter );
        }
    }

    public void drawSun( ObservedSky sky, StereographicProjection projection, Transform planeToCanvas ) {
        Sun sun = sky.sun();
        double sunDiameter = magnitudeDiameter(sun.magnitude());
        sunDiameter = projection.applyToAngle(sunDiameter);
        sunDiameter = planeToCanvas.transform(sunDiameter, 0).getX();
        Point2D sunPoint = planeToCanvas.transform(sky.sunPosition().x(), sky.sunPosition().y());

        ctx.setFill(Color.color(YELLOW_COLOR.getRed(), YELLOW_COLOR.getGreen(), YELLOW_COLOR.getBlue(), 0.25));
        ctx.fillOval( sunPoint.getX(), sunPoint.getY(), sunDiameter * 2.2, sunDiameter * 2.2 );

        ctx.setFill(YELLOW_COLOR);
        ctx.fillOval(sunPoint.getX(), sunPoint.getY(), sunDiameter + 2, sunDiameter +2);

        ctx.setFill( WHITE_COLOR );
        ctx.fillOval( sunPoint.getX(), sunPoint.getY(), sunDiameter, sunDiameter );

    }

    public void drawMoon( ObservedSky sky, StereographicProjection projection, Transform planeToCanvas ) {
        Moon moon = sky.moon();
        double moonDiameter = magnitudeDiameter(moon.magnitude());
        moonDiameter = projection.applyToAngle(moonDiameter);
        moonDiameter = planeToCanvas.transform(moonDiameter, 0).getX();
        Point2D moonPoint = planeToCanvas.transform(sky.moonPosition().x(), sky.moonPosition().y());

        ctx.setFill( WHITE_COLOR );
        ctx.fillOval(moonPoint.getX(), moonPoint.getY(), moonDiameter, moonDiameter );
    }

    public void drawHorizon(StereographicProjection projection) {
    }
}
