package ch.epfl.rigel.gui;

public class Card {
    private String image;
    private String title;
    private String field;

    public Card( String image, String title, String field ) {
        this.image = image;
        this.title = title;
        this.field = field;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
