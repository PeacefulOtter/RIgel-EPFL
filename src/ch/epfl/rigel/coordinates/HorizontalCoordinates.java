package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

import java.util.Locale;

public final class HorizontalCoordinates extends SphericalCoordinates
{
    private final double az;
    private final double alt;

    private HorizontalCoordinates( double az, double alt )
    {
        this.az = az;
        this.alt = alt;
    }

    public static HorizontalCoordinates of( double az, double alt )
    {
        if (az < 0 || az >= 2*Math.PI || alt < - Math.PI/2 || alt > Math.PI/2) {
            throw new IllegalArgumentException();
        }
        // TO CHECK
        return new HorizontalCoordinates( az, alt );
    }

    public static HorizontalCoordinates ofDeg( double azDeg, double altDeg )
    {
        if (az < 0 || az >= 360 || alt < -90 || alt > 90) {
            throw new IllegalArgumentException();
        }
        // TO CHECK
        return new HorizontalCoordinates( Angle.ofDeg( azDeg ), Angle.ofDeg( altDeg ) );
    }

    public double az()
    {
        return az;
    }

    public double azDeg()
    {
        return Angle.toDeg( az );
    }

    public String azOctantName( String n, String e, String s, String w )
    {
        switch (az) {
            case az <= Angle.ofDeg(22.5) || az > Angle.ofDeg( 337.5 ) :
                return n;
            case az > Angle.ofDeg(22.5) && az <= Angle.ofDeg(67.5):
                return n + e;
            case az > Angle.ofDeg(67.5) && az <= Angle.ofDeg(112.5):
                return e;
            case az > Angle.ofDeg(112.5) && az <= Angle.ofDeg(157.5):
                return s + e;
            case az > Angle.ofDeg(157.5) && az <= Angle.ofDeg(202.5):
                return s;
            case az > Angle.ofDeg(202.5) && az <= Angle.ofDeg(247.5):
                return s + w;
            case az > Angle.ofDeg(247.5) && az <= Angle.ofDeg(292.5):
                return w;
            case az > Angle.ofDeg(292.5) && az <= Angle.ofDeg(337.5):
                return n + w;
        }
    }

    public double alt() { return alt; }


    public double altDeg() { return Angle.toDeg( alt ); }

    public double angularDistanceTo( HorizontalCoordinates that )
    {
        return Math.acos(Math.sin(that.alt) * Math.sin(this.alt) + Math.cos(that.alt) * Math.cos(this.alt) * Math.cos(that.az - this.az));
    }

    @Override
    double lon()
    {
        return 0;
    }

    @Override
    double lonDeg()
    {
        return 0;
    }

    @Override
    double lat()
    {
        return 0;
    }

    @Override
    double latDeg()
    {
        return 0;
    }

    @Override
    public String toString()
    {
        return String.format( Locale.ROOT, "(az=%.4f°, alt=%.4f°)", az, alt );
    }
}
