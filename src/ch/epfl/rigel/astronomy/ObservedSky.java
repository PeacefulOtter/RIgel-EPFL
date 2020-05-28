
package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;

import java.time.ZonedDateTime;
import java.util.*;
/*
    represents a set of celestial objects projected in the plane by a stereographic projection.
    In other words, it represents a kind of photograph of the sky at a given time and place of observation.
 */
public class ObservedSky
{
    // list of planetModel without earth
    private final List<PlanetModel> planetsModelWithoutEarth;
    // list of planet without earth
    private final List<Planet> planetsWithoutEarth;
    // hashmap linking a celestial object with his Cartesian Coordinates
    private final HashMap<CelestialObject, CartesianCoordinates> planetCartesianCoordinates;
    private final Set<CelestialObject> celestialObjects;
    private final EquatorialToHorizontalConversion conversionToHorizontal;
    private final StereographicProjection projection;
    private final Sun sun;
    private final Moon moon;
    private StarCatalogue catalogue;

    /**
     * calculates the projected position in the plane of all celestial objects: the Sun,
     * the Moon, the planets of the solar system - except the Earth - and the stars in the catalogue.
     * @param moment: the time of observation (given by a "zoned" date/time pair),
     * @param position: the observation position (given by its geographical coordinates)
     * @param projection : the stereographic projection to be used
     * @param catalogue : the catalogue containing the stars and asterisms
     */
    public ObservedSky(
            ZonedDateTime moment,
            GeographicCoordinates position,
            StereographicProjection projection,
            StarCatalogue catalogue)
    {
        this.catalogue = catalogue;
        this.projection = projection;
        planetsWithoutEarth = new ArrayList<>();
        planetCartesianCoordinates = new HashMap<>();
        planetsModelWithoutEarth = new ArrayList<>( PlanetModel.ALL );
        planetsModelWithoutEarth.remove(PlanetModel.EARTH);
        celestialObjects = new HashSet<>();

        EclipticToEquatorialConversion conversionToEquatorial = new EclipticToEquatorialConversion( moment );
        this.conversionToHorizontal = new EquatorialToHorizontalConversion( moment, position );
        double daysUntil = Epoch.J2010.daysUntil( moment );

        sun = SunModel.SUN.at( daysUntil, conversionToEquatorial );
        moon = MoonModel.MOON.at( daysUntil, conversionToEquatorial );
        celestialObjects.add( sun );
        celestialObjects.add( moon );

        for ( PlanetModel planet : planetsModelWithoutEarth )
        {
            Planet solarPlanet = planet.at( daysUntil, conversionToEquatorial );
            celestialObjects.add( solarPlanet );
            planetsWithoutEarth.add( solarPlanet );
        }

        celestialObjects.addAll( catalogue.stars() );

        for ( CelestialObject planet : celestialObjects )
        {
            planetCartesianCoordinates.put(
                    planet,
                    projection.apply( conversionToHorizontal.apply( planet.equatorialPos() ) ) );
        }
    }

    /**
     *
     * @return list of the stars in the catalogue
     */
    public List<Star> stars() { return List.copyOf( this.catalogue.stars() ); }

    /**
     *
     * @return return a list of stars carstesian coordinates
     */
    public List<CartesianCoordinates> starPosition()
    {
        List<Star> allStars = stars();
        List<CartesianCoordinates> cartesianCoordinates = new ArrayList<>();
        for ( Star star: allStars )
        {
            // convert coordinates to horizontal and then apply the projection
            cartesianCoordinates.add( this.projection.apply( conversionToHorizontal.apply( star.equatorialPos() ) ) );
        }
        return cartesianCoordinates;
    }

    /**
     *
     * @return return a double array of all stars carstesian coordinates
     * contains at position 0 the x-coordinate of the first star, at position 1 the y-coordinate of the same star.
     */
    public double[] starsArrayPosition()
    {
        List<CartesianCoordinates> allStars = starPosition();
        double[] starsCoordinates = new double[ allStars.size() * 2 ];
        int i = 0;
        for ( CartesianCoordinates starCartesianCoordinates : allStars )
        {
            starsCoordinates[ i++ ] = starCartesianCoordinates.x();
            starsCoordinates[ i++ ]  = starCartesianCoordinates.y();
        }
        return starsCoordinates;
    }

    public Sun sun() { return sun; }

    public CartesianCoordinates sunPosition() { return planetCartesianCoordinates.get( sun ); }

    public Moon moon() { return moon; }

    public CartesianCoordinates moonPosition()
    {
        return planetCartesianCoordinates.get( moon );
    }

    /**
     *
     * @return a list of all solar system planet without Earth
     */
    public List<Planet> planets() { return planetsWithoutEarth; }

    /**
     *
     * @return return a double array of all planet carstesian coordinates
     * contains at position 0 the x-coordinate of the first planet, at position 1 the y-coordinate of the same planet.
     */
    public double[] planetPosition()
    {
        double[] cartesianCoordinates = new double[ 14 ];
        int i = 0;
        for ( Planet planet : planetsWithoutEarth )
        {
            cartesianCoordinates[ i++ ] = planetCartesianCoordinates.get( planet ).x();
            cartesianCoordinates[ i++ ] = planetCartesianCoordinates.get( planet ).y();
        }
        return cartesianCoordinates;
    }

    /**
     *
     * @return list of asterisms in the catalogue
     */
    public Set<Asterism> getAsterism() { return catalogue.asterisms(); }

    public List<Integer> asterismIndices( Asterism asterism ) { return catalogue.asterismIndices( asterism ); }


    /**
     *
     * @return the closest celestial object to that point, as long as it is at a distance less than the maximum distance.
     */
    public Optional<CelestialObject> objectClosestTo( CartesianCoordinates coordinates, double maximalDistance )
    {
        double distance = maximalDistance;
        double distanceBetween;
        CelestialObject currentObject = null;
        for ( CelestialObject object : celestialObjects )
        {
            distanceBetween = distanceBetween( planetCartesianCoordinates.get( object ), coordinates );
            if ( distance > distanceBetween )
            {
                distance = distanceBetween;
                currentObject = object;
            }
        }

        if ( distance == maximalDistance )
        {
            return Optional.empty();
        }
        else
        {
            assert currentObject != null;
            return Optional.of( currentObject );
        }
    }

    /**
     *
     * @param point1
     * @param point2
     * @return the distance between the 2 points
     */
    private double distanceBetween( CartesianCoordinates point1, CartesianCoordinates point2 )
    {
        return Math.sqrt( Math.pow( point2.x() - point1.x(), 2 ) + Math.pow( point2.y() - point1.y(), 2 ) );
    }

}
