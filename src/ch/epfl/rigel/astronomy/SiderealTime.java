package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;

import java.time.ZonedDateTime;

public final class SiderealTime
{
    /**
     * @param when: the actual time date and hour
     * @return : the Greenwish Sidereal time in radians for a precise date and hour (when)
     */
    public static double greenwich( ZonedDateTime when )
    {
        double T = Epoch.J2000.julianCenturiesUntil( when );
        double t = Epoch.J2000.daysUntil( when );
        double S0 = 0.000025862 * Math.pow( T, 2 ) + 2400.051336 * T + 6.697374558;
        double S1 = 1.002737909 * t;
        double Sg = S0 + S1;
        return Angle.normalizePositive( Angle.ofHr( Sg ) );
    }

    /**
     * @param when: the actual time date and hour
     * @param where: a position
     * @return: the local Sidereal time in radians for a precise date and hour (when) and a position (where)
     */
    public static double local( ZonedDateTime when, GeographicCoordinates where )
    {
        return greenwich( when ) + where.lon();
    }
}
