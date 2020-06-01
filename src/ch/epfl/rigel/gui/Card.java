package ch.epfl.rigel.gui;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class Card
{
    private VBox card;

    public Card( VBox card )
    {
        this.card = card;
    }

    public VBox getCard() { return card; }


    public static class Builder
    {
        private static final int WRAPPER_WIDTH = 150;
        private final VBox wrapper;

        public Builder( int wrapperHeight )
        {
            wrapper = new VBox();
            wrapper.setPrefWidth( WRAPPER_WIDTH );
            wrapper.setPrefHeight( wrapperHeight );
            wrapper.setStyle(
                    "-fx-spacing: 4;" +
                    "-fx-padding: 10;" +
                    "-fx-background-radius: 50px;" +
                    "-fx-border-radius: 15px;" +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);" );
        }


        public Builder setImage( String imgPath )
        {
            Image img = new Image( imgPath );
            ImageView imgView = new ImageView( img );
            imgView.setX( 0 );
            imgView.setY( 0 );
            imgView.setFitWidth( WRAPPER_WIDTH );
            imgView.setFitHeight( WRAPPER_WIDTH );
            wrapper.getChildren().set( 0, imgView );
            return this;
        }

        public Builder setTitle( String title )
        {
            Label label = new Label( title );
            label.setStyle( "-fx-spacing: 4; -fx-padding: 4; -fx-font-size: 12pt;" );
            wrapper.getChildren().set( 1, label );
            return this;
        }

        public Builder addLabel( String text )
        {
            Label label = new Label( text );
            label.setStyle( "-fx-spacing: 4; -fx-padding: 4; -fx-font-size: 8pt;" );
            wrapper.getChildren().add( label );
            return this;
        }

        public Card build()
        {
            return new Card( wrapper );
        }

    }
}
