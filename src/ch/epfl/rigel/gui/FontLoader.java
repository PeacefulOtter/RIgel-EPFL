package ch.epfl.rigel.gui;

import javafx.scene.text.Font;

import java.io.IOException;
import java.io.InputStream;


public class FontLoader
{
    // an input stream to read the files
    private static final String FONT_AWESOME_FILE_NAME = "/Font Awesome 5 Free-Solid-900.otf";

    private InputStream resourceStream( String fontPath ) { return getClass().getResourceAsStream( fontPath ); }

    private Font loadFont( String fontPath )
    {
        // load the 'Font Awesome' font or assign the button's text to a backup value
        try ( InputStream fontStream = resourceStream( fontPath ) )
        {
            return Font.loadFont( fontStream, 15 );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            return null;
        }
    }

    public Font loadFontAwesome() { return loadFont( FONT_AWESOME_FILE_NAME ); }

    // public Font loadOtherFont() { return loadFont( OTHER_FONT_FILE_NAME ); }
}
