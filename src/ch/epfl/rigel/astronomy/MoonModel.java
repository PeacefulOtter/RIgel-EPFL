package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

public enum MoonModel implements CelestialObjectModel<Moon>
{

    MOON();

    private static final double averageLongitude = Angle.ofDeg( 91.929336 );
    private static final double perigeeAverageLongitude = Angle.ofDeg( 130.143076 );
    private static final double ascendingNodeLongitude = Angle.ofDeg( 291.682457 );
    private static final double inclinationOrbit = Angle.ofDeg( 5.145396 );
    private static final double orbitalEccentricity = 0.0549;

    private static final RightOpenInterval lonInterval = RightOpenInterval.of( 0, Angle.TAU );
    private static final ClosedInterval latInterval = ClosedInterval.of( -Angle.TAU / 4, Angle.TAU / 4 );
    private static final ClosedInterval moonPhaseInterval = ClosedInterval.of( 0, 1 );


    @Override
    public Moon at(
            double daysSinceJ2010,
            EclipticToEquatorialConversion eclipticToEquatorialConversion )
    {
        double l = Angle.ofDeg( 13.1763966 ) * daysSinceJ2010 + averageLongitude;
        double Mm = l - Angle.ofDeg( 0.1114041 ) * daysSinceJ2010 - perigeeAverageLongitude;


        Sun sun = SunModel.SUN.at( daysSinceJ2010, eclipticToEquatorialConversion );
        double sunLon = sun.eclipticPos().lon();
        double sunMeanAnomaly = sun.meanAnomaly();
        double sinSunMeanAnomaly = Math.sin( sunMeanAnomaly );

        double Ev = Angle.ofDeg( 1.2739 ) * Math.sin( 2 * ( l - sunLon ) - Mm );
        double Ae = Angle.ofDeg( 0.1858 ) * sinSunMeanAnomaly;
        double A3 = Angle.ofDeg( 0.37 ) * sinSunMeanAnomaly;


        double Mmprime = Mm + Ev - Ae - A3;
        double Ec = Angle.ofDeg( 6.2886 ) * Math.sin( Mmprime );
        double A4 = Angle.ofDeg( 0.214 ) * Math.sin( 2 * Mmprime );

        double lprime = l + Ev + Ec - Ae + A4;
        double V = Angle.ofDeg( 0.6583 ) * Math.sin( 2 * ( lprime - sunLon ) );

        double lprimeprime = lprime + V;





        double N = ascendingNodeLongitude - Angle.ofDeg( 0.0529539 ) * daysSinceJ2010;
        double Nprime = N - Angle.ofDeg( 0.16 ) * sinSunMeanAnomaly;


        double deltaLon = lprimeprime - Nprime;
        double sinDeltaLon = Math.sin( deltaLon );

        double lambdam = lonInterval.reduce( Math.atan2(
                sinDeltaLon * Math.cos( inclinationOrbit ),
                Math.cos( deltaLon )
        ) + Nprime );
        double betam = latInterval.clip( Math.asin( sinDeltaLon  * Math.sin( inclinationOrbit ) ) );

        EquatorialCoordinates equatorialPos = eclipticToEquatorialConversion.apply( EclipticCoordinates.of( lambdam, betam ) );



        double p = ( 1 - Math.pow( orbitalEccentricity, 2 ) )  /  ( 1 + orbitalEccentricity * Math.cos( Mmprime + Ec ) );
        double angularSize = Angle.ofDeg( 0.5181 ) / p;


        // between 0 and 1
        double phase = moonPhaseInterval.clip( ( 1 - Math.cos( lprimeprime - sunLon ) ) / 2 );


        return new Moon( equatorialPos, (float)angularSize, 0, (float)phase );
    }
}
