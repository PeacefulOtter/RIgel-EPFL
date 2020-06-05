package ch.epfl.rigel.gui;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * Represents a modified Box that can contain a logo, and a title
 */
public class NotificationBox extends HBox
{
    // Transitions used to make it appear/disappear with style !
    private static final Duration TRANSITION_DURATION = Duration.millis( 2000 );
    private static final FadeTransition FADE_IN_TRANSITION = new FadeTransition( TRANSITION_DURATION );
    private static final FadeTransition FADE_OUT_TRANSITION = new FadeTransition( TRANSITION_DURATION );

    private static final Font FONT_AWESOME = new FontLoader().loadFontAwesome();

    private static final String SUCCESS_IMG_PATH = "success.png";
    private static final String ERROR_IMG_PATH = "error.png";

    private static final double LETTER_WIDTH = 9;
    private static final double NOTIFICATION_HEIGHT = 30;

    private final ImageView imgView;
    private Pane parentElt;
    private int notificationWidth = 0;

    NotificationBox()
    {
        // apply some styles to the box
        this.setPrefHeight( NOTIFICATION_HEIGHT );
        this.setStyle(  "-fx-spacing: 4;" +
                "-fx-padding: 5px;" +
                "-fx-border-insets: 5px;" +
                "-fx-background-insets: 5px;" +
                "-fx-background-radius: 5px;" +
                "-fx-border-radius: 5px;" +
                "-fx-background-color: #ffffff;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);" );
        // define some values for the transitions
        FADE_IN_TRANSITION.setNode( this );
        FADE_OUT_TRANSITION.setNode( this );
        FADE_IN_TRANSITION.setFromValue( 0 );
        FADE_IN_TRANSITION.setToValue( 1 );
        FADE_OUT_TRANSITION.setFromValue( 1 );
        FADE_OUT_TRANSITION.setToValue( 0 );

        // when the fade-in transition is finished, launch the fade-out transition
        FADE_IN_TRANSITION.setOnFinished( event ->
                FADE_OUT_TRANSITION.play()
        );

        // when the fade-out transition is finished, remove the element from the parent element
        FADE_OUT_TRANSITION.setOnFinished( event ->
                parentElt.getChildren().remove( this )
        );

        // creates the ImageView used for the logo
        imgView = new ImageView();
        imgView.setX( 0 );
        imgView.setY( 0 );
        imgView.setFitWidth( NOTIFICATION_HEIGHT );
        imgView.setFitHeight( NOTIFICATION_HEIGHT );
        imgView.setStyle(  "-fx-spacing: 4; -fx-padding: 3px;" );
        imgView.setPreserveRatio( true );
    }

    /**
     * Sets the logo of the NotificationBox
     * @param imgPath : the image path
     * @return the NotificationBox instance
     */
    private NotificationBox setLogo( String imgPath )
    {
        if ( FONT_AWESOME != null )
        {
            Image img = new Image( "/img/" + imgPath );
            imgView.setImage( img );
            this.getChildren().add( imgView );
            notificationWidth += (int) NOTIFICATION_HEIGHT;
        }
        return this;
    }

    /**
     * public methods to make it easier to set a logo (rather than entering each time the image path)
     * @return the NotificationBox instance
     */
    public NotificationBox setSuccessLogo() { return setLogo( SUCCESS_IMG_PATH ); }
    public NotificationBox setErrorLogo()   { return setLogo( ERROR_IMG_PATH ); }

    /**
     * Sets the title of the NotificationBox
     * @param title : the String title
     * @return the NotificationBox instance
     */
    public NotificationBox setTitle( String title )
    {
        Label text = new Label( title );
        if ( FONT_AWESOME != null ) { text.setFont( FONT_AWESOME ); }
        text.setAlignment( Pos.BASELINE_CENTER );
        text.setStyle( "-fx-spacing: 4; -fx-padding: 4; -fx-font-size: 13pt;" );
        this.getChildren().add( text );
        notificationWidth += LETTER_WIDTH * title.length();
        return this;
    }

    /**
     * Sets the parent of the NotificationBox
     * @param parent : the parent element the NotificationBox will be drawn to
     * @return the NotificationBox instance
     */
    public NotificationBox setParentElement( Pane parent )
    {
        parentElt = parent;
        return this;
    }

    /**
     * triggers the fade-in transitions to display the NotificationBox
     */
    public void fire()
    {
        // a parent element is required
        if ( parentElt == null ) { throw new NullPointerException( "No Parent Element has been set" ); }
        this.setPrefWidth( notificationWidth );

        double parentWidth = parentElt.getWidth();
        // place the box at the top-right corner of the parent element
        this.setLayoutX( parentWidth - notificationWidth );
        parentElt.getChildren().add( this );

        FADE_IN_TRANSITION.play();
    }
}