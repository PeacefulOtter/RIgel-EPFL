package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * Represents a model of the moon
 */
public enum MoonModel implements CelestialObjectModel<Moon>
{

    MOON();

    // Moon constants used to determine the moon's position
    private static final double AVERAGE_LONGITUDE = Angle.ofDeg( 91.929336 );
    private static final double PERIGEE_AVERAGE_LONGITUDE = Angle.ofDeg( 130.143076 );
    private static final double ASCENDING_NODE_LONGITUDE = Angle.ofDeg( 291.682547 );
    private static final double INCLINATION_ORBIT = Angle.ofDeg( 5.145396 );
    private static final double COS_INCLINATION_ORBIT = Math.cos( INCLINATION_ORBIT );
    private static final double SIN_INCLINATION_ORBIT = Math.sin( INCLINATION_ORBIT );
    private static final double ORBITAL_ECCENTRICITY = 0.0549;
    private static final double ORBITAL_ECCENTRICITY_SQUARED = Math.pow( ORBITAL_ECCENTRICITY, 2 );

    // All other constants needed for the calculation
    private static final double AVERAGE_LONGITUDE_CONST = Angle.ofDeg( 13.1763966 );
    private static final double MEAN_ANOMALY_CONST = Angle.ofDeg( 0.1114041 );
    private static final double EVECTION_CONST = Angle.ofDeg( 1.2739 );
    private static final double ANNUAL_EQUATION_CORRECTION_CONST = Angle.ofDeg( 0.1858 );
    private static final double THIRD_CORRECTION_CONST = Angle.ofDeg( 0.37 );
    private static final double CENTER_EQUATION_CORRECTION_CONST = Angle.ofDeg( 6.2886 );
    private static final double FOURTH_CORRECTION_CONST = Angle.ofDeg( 0.214 );
    private static final double VARIATION_CONST = Angle.ofDeg( 0.6583 );
    private static final double CORRECTED_ASCENDING_NODE_CONST  = Angle.ofDeg( 0.16 );
    private static final double AVERAGE_ASCENDING_NODE_CONST  = Angle.ofDeg( 0.0529539 );
    private static final double ANGULAR_SIZE_CONST = Angle.ofDeg( 0.5181 );

    // ecliptic longitude interval
    private static final RightOpenInterval lonInterval = RightOpenInterval.of( 0, Angle.TAU );
    // phase interval
    private static final ClosedInterval phaseInterval = ClosedInterval.of( 0, 1 );


    /**
     * @param daysSinceJ2010 : number of days after the J2010 (possibly negative)
     * @param eclipticToEquatorialConversion : Conversion used to get its equatorial coordinates from its ecliptic coordinates
     * @return the moon at a certain instant
     */
    @Override
    public Moon at(
            double daysSinceJ2010,
            EclipticToEquatorialConversion eclipticToEquatorialConversion )
    {
        double averageOrbitalLongitude = AVERAGE_LONGITUDE_CONST * daysSinceJ2010 + AVERAGE_LONGITUDE;

        double meanAnomaly = averageOrbitalLongitude - MEAN_ANOMALY_CONST * daysSinceJ2010 - PERIGEE_AVERAGE_LONGITUDE;

        Sun sun = SunModel.SUN.at( daysSinceJ2010, eclipticToEquatorialConversion );
        double sunLon = sun.eclipticPos().lon();
        double sinSunMeanAnomaly = Math.sin( sun.meanAnomaly() );

        double evection = EVECTION_CONST * Math.sin( 2 * ( averageOrbitalLongitude - sunLon ) - meanAnomaly );
        double annualEquationCorrection = ANNUAL_EQUATION_CORRECTION_CONST * sinSunMeanAnomaly;
        double thirdCorrection = THIRD_CORRECTION_CONST * sinSunMeanAnomaly;

        double correctedAnomaly = meanAnomaly + evection - annualEquationCorrection - thirdCorrection;

        double centerEquationCorrection = CENTER_EQUATION_CORRECTION_CONST * Math.sin( correctedAnomaly );
        double fourthCorrection = FOURTH_CORRECTION_CONST * Math.sin( 2 * correctedAnomaly );

        double correctedOrbitalLongitude = averageOrbitalLongitude + evection + centerEquationCorrection - annualEquationCorrection + fourthCorrection;

        double variation = VARIATION_CONST * Math.sin( 2 * ( correctedOrbitalLongitude - sunLon ) );

        double trueOrbitalLongitude = correctedOrbitalLongitude + variation;

        double averageAscendingNodeLongitude = ASCENDING_NODE_LONGITUDE - AVERAGE_ASCENDING_NODE_CONST * daysSinceJ2010;
        double correctedAscendingNodeLongitude = averageAscendingNodeLongitude - CORRECTED_ASCENDING_NODE_CONST * sinSunMeanAnomaly;

        double deltaLon = trueOrbitalLongitude - correctedAscendingNodeLongitude;
        double cosDeltaLon = Math.cos( deltaLon );
        double sinDeltaLon = Math.sin( deltaLon );

        double eclipticLongitude = lonInterval.reduce( Math.atan2(
                sinDeltaLon * COS_INCLINATION_ORBIT,
                cosDeltaLon
        ) + correctedAscendingNodeLongitude );
        double eclipticLatitude = Math.asin( sinDeltaLon  * SIN_INCLINATION_ORBIT );

        EquatorialCoordinates equatorialPos = eclipticToEquatorialConversion.apply( EclipticCoordinates.of( eclipticLongitude, eclipticLatitude ) );



        double orbitalHalfAxisLength = ( 1 - ORBITAL_ECCENTRICITY_SQUARED )  /  ( 1 + ORBITAL_ECCENTRICITY * Math.cos( correctedAnomaly + centerEquationCorrection ) );
        double angularSize =  ANGULAR_SIZE_CONST / orbitalHalfAxisLength;


        double phase = phaseInterval.clip( ( 1 - Math.cos( trueOrbitalLongitude - sunLon ) ) / 2 );


        return new Moon( equatorialPos, (float)angularSize, 0, (float)phase );
    }
}
