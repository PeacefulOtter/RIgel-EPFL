package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.StarCatalogue;
import javafx.beans.property.ObjectProperty;
import javafx.scene.canvas.Canvas;

public class SkyCanvasManager
{
    public SkyCanvasManager(
            StarCatalogue catalogue,
            DateTimeBean dateTimeBean,
            ObserverLocationBean observerLocationBean,
            ViewingParametersBean viewingParametersBean )
    {
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
