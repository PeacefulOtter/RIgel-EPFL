package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.UnaryOperator;



public class Main extends Application
{
    private static final String RESET_NAME = "\uf0e2";
    private static final String BACKUP_RESET_NAME = "R";
    private static final String PLAY_NAME = "\uf04b";
    private static final String BACKUP_PLAY_NAME = "▶";
    private static final String PAUSE_NAME = "\uf04c";
    private static final String BACKUP_PAUSE_NAME = "\u23F8";
    private static final NumberStringConverter TWO_DECIMAL_CONVERTER = new NumberStringConverter("#0.00" );
    private static final double INIT_POS_LON = 6.57;
    private static final double INIT_POS_LAT = 46.52;

    private Canvas sky;
    private LocalTime startTime;
    private TimeAnimator timeAnimator;
    private DateTimeBean dateTimeBean;

    private boolean loadedFont = false;

    private InputStream resourceStream( String resourceName )
    {
        return getClass().getResourceAsStream( resourceName );
    }

    public static void main( String[] args )
    {
        launch( args );
    }

    @Override
    public void start( Stage primaryStage ) throws Exception
    {
        startTime = LocalTime.now();
        dateTimeBean = new DateTimeBean();
        dateTimeBean.setZonedDateTime( ZonedDateTime.now() );
        timeAnimator = new TimeAnimator( dateTimeBean );
        TimeAccelerator accelerator = NamedTimeAccelerator.TIMES_3000.getAccelerator();
        timeAnimator.setAccelerator( accelerator );

        BorderPane wrapper = new BorderPane();
        wrapper.setMinWidth( 800 );
        wrapper.setMinHeight( 600 );
        /*HBox topTab = buildTopTab();  TRY TO MAKE IT RESPONSIVE  - not *needed*
        primaryStage.widthProperty().addListener( (o, oV, nV) -> {
            topTab.setMinWidth( nV.doubleValue() );
        } );*/
        wrapper.setTop( buildTopTab() );
        wrapper.setCenter( buildSky() );
        wrapper.setBottom( buildBottomTab() );

        sky.widthProperty().bind( wrapper.widthProperty() );
        sky.heightProperty().bind( wrapper.heightProperty() );

        primaryStage.setMinWidth( 800 );
        primaryStage.setMinHeight( 600 );

        primaryStage.setY( 100 );

        primaryStage.setScene( new Scene( wrapper ) );
        primaryStage.show();

        sky.requestFocus();
    }

    //      I) TOP TAB
    // TODO : BIND lonTextFormatter.valueProperty()
    // TODO : BIND latTextFormatter.valueProperty()
    // TODO : BIND datePicker.valueProperty()
    // TODO : BIND timeFormatter.valueProperty()
    // TODO : BIND timezoneBox.valueProperty();
    // TODO : CHANGE TYPE ZoneId.getAvailableZoneIds() to a ObservableList somehow
    // TODO : REORDER  ZoneId.getAvailableZoneIds() by alphabetic order
    // TODO : CHANGE TEXT when isRunning changes, problem -> lambda only accepts final variables
    private HBox buildTopTab()
    {
        HBox topTab = new HBox();
        topTab.setStyle( "-fx-spacing: 4; -fx-padding: 4;" );


        // LEFT PART
        HBox posBox = new HBox();
        posBox.setStyle( "-fx-spacing: inherit; -fx-alignment: baseline-left;" );

        // Longitude label and field
        Label posLongitudeLabel = new Label( "Longitude (°) :" );
        TextField posLongitudeField = new TextField();
        posLongitudeField.setStyle( "-fx-pref-width: 60; -fx-alignment: baseline-right;" );
        // Longitude Formatter
        UnaryOperator<TextFormatter.Change> lonFilter = ( change -> {
            try
            {
                String newText = change.getControlNewText();
                double newLonDeg = TWO_DECIMAL_CONVERTER.fromString( newText ).doubleValue();
                return GeographicCoordinates.isValidLonDeg( newLonDeg ) ? change : null;
            }
            catch ( Exception e ) { return null; }
        } );
        TextFormatter<Number> lonTextFormatter = new TextFormatter<>( TWO_DECIMAL_CONVERTER, INIT_POS_LON, lonFilter );
        posLongitudeField.setTextFormatter( lonTextFormatter );

        // Latitude label and field
        Label posLatitudeLabel = new Label( "Latitude (°) :" );
        TextField posLatitudeField = new TextField();
        posLatitudeField.setStyle( "-fx-pref-width: 60; -fx-alignment: baseline-right;" );
        // Latitude Formatter
        UnaryOperator<TextFormatter.Change> latFilter = ( change -> {
            try
            {
                String newText = change.getControlNewText();
                double newLatDeg = TWO_DECIMAL_CONVERTER.fromString( newText ).doubleValue();
                return GeographicCoordinates.isValidLatDeg( newLatDeg ) ? change : null;
            }
            catch ( Exception e ) { return null; }
        } );
        TextFormatter<Number> latTextFormatter = new TextFormatter<>( TWO_DECIMAL_CONVERTER, INIT_POS_LAT, latFilter );
        posLatitudeField.setTextFormatter( latTextFormatter );

        posBox.getChildren().addAll( posLongitudeLabel, posLongitudeField, posLatitudeLabel, posLatitudeField );


        // MIDDLE PART Date Hour TimeZone
        HBox timeDetailsBox = new HBox();
        timeDetailsBox.setStyle( "-fx-spacing: inherit; -fx-alignment: baseline-left;" );

        // Date
        Label dateLabel = new Label( "Date :" );
        DatePicker datePicker = new DatePicker();
        datePicker.setStyle( "-fx-pref-width: 120;" );
        datePicker.setValue( dateTimeBean.getDate() );

        // Time
        Label hourLabel = new Label( "Heure :" );
        TextField hourField = new TextField();
        hourField.setStyle( "-fx-pref-width: 75; -fx-alignment: baseline-right;" );
        // Hour Formatter
        DateTimeFormatter hmsFormatter = DateTimeFormatter.ofPattern( "HH:mm:ss" );
        LocalTimeStringConverter stringConverter = new LocalTimeStringConverter( hmsFormatter, hmsFormatter );
        TextFormatter<LocalTime> timeFormatter = new TextFormatter<>( stringConverter, startTime );
        hourField.setTextFormatter( timeFormatter );
        hourField.setText( dateTimeBean.getTime().toString() );

        // Time Zone
        ComboBox timezoneBox = new ComboBox();
        timezoneBox.setStyle( "-fx-pref-width: 180;" );
        ObservableList zoneIds = FXCollections.observableList( List.of( ZoneId.getAvailableZoneIds().toArray() ) );
        timezoneBox.setItems( zoneIds );
        timezoneBox.setValue( ZoneId.systemDefault() );
        timeDetailsBox.getChildren().addAll( dateLabel, datePicker, hourLabel, hourField, timezoneBox );


        // RIGHT PART
        HBox timeManagerBox = new HBox();
        timeManagerBox.setStyle( "-fx-spacing: inherit; -fx-alignment: baseline-right;" );

        ChoiceBox acceleratorChoiceBox = new ChoiceBox();
        acceleratorChoiceBox.setItems( FXCollections.observableList( NamedTimeAccelerator.ACCELERATOR_NAMES ) );
        acceleratorChoiceBox.setValue( NamedTimeAccelerator.TIMES_300.getName() );

        /*
        String to Accelerator Binding example
        ObjectProperty<NamedTimeAccelerator> p1 = new SimpleObjectProperty<>( NamedTimeAccelerator.TIMES_1 );
        ObjectProperty<String> p2 = new SimpleObjectProperty<>();

        p2.addListener((p, o, n) -> {
            System.out.printf("old: %s  new: %s%n", o, n);
        });

        p2.bind( Bindings.select( p1, "name" ) );
        p1.set( NamedTimeAccelerator.TIMES_300 );*/

        // Buttons : refresh, play/pause
        Button resetButton, playPauseButton;
        // load font or assign the button's text to a backup value
        try ( InputStream fontStream = resourceStream( "/Font Awesome 5 Free-Solid-900.otf" ) )
        {
            Font fontAwesome = Font.loadFont( fontStream, 15 );
            resetButton = new Button( RESET_NAME );
            resetButton.setFont( fontAwesome );
            playPauseButton = new Button( PLAY_NAME );
            playPauseButton.setFont( fontAwesome );
            loadedFont = true;
        }
        catch ( IOException e )
        {
            resetButton = new Button( BACKUP_RESET_NAME );
            playPauseButton = new Button( BACKUP_PLAY_NAME );
        }

        // play pause logic
        playPauseButton.setOnMouseClicked( mouseEvent -> {
            if ( mouseEvent.isPrimaryButtonDown() )
            {
                if ( timeAnimator.isRunning().get() )
                {
                    timeAnimator.stop();
                }
                else
                {
                    timeAnimator.start();
                }
            }
        } );

        timeManagerBox.getChildren().addAll( acceleratorChoiceBox, resetButton, playPauseButton );

        // disable all inputs if the animation is running
        timeAnimator.isRunning().addListener( ( o, oV, nV ) -> {
            posLongitudeField.setDisable( nV );
            posLatitudeField.setDisable( nV );
            datePicker.setDisable( nV );
            hourField.setDisable( nV );
            timezoneBox.setDisable( nV );
        } );

        topTab.getChildren().addAll(
                posBox,
                new Separator( Orientation.VERTICAL ),
                timeDetailsBox,
                new Separator( Orientation.VERTICAL ),
                timeManagerBox );
        return topTab;
    }

    //      II) MIDDLE VIEW
    // TODO : BIND Canvas dimensions to BorderPane dimensions
    private Pane buildSky()
    {
        try ( InputStream hs = resourceStream( "/hygdata_v3.csv" ) )
        {
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom( hs, HygDatabaseLoader.INSTANCE )
                    .build();

            ObserverLocationBean observerLocationBean = new ObserverLocationBean();
            observerLocationBean.setCoordinates( GeographicCoordinates.ofDeg( INIT_POS_LON, INIT_POS_LAT ) );

            ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
            viewingParametersBean.setCenter( HorizontalCoordinates.ofDeg( 180.000000000001, 15 ) );
            viewingParametersBean.setFieldOfViewDeg( 100 );

            SkyCanvasManager canvasManager = new SkyCanvasManager(
                    catalogue, dateTimeBean,
                    observerLocationBean, viewingParametersBean );

            canvasManager.objectUnderMouseProperty().addListener( ( p, o, n ) ->
            {
                if ( n != null ) System.out.println( n );
            } );

            sky = canvasManager.canvas();
            return new Pane( sky );
        } catch ( IOException e )
        {
            return new Pane();
        }
    }


    //      III) BOTTOM TAB
    private BorderPane buildBottomTab()
    {
        BorderPane info = new BorderPane();
        info.setStyle( "-fx-padding: 4;\n" +
                "-fx-background-color: white;" );
        Text left = new Text();
        left.setText(String.format("Champ de vue : <fov>°"));
        info.setLeft(left);

        Text center = new Text();
        center.setText("celestial object");
        info.setCenter(center);

        Text right = new Text();
        right.setText("Azimut : <az>°, hauteur : <alt>°");
        info.setRight(right);
      // FOV ,0°

        // star name and info OR empty

        // horizontal mouse pos ,00°
        return info;
    }
}
