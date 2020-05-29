package ch.epfl.rigel.gui;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class used to get the color of a star using it's temperature in kelvin.
 * This class used to be static but we made it instantiable so that we only read the bbr_color file
 * one time and not for every stars which made the program much slower.
 */
public class BlackBodyColor
{
    // file name where all the temperatures and colors are stored
    private static final String COLOR_FILE_NAME = "/bbr_color.txt";
    // interval of the temperatures in kelvin
    private static final ClosedInterval KELVIN_INTERVAL = ClosedInterval.of( 1000, 40000 );
    // Associate all temperature to its corresponding color
    private final Map<Integer, Color> colorMap = new HashMap<>();
    // column index we want to retrieve in the file
    private static final int KELVIN_INDEX = 1;
    private static final int RGB_INDEX = 13;


    // an input stream to read the files
    private InputStream resourceStream()
    {
        return getClass().getResourceAsStream( COLOR_FILE_NAME );
    }

    // initialize the map
    public BlackBodyColor() { initColorMap(); }

    /**
     * Retrieves the temperatures and colors, and save them into the colorMap map
     */
    private void initColorMap()
    {
        String line;
        String[] lineSplit;

        // read the file in a BufferedReader to be able to read the file line by line
        try ( BufferedReader stream = new BufferedReader( new InputStreamReader( resourceStream() ) ) )
        {
            while ( ( line = stream.readLine() ) != null )
            {
                lineSplit = line.split( "\\s+" );
                // skip the lines that do not interest us
                if ( lineSplit[ 0 ].equals( "#" ) || lineSplit[ 3 ].equals( "2deg" ) ) continue;

                colorMap.put( Integer.parseInt( lineSplit[ KELVIN_INDEX ] ), Color.web( lineSplit[ RGB_INDEX ] ) );
            }
        }
        catch( IOException e )
        {
            throw new UncheckedIOException( e );
        }
    }

    /**
     * Get the color corresponding to a temperature
     * @param kelvin : temperature in kelvin
     * @return the colour of a black body given its temperature.
     */
    public Color colorForTemperature( int kelvin )
    {
        Preconditions.checkInInterval( KELVIN_INTERVAL, kelvin );
        // round to the nearest 500
        int roundedTemperature = ( ( ( kelvin + 499 ) / 500 ) * 500 );
        return colorMap.get( roundedTemperature );
    }
}
