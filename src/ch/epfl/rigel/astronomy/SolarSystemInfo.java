package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.gui.Card;
import ch.epfl.rigel.math.ClosedInterval;

public enum SolarSystemInfo
{
    SUN(     "Soleil",  "sun.jpg" ,   "Étoile", 5505, 149.6, 696340, 1.989, 6.0877 ),
    MOON(    "Lune",    "moon.jpg",   "Satellite", 120 , -180, 384400, 1737,  1.62, 38 ),
    MERCURY( "Mercure", "mercury.jpg","Planète tellurique", 427,  -173, 91,  2440,  3.7,  75 ),
    VENUS(   "Vénus",   "venus.jpg",  "Planète tellurique", 490,   446, 42,  6052,  8.9,  460 ),
    MARS(    "Mars",    "mars.jpg",   "Planète tellurique", 36,   -140, 78,  3389,  3.7,  145 ),
    JUPITER( "Jupiter", "jupiter.jpg","Planète gazeuse",   -118,  -129, 628, 69911, 24.8, 61420 ),
    SATURN(  "Saturne", "saturn.jpg", "Planète gazeuse",   -139,  -201, 1275,58232, 10.4 ,42700 ),
    URANUS(  "Uranus",  "uranus.jpg", "Planète gazeuse",   -208 , -212 ,2723,25362 ,8.9 , 8083 ),
    NEPTUNE( "Neptune", "neptune.jpg","Planète gazeuse",   -210,  -220, 4351,24622 ,11.2 ,7618 );

    private final Card card;
    private final String name;

    /**
     * Create a Card for each.
     * @param name : the name of the Celestial Object
     * @param imgPath : an image of the Celestial Object
     * @param type : the type of Celestial Object
     * @param cardHeight : the cardHeight
     */

    SolarSystemInfo( String name, String imgPath, String type, int cardHeight )
    {
        this.name = name;
        card = new Card( cardHeight )
                .setImage( imgPath )
                .setTitle( name )
                .addLabel( "Type : " + type );
    }

    // SUN Constructor
    SolarSystemInfo( String name,
                     String imgPath,
                     String type,
                     double degrees,
                     double earthDistance,
                     double radius,
                     double mass,
                     double surface )
    {
        this( name, imgPath, type, 460 );
        // create the Sun Card
        card.addLabel( "Temperature : " + degrees + "°C" )
                .addLabel( "Earth distance : " + earthDistance + " x10^6 km" )
                .addLabel( "Rayon : " + radius + " km")
                .addLabel( "Mass : " + mass + " x10^30 kg" )
                .addLabel( "Surface : " + surface + " x10^12 km2" );
    }

    // PLANETS and MOON Constructor
    SolarSystemInfo( String name,
                     String imgPath,
                     String type,
                     double maxDegrees,
                     double minDegrees,
                     double earthDistance,
                     double radius,
                     double gravity,
                     double surface )
    {
        this( name, imgPath, type, 460 );
        // Create the Card for each planet and Moon
        card.addLabel( "Temperatures : " + ClosedInterval.of( minDegrees, maxDegrees ).toString() + "°C" )
                .addLabel( "Earth distance : " + earthDistance + "x10^6 km" )
                .addLabel( "Rayon : " + radius + " km" )
                .addLabel( "Gravity : " + gravity + " m/s²" )
                .addLabel( "Surface : " + surface + " x10^6 km2" );
    }

    public String getName() { return name; }
    public Card getCard()   { return card; }
}