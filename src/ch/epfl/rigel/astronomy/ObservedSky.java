
package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;

import java.time.ZonedDateTime;
import java.util.*;

public class ObservedSky
{
    private final List<PlanetModel> planetsModelWithoutEarth;
    private final List<Planet> planetsWithoutEarth;
    private final HashMap<CelestialObject, CartesianCoordinates> planetCartesianCoordinates;
    private final Set<CelestialObject> celestialObjects;
    private final EquatorialToHorizontalConversion conversionToHorizontal;
    private final StereographicProjection projection;
    private final Sun sun;
    private final Moon moon;
    private StarCatalogue catalogue;

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
        double daysUntil = Epoch.J2000.daysUntil(moment);

        sun = SunModel.SUN.at( daysUntil, conversionToEquatorial );
        moon = MoonModel.MOON.at( daysUntil, conversionToEquatorial );
        celestialObjects.add( sun );
        celestialObjects.add( moon );

        for ( PlanetModel planet : planetsModelWithoutEarth )
        {
            Planet solarPlanet = planet.at(daysUntil, conversionToEquatorial);
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

    public List<Star> stars() { return List.copyOf( this.catalogue.stars() ); }

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

    public Sun sun() { return sun; }

    public CartesianCoordinates sunPosition() { return planetCartesianCoordinates.get( sun ); }

    public Moon moon() { return moon; }

    public CartesianCoordinates moonPosition()
    {
        return planetCartesianCoordinates.get( moon );
    }

    public List<Planet> planets() { return planetsWithoutEarth; }

    public double[] planetPosition()
    {
        double[] cartesianCoordinates = new double[ 14 ];
        int i = 0;
        for ( Planet planet : planetsWithoutEarth )
        {
            cartesianCoordinates[ i ] = planetCartesianCoordinates.get( planet ).x();
            i++;
            cartesianCoordinates[ i ]  = planetCartesianCoordinates.get( planet ).y();
            i++;
        }
        return cartesianCoordinates;
    }

    public Set<Asterism> getAsterism() { return catalogue.asterisms(); }

    public List<Integer> asterismIndices( Asterism asterism ) { return catalogue.asterismIndices( asterism ); }


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

    private double distanceBetween( CartesianCoordinates point1, CartesianCoordinates point2 )
    {
        return Math.sqrt( Math.pow( point2.x() - point1.x(), 2 ) + Math.pow( point2.y() - point1.y(), 2 ) );
    }

}
