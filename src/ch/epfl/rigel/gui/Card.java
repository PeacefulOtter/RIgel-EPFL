package ch.epfl.rigel.gui;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


public class Card extends VBox
{
    private static final int WRAPPER_WIDTH = 250;
    private final VBox container;

    public Card( int wrapperHeight )
    {
        this.setPrefWidth( WRAPPER_WIDTH );
        this.setPrefHeight( wrapperHeight );
        this.setStyle(  "-fx-spacing: 4;" +
                        "-fx-background-radius: 5px;" +
                        "-fx-border-radius: 20px;" +
                        "-fx-background-color: #ffffff;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);" );
        container = new VBox();
        container.setStyle( "-fx-padding: 10;" );
    }



    public Card setImage( String imgPath )
    {
        Image img = new Image( "/img/" + imgPath );
        ImageView imgView = new ImageView( img );
        imgView.setX( 0 );
        imgView.setY( 0 );
        imgView.setFitWidth( WRAPPER_WIDTH );
        imgView.setFitHeight( WRAPPER_WIDTH );
        this.getChildren().add( imgView );
        this.getChildren().add( container );
        return this;
    }

    public Card setTitle( String title )
    {
        Label label = new Label( title );
        label.setStyle( "-fx-spacing: 4; -fx-padding: 4; -fx-font-size: 13pt;" );
        container.getChildren().add( label );
        return this;
    }

    public Card addLabel( String text )
    {
        Label label = new Label( text );
        label.setStyle( "-fx-spacing: 4; -fx-padding: 4; -fx-font-size: 8pt;" );
        container.getChildren().add( label );
        return this;
    }
}