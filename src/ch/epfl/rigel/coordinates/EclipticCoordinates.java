package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

import java.util.Locale;

public final class EclipticCoordinates extends SphericalCoordinates
{

    private EclipticCoordinates( double lon, double lat )
    {
        this.lon = lon;
        this.lat = lat;
    }

    public EclipticCoordinates of( double lon, double lat )
    {
        if (lon < 0 || lon >= 2*Math.PI || lat < - Math.PI/2 || lat > Math.PI/2) {
            throw new IllegalArgumentException();
        }
        return new EclipticCoordinates( lon, lat );
    }

    @Override
    public double lon()
    {
        return lon;
    }

    @Override
    public double lonDeg()
    {
        return Angle.toDeg(lon);
    }

    @Override
    public double lat() { return lat; }

    @Override
    public double latDeg() { return Angle.toDeg(lat); }

    @Override
    public String toString()
    {
        return String.format( Locale.ROOT, "(λ=%.4f°, β=%.4f°)", Angle.toDeg(lon), Angle.toDeg(lat) );
    }
}
