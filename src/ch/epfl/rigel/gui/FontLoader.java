package ch.epfl.rigel.gui;

import javafx.scene.text.Font;

import java.io.IOException;
import java.io.InputStream;

/**
 * Represents a Font loader, that can be used by any class in the program
 */
public class FontLoader
{
    // Font paths
    private static final String FONT_AWESOME_FILE_NAME = "/Font Awesome 5 Free-Solid-900.otf";

    // an input stream to read the files
    private InputStream resourceStream( String fontPath ) { return getClass().getResourceAsStream( fontPath ); }

    /**
     * Read the font file and returns the Font
     * @param fontPath : the font path
     * @return Font or null
     */
    private Font loadFont( String fontPath )
    {
        // load the font or assign the button's text to a backup value
        try ( InputStream fontStream = resourceStream( fontPath ) )
        {
            return Font.loadFont( fontStream, 15 );
        }
        catch ( IOException e )
        {
            return null;
        }
    }

    // public methods to load each font individually
    public Font loadFontAwesome() { return loadFont( FONT_AWESOME_FILE_NAME ); }

    // public Font loadOtherFont() { return loadFont( OTHER_FONT_FILE_NAME ); }
    // ....
}
