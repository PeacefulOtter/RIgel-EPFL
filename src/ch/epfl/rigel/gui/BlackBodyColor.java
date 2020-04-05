package ch.epfl.rigel.gui;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.scene.paint.Color;

import java.io.*;

public final class BlackBodyColor
{
    private static final String COLOR_FILE_NAME = "/bbr_color.txt";
    private static final ClosedInterval KELVIN_INTERVAL = ClosedInterval.of( 1000, 40000 );

    public static Color colorForTemperature( int kelvin )
    {
        Preconditions.checkInInterval( KELVIN_INTERVAL, kelvin );

        String line;
        String[] lineSplit;
        String hexColor = "";

        try ( BufferedReader stream = new BufferedReader( new InputStreamReader( BlackBodyColor.class.getResourceAsStream( COLOR_FILE_NAME ) ) ) )
        {
            while ( ( line = stream.readLine() ) != null )
            {
                lineSplit = line.split( "\\s+" );

                if ( lineSplit[ 0 ].equals( "#" ) || lineSplit[ 3 ].equals( "2deg" ) ) continue;

                if ( Integer.parseInt( lineSplit[ 1 ] ) == kelvin )
                {
                    hexColor = lineSplit[ 13 ];
                    break;
                }
            }
        }
        catch( IOException e )
        {
            throw new UncheckedIOException( e );
        }

        return Color.web( hexColor );
    }
}
