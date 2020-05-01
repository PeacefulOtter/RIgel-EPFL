package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.StarCatalogue;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.scene.canvas.Canvas;

public class SkyCanvasManager
{
    public DoubleProperty mouseAzDeg, mouseAltDeg;
    public ObservableObjectValue<CelestialObject> objectUnderMouse ;

    public SkyCanvasManager(
            StarCatalogue catalogue,
            DateTimeBean dateTimeBean,
            ObserverLocationBean observerLocationBean,
            ViewingParametersBean viewingParametersBean )
    {
        Canvas canvas = new Canvas( 800, 600 );
        SkyCanvasPainter painter = new SkyCanvasPainter( canvas );
    }

    public ObjectProperty objectUnderMouseProperty()
    {
        return null;
    }

    public Canvas canvas()
    {
        return null;
    }
}
