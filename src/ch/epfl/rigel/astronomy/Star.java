package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

public final class Star extends CelestialObject {
    private final static ClosedInterval COLOR_INTERVAL = ClosedInterval.of(-0.5, 5.5);
    private int hipparcosId;
    private float colorIndex;

    Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex) {
        super(name, equatorialPos, 0, magnitude);
        if( hipparcosId < 0 || COLOR_INTERVAL.contains(colorIndex)){
            throw new IllegalArgumentException();
        }

        this.colorIndex = colorIndex;
        this.hipparcosId = hipparcosId;
    }

    public int hipparcosId(){ return  hipparcosId; }
    public int colorTemperature(){
       return 0;
    }
}
