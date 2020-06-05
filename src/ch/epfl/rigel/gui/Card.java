package ch.epfl.rigel.gui;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Represents a panel with an image, a title and labels
 */
public class Card extends VBox
{
    private static final Font FONT_AWESOME = new FontLoader().loadFontAwesome();
    private static final int WRAPPER_WIDTH = 250;

    private final ImageView imgView;
    private final VBox container;

    public Card( int wrapperHeight )
    {
        // set some styles to the card
        this.setPrefWidth( WRAPPER_WIDTH );
        this.setPrefHeight( wrapperHeight );
        this.setStyle(  "-fx-spacing: 4;" +
                        "-fx-padding: 5px;" +
                        "-fx-border-insets: 5px;" +
                        "-fx-background-insets: 5px;" +
                        "-fx-background-radius: 5px;" +
                        "-fx-border-radius: 20px;" +
                        "-fx-background-color: #ffffff;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);" );
        // creates the image view to put the image in it
        imgView = new ImageView();
        imgView.setX( 0 );
        imgView.setY( 0 );
        imgView.setPreserveRatio( true );
        // the container contains the title and labels
        container = new VBox();
        container.setStyle( "-fx-padding: 10;" );
        // add the ImageView and then the container to the card
        this.getChildren().addAll( imgView, container );
        container.getChildren().add( new Label() );
    }

    /**
     * Sets an image to the card
     * @param imgPath : the image path
     * @return the card's instance
     */
    public Card setImage( String imgPath )
    {
        Image img = new Image( "/img/" + imgPath );
        imgView.setFitWidth( WRAPPER_WIDTH );
        imgView.setFitHeight( WRAPPER_WIDTH );
        imgView.setImage( img );
        return this;
    }

    /**
     * Sets a title to the card
     * @param title : the String title
     * @return the card's instance
     */
    public Card setTitle( String title )
    {
        Label label = (Label) container.getChildren().get( 0 );
        label.setText( title );
        label.setStyle( "-fx-spacing: 4; -fx-padding: 4; -fx-font-size: 13pt;" );
        label.setFont( FONT_AWESOME );
        container.getChildren().set( 0, label );
        return this;
    }

    /**
     * Adds a label at the end of the container
     * @param text : the String text
     * @return the card's instance
     */
    public Card addLabel( String text )
    {
        Label label = new Label( text );
        label.setStyle( "-fx-spacing: 4; -fx-padding: 4; -fx-font-size: 8pt;" );
        label.setFont( FONT_AWESOME );
        container.getChildren().add( label );
        return this;
    }
}