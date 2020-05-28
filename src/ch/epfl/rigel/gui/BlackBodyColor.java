package ch.epfl.rigel.gui;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

public class BlackBodyColor
{
    private static final String COLOR_FILE_NAME = "/bbr_color.txt";
    private static final ClosedInterval KELVIN_INTERVAL = ClosedInterval.of( 1000, 40000 );
    private final Map<Integer, Color> colorMap = new HashMap<>();

    /**
     * link a color to a temperature in kelvin
     */
    public BlackBodyColor() { initColorMap(); }

    private void initColorMap()
    {
        String line;
        String[] lineSplit;

        try ( BufferedReader stream = new BufferedReader( new InputStreamReader( BlackBodyColor.class.getResourceAsStream( COLOR_FILE_NAME ) ) ) )
        {
            while ( ( line = stream.readLine() ) != null )
            {
                lineSplit = line.split( "\\s+" );

                if ( lineSplit[ 0 ].equals( "#" ) || lineSplit[ 3 ].equals( "2deg" ) ) continue;

                colorMap.put( Integer.parseInt( lineSplit[ 1 ] ), Color.web( lineSplit[ 13 ] ) );
            }
        }
        catch( IOException e )
        {
            throw new UncheckedIOException( e );
        }
    }

    /**
     *
     * @param kelvin : temperature in kelvin
     * @return the colour of a black body given its temperature.
     */
    public Color colorForTemperature( int kelvin )
    {
        Preconditions.checkInInterval( KELVIN_INTERVAL, kelvin );
        return colorMap.get( kelvin );
    }
}
