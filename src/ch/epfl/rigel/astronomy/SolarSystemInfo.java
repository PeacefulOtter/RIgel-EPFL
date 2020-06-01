package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.gui.Card;
import ch.epfl.rigel.math.ClosedInterval;

import java.util.HashMap;
import java.util.Map;

public enum SolarSystemInfo
{
    SUN(     "Soleil",  "" ,"Étoile",  5505, 149.6, 696340, 1.989, 6.0877 ),
    MOON(    "Lune",    "", "Satellite", 120 , -180, 384400, 1737,  1.62, 38,4.53 ),
    MERCURY( "Mercure", "", "Planète tellurique", 427,  -173, 91,  2440,  3.7,  75 ),
    VENUS(   "Vénus",   "", "Planète tellurique", 490,   446, 42,  6052,  8.9,  460 ),
    MARS(    "Mars",    "", "Planète tellurique", 36,   -140, 78,  3389,  3.7,  145 ),
    JUPITER( "Jupiter", "", "Planète gazeuse",   -118,  -129, 628, 69911, 24.8, 61420 ),
    SATURN(  "Saturn",  "", "Planète gazeuse",   -139,  -201, 1275,58232, 10.4 ,42700 ),
    URANUS(  "Uranus",  "", "Planète gazeuse",   -208 , -212 ,2723,25362 ,8.9 , 8083 ),
    NEPTUNE( "Neptune", "", "Planète gazeuse",   -210,  -220, 4351,24622 ,11.2 ,7618 );

    private final Card.Builder builder;

    public final Map<String, Card> solarSystemCardsMap = new HashMap<>();

    SolarSystemInfo( String name, String imgPath, String type, int cardHeight )
    {
        builder = new Card.Builder( cardHeight )
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
        this( name, imgPath, type, 410 );
        builder.addLabel( "Temperature " + degrees + "°C" )
                .addLabel( "Earth distance " + earthDistance + " x10^6 km" )
                .addLabel( "Radius : " + radius + " km")
                .addLabel( "Mass : " + mass + " x10^30 kg" )
                .addLabel( "Surface : " + surface + " x10^12 km2" );
        solarSystemCardsMap.put( name, builder.build() );
    }

    // MOON Constructor
    SolarSystemInfo( String name,
                     String imgPath,
                     String type,
                    double maxDegrees,
                    double minDegrees,
                    double earthDistance,
                    double radius,
                    double gravity,
                    double surface,
                    double age )
    {
        this( name, imgPath, type, 430 );
        builder.addLabel( "Temperatures " + ClosedInterval.of( minDegrees, maxDegrees ).toString() + "°C" )
                .addLabel( "Earth distance " + earthDistance + " x10^6 km"  )
                .addLabel( "Radius : " + radius + " km" )
                .addLabel( "Gravity : " + gravity + " m/s²" )
                .addLabel( "Surface : " + surface + " x10^6 km2" )
                .addLabel( "Age : " + age + " billion years" );
        solarSystemCardsMap.put( name, builder.build() );
    }

    // PLANETS Constructor
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
        this( name, imgPath, type, 450 );
        builder.addLabel( "Temperatures " + ClosedInterval.of( minDegrees, maxDegrees ).toString() + "°C" )
                .addLabel( "Earth distance " + earthDistance + "x10^6 km" )
                .addLabel( "Radius : " + radius + " km" )
                .addLabel( "Gravity : " + gravity + " m/s²" )
                .addLabel( "Surface : " + surface + " x10^6 km2" );
        solarSystemCardsMap.put( name, builder.build() );
    }
}
