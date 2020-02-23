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
        if (azDeg < 0 || azDeg >= 360 || altDeg < -90 || altDeg > 90) {
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
        if ( az <= Angle.ofDeg(22.5) || az > Angle.ofDeg( 337.5 ) ){ return n;}
        else if( az > Angle.ofDeg(22.5) && az <= Angle.ofDeg(67.5) ){ return n + e; }
        else if( az > Angle.ofDeg(67.5) && az <= Angle.ofDeg(112.5) ){ return e; }
        else if( az > Angle.ofDeg(112.5) && az <= Angle.ofDeg(157.5) ){ return s + e; }
        else if( az > Angle.ofDeg(157.5) && az <= Angle.ofDeg(202.5) ){ return s; }
        else if( az > Angle.ofDeg(202.5) && az <= Angle.ofDeg(247.5) ){ return s + w; }
        else if( az > Angle.ofDeg(247.5) && az <= Angle.ofDeg(292.5) ){ return w; }
        else if( az > Angle.ofDeg(292.5) && az <= Angle.ofDeg(337.5) ){ return n + w; }
        else {
            throw new IllegalArgumentException();
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
