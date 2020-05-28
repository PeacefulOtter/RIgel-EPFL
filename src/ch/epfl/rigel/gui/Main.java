package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
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
import java.util.*;
import java.util.function.UnaryOperator;



public class Main extends Application
{
    private static final String HYG_CATALOGUE_NAME = "/hygdata_v3.csv";
    private static final String ASTERISM_CATALOGUE_NAME = "/asterisms.txt";

    // Buttons text and backup text if the font cant load
    private static final String RESET_BTN_TEXT = "\uf0e2";
    private static final String BACKUP_RESET_BTN_TEXT = "R";
    private static final String PLAY_BTN_TEXT = "\uf04b";
    private static final String BACKUP_PLAY_BTN_TEXT = "▶";
    private static final String PAUSE_BTN_TEXT = "\uf04c";
    private static final String BACKUP_PAUSE_BTN_TEXT = "\u23F8";

    private static final NumberStringConverter TWO_DECIMAL_CONVERTER = new NumberStringConverter("#0.00" );

    private static final double INIT_OBSERVER_LON = 6.57;
    private static final double INIT_OBSERVER_LAT = 46.52;
    private static final double INIT_VIEWING_LON = 180.000000000001;
    private static final double INIT_VIEWING_LAT = 15;
    private static final double INIT_FOV_VALUE = 100;

    private Canvas sky;
    private TimeAnimator timeAnimator;
    private DateTimeBean dateTimeBean;
    private ObserverLocationBean observerLocationBean;
    private ViewingParametersBean viewingParametersBean;
    private SkyCanvasManager canvasManager;

    private boolean loadedFont = true;
    private boolean loadedResources = true;


    private InputStream resourceStream( String resourceName )
    {
        return getClass().getResourceAsStream( resourceName );
    }


    public static void main( String[] args )
    {
        launch( args );
    }

    @Override
    public void start( Stage primaryStage )
    {
        dateTimeBean = new DateTimeBean();
        dateTimeBean.setZonedDateTime( ZonedDateTime.now() );
        timeAnimator = new TimeAnimator( dateTimeBean );
        TimeAccelerator accelerator = NamedTimeAccelerator.TIMES_3000.getAccelerator();
        timeAnimator.setAccelerator( accelerator );

        observerLocationBean = new ObserverLocationBean();
        viewingParametersBean = new ViewingParametersBean();

        initStarsAndAsterisms();

        BorderPane wrapper = new BorderPane();
        wrapper.setMinWidth( 800 );
        wrapper.setMinHeight( 600 );

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




    private void initStarsAndAsterisms()
    {
        StarCatalogue.Builder builder = new StarCatalogue.Builder();
        StarCatalogue catalogue;

        try ( InputStream hygStream = resourceStream( HYG_CATALOGUE_NAME ) )
        {
            builder.loadFrom( hygStream, HygDatabaseLoader.INSTANCE);
        } catch ( IOException e )
        {
            loadedResources = false;
            return;
        }

        try ( InputStream asterismStream = getClass().getResourceAsStream( ASTERISM_CATALOGUE_NAME ) )
        {
            builder.loadFrom( asterismStream, AsterismLoader.INSTANCE );
        }
        catch ( IOException e )
        {
            loadedResources = false;
            return;
        }

        catalogue = builder.build();
        observerLocationBean.setCoordinates( GeographicCoordinates.ofDeg( INIT_OBSERVER_LON, INIT_OBSERVER_LAT ) );
        viewingParametersBean.setCenter( HorizontalCoordinates.ofDeg( INIT_VIEWING_LON, INIT_VIEWING_LAT ) );
        viewingParametersBean.setFieldOfViewDeg( INIT_FOV_VALUE );

        canvasManager = new SkyCanvasManager(
                catalogue, dateTimeBean,
                observerLocationBean, viewingParametersBean );
    }



    //      I) TOP TAB)
    // TODO : REORDER  ZoneId.getAvailableZoneIds() by alphabetic order
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
        TextFormatter<Number> lonTextFormatter = new TextFormatter<>( TWO_DECIMAL_CONVERTER, INIT_OBSERVER_LON, lonFilter );
        posLongitudeField.setTextFormatter( lonTextFormatter );
        observerLocationBean.lonDegProperty().bind( lonTextFormatter.valueProperty() );



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
        TextFormatter<Number> latTextFormatter = new TextFormatter<>( TWO_DECIMAL_CONVERTER, INIT_OBSERVER_LAT, latFilter );
        posLatitudeField.setTextFormatter( latTextFormatter );
        observerLocationBean.latDegProperty().bind( latTextFormatter.valueProperty() );

        posBox.getChildren().addAll( posLongitudeLabel, posLongitudeField, posLatitudeLabel, posLatitudeField );


        // MIDDLE PART Date Hour TimeZone
        HBox timeDetailsBox = new HBox();
        timeDetailsBox.setStyle( "-fx-spacing: inherit; -fx-alignment: baseline-left;" );

        // Date
        Label dateLabel = new Label( "Date :" );
        DatePicker datePicker = new DatePicker();
        datePicker.setStyle( "-fx-pref-width: 120;" );
        datePicker.setValue( dateTimeBean.getDate() );
        dateTimeBean.dateProperty().bindBidirectional( datePicker.valueProperty() );

        // Time
        Label hourLabel = new Label( "Heure :" );
        TextField hourField = new TextField();
        hourField.setStyle( "-fx-pref-width: 75; -fx-alignment: baseline-right;" );
        // Hour Formatter
        DateTimeFormatter hmsFormatter = DateTimeFormatter.ofPattern( "HH:mm:ss" );
        LocalTimeStringConverter stringConverter = new LocalTimeStringConverter( hmsFormatter, hmsFormatter );
        TextFormatter<LocalTime> timeFormatter = new TextFormatter<>( stringConverter, dateTimeBean.getTime() );
        hourField.setTextFormatter( timeFormatter );
        hourField.setText( dateTimeBean.getTime().toString() );
        dateTimeBean.timeProperty().bindBidirectional( timeFormatter.valueProperty() );

        // Time Zone
        // directly sort the zone ids by alphabetical order
        SortedSet<String> zoneIdsSet = new TreeSet<>( ZoneId.getAvailableZoneIds() );
        // and turn the sorted set into an observable list
        ObservableList zoneIds = FXCollections.observableList( new ArrayList<>( zoneIdsSet ) );
        ComboBox timezoneBox = new ComboBox( zoneIds );
        timezoneBox.setStyle( "-fx-pref-width: 180;" );
        timezoneBox.setValue( ZoneId.systemDefault() );
        timezoneBox.getSelectionModel().selectedItemProperty().addListener( ( observable, oldValue, newValue ) ->
             dateTimeBean.setZone( ZoneId.of( newValue.toString() ) )
        );


        timeDetailsBox.getChildren().addAll( dateLabel, datePicker, hourLabel, hourField, timezoneBox );


        // RIGHT PART
        HBox timeManagerBox = new HBox();
        timeManagerBox.setStyle( "-fx-spacing: inherit; -fx-alignment: baseline-right;" );

        ObservableList acceleratorsName = FXCollections.observableList( new ArrayList<>( NamedTimeAccelerator.ACCELERATORS.keySet() ) );
        ChoiceBox acceleratorChoiceBox = new ChoiceBox( acceleratorsName );
        acceleratorChoiceBox.setValue( NamedTimeAccelerator.TIMES_300.getName() );
        acceleratorChoiceBox.getSelectionModel().selectedItemProperty().addListener( ( observable, oldValue, newValue ) ->
                timeAnimator.setAccelerator( NamedTimeAccelerator.ACCELERATORS.get( newValue.toString() ) )
        );

        // Buttons : refresh, play/pause
        Button resetButton = new Button();
        Button playPauseButton = new Button();
        // load font or assign the button's text to a backup value
        try ( InputStream fontStream = resourceStream( "/Font Awesome 5 Free-Solid-900.otf" ) )
        {
            Font fontAwesome = Font.loadFont( fontStream, 15 );
            resetButton.setFont( fontAwesome );
            resetButton.setText( RESET_BTN_TEXT );
            playPauseButton.setFont( fontAwesome );
            playPauseButton.setText( PLAY_BTN_TEXT );
            loadedFont = true;
        }
        catch ( IOException e )
        {
            resetButton.setText( BACKUP_RESET_BTN_TEXT );
            playPauseButton.setText( BACKUP_PLAY_BTN_TEXT );
            loadedFont = false;
        }

        // play pause logic
        playPauseButton.setOnMouseClicked( mouseEvent -> {
            if ( timeAnimator.isRunning().get() )
            {
                timeAnimator.stop();
                playPauseButton.setText( loadedFont ?  PLAY_BTN_TEXT : BACKUP_PLAY_BTN_TEXT );
            }
            else
            {
                timeAnimator.start();
                playPauseButton.setText( loadedFont ?  PAUSE_BTN_TEXT : BACKUP_PAUSE_BTN_TEXT );
            }
        } );

        resetButton.setOnMouseClicked( mouseEvent -> {
            dateTimeBean.setZonedDateTime( ZonedDateTime.now() );
            timezoneBox.setValue( ZoneId.systemDefault() );
        } );

        timeManagerBox.getChildren().addAll( acceleratorChoiceBox, resetButton, playPauseButton );

        // disable all inputs if the animation is running and enable them if not
        timeAnimator.isRunning().addListener( ( o, oV, nV ) -> {
            datePicker.setDisable( nV );
            hourField.setDisable( nV );
            timezoneBox.setDisable( nV );
            acceleratorChoiceBox.setDisable( nV );
            resetButton.setDisable( nV );
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
    private Pane buildSky()
    {
        if ( loadedResources )
        {
            sky = canvasManager.canvas();
            return new Pane( sky );
        }
        else
        {
            return new Pane();
        }
    }


    //      III) BOTTOM TAB
    private BorderPane buildBottomTab()
    {
        BorderPane bottomTab = new BorderPane();
        bottomTab.setStyle( "-fx-padding: 4;\n" +
                "-fx-background-color: white;" );

        Text left = new Text();
        StringExpression fov = Bindings.format(Locale.ROOT,"Champ de vue : %.1f°", viewingParametersBean.fieldOfViewDegProperty());
        left.textProperty().bind(fov);

        Text center = new Text();
        center.textProperty().bind(canvasManager.objectUnderMouse);


        Text right = new Text();
        StringExpression mouseHorizontal = Bindings.format( Locale.ROOT, "Azimut : %.2f°, hauteur : %.2f°", canvasManager.mouseAzDeg, canvasManager.mouseAltDeg );
        right.textProperty().bind(mouseHorizontal);


        // add children
        bottomTab.setLeft(left);
        bottomTab.setCenter(center);
        bottomTab.setRight(right);

        return bottomTab;
    }
}
