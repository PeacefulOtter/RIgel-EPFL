package ch.epfl.rigel.gui;

import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class NotificationBox extends HBox
{
    private static final Duration TRANSLATION_DURATION = Duration.millis( 1500 );
    private static final TranslateTransition TRANSLATE_IN_TRANSITION = new TranslateTransition( TRANSLATION_DURATION );
    private static final TranslateTransition TRANSLATE_OUT_TRANSITION = new TranslateTransition( TRANSLATION_DURATION );

    private static final Font FONT_AWESOME = new FontLoader().loadFontAwesome();

    private final ImageView imgView;
    private static final String SUCCESS_IMG_PATH = "success.png";
    private static final String ERROR_IMG_PATH = "error.png";

    private static final double LOGO_WIDTH = 21;
    private static final double LETTER_WIDTH = 9;
    private static final double NOTIFICATION_HEIGHT = 30;

    private Pane parentElt;
    private int notificationWidth;

    NotificationBox()
    {
        this.setPrefHeight( NOTIFICATION_HEIGHT );
        this.setStyle(  "-fx-spacing: 4;" +
                "-fx-padding: 5px;" +
                "-fx-border-insets: 5px;" +
                "-fx-background-insets: 5px;" +
                "-fx-background-radius: 5px;" +
                "-fx-border-radius: 5px;" +
                "-fx-background-color: #ffffff;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);" );
        TRANSLATE_IN_TRANSITION.setNode( this );
        TRANSLATE_OUT_TRANSITION.setNode( this );

        imgView = new ImageView();
        imgView.setX( 0 );
        imgView.setY( 0 );
        imgView.setFitWidth( NOTIFICATION_HEIGHT );
        imgView.setFitHeight( NOTIFICATION_HEIGHT );
        imgView.setStyle(  "-fx-spacing: 4; -fx-padding: 3px;" );
        imgView.setPreserveRatio( true );
    }

    private NotificationBox setLogo( String imgPath )
    {
        if ( FONT_AWESOME != null )
        {
            Image img = new Image( "/img/" + imgPath );
            imgView.setImage( img );
            this.getChildren().add( imgView );
            notificationWidth += NOTIFICATION_HEIGHT;
        }
        return this;
    }

    public NotificationBox setSuccessLogo() { return setLogo( SUCCESS_IMG_PATH ); }
    public NotificationBox setErrorLogo()   { return setLogo( ERROR_IMG_PATH ); }

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

    public NotificationBox setParentElement( Pane parent )
    {
        parentElt = parent;
        return this;
    }

    public void fire()
    {
        this.setPrefWidth( notificationWidth );
        parentElt.getChildren().add( this );

        double parentWidth = parentElt.getWidth();
        TRANSLATE_IN_TRANSITION.setFromX( parentWidth );
        TRANSLATE_IN_TRANSITION.setToX( parentWidth - notificationWidth );
        TRANSLATE_OUT_TRANSITION.setFromX( parentWidth - notificationWidth );
        TRANSLATE_OUT_TRANSITION.setToX( parentWidth );

        TRANSLATE_IN_TRANSITION.play();
        TRANSLATE_IN_TRANSITION.setOnFinished(  event ->
            TRANSLATE_OUT_TRANSITION.play()
        );
        TRANSLATE_OUT_TRANSITION.setOnFinished( event ->
            parentElt.getChildren().remove( this )
        );
    }
}
