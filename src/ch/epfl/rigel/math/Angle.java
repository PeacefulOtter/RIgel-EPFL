package ch.epfl.rigel.math;

public final class Angle
{
    public static final double TAU = 2 * Math.PI;
    private static final double HR_PER_RAD = 24 / TAU;
    private static final double RAD_PER_HR = TAU / 24;

    //normalise l'angle rad en le réduisant à l'intervalle [0,τ[
    public static double normalizePositive( double rad ) {
        return rad % TAU;
    }

    // retourne l'angle correspondant au nombre de secondes d'arc donné, qui peut être quelconque (y compris négatif)
    public static double ofArcsec( double sec ) {
        return sec * ( TAU / 3600 );
    }

    // retourne l'angle correspondant à l'angle deg​° min​′ sec​″, ou lève IllegalArgumentException si les minutes données ne sont pas comprises entre 0 (inclus) et 60 (exclus), ou si les secondes ne sont pas comprises entre 0 (inclus) et 60 (exclus),
    public static double ofDMS( int deg, int min, double sec ) {
        if ( min < 0 || min >= 60 || sec < 0 || min >= 60 )
        {
            throw new IllegalArgumentException();
        }
        return ofDeg( deg ) + ofArcsec( min * 60 + sec );
    }

    // retourne l'angle correspondant à l'angle en degrés donné
    public static double ofDeg(double deg) {
        return Math.toRadians( deg );
    }

    // retourne l'angle en degrés correspondant à l'angle donné
    public static double toDeg(double rad) {
        return Math.toDegrees( rad );
    }

    // retourne l'angle correspondant à l'angle en heures donné
    public static double ofHr(double hr) {
        return hr * RAD_PER_HR;
    }

    // retourne l'angle en heures correspondant à l'angle donné
    public static double toHr(double rad) {
        return rad * HR_PER_RAD;
    }
}
