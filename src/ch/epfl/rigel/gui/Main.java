package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.SolarSystemData;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.coordinates.EquatorialToHorizontalConversion;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.*;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.UnaryOperator;



public class Main extends Application
{
    // file names to load the stars and asterisms
    private static final String HYG_CATALOGUE_NAME = "/hygdata_v3.csv";
    private static final String ASTERISM_CATALOGUE_NAME = "/asterisms.txt";

    // Buttons text and backup text if the font cant load
    private static final String RESET_BTN_TEXT = "\uf0e2";
    private static final String BACKUP_RESET_BTN_TEXT = "R";
    private static final String PLAY_BTN_TEXT = "\uf04b";
    private static final String BACKUP_PLAY_BTN_TEXT = "▶";
    private static final String PAUSE_BTN_TEXT = "\uf04c";
    private static final String BACKUP_PAUSE_BTN_TEXT = "\u23F8";
    private static final String DOWNLOAD_BTN_TEXT = "\uf019";
    private static final String BACKUP_DOWNLOAD_BTN_TEXT = "Download";
    private static final String IMPORT_BTN_TEXT = "\uf574";
    private static final String BACKUP_IMPORT_BTN_TEXT = "Import";

    // Number string converter to have a result with only two decimals
    private static final NumberStringConverter TWO_DECIMALS_CONVERTER = new NumberStringConverter("#0.00" );
    // a date-time formatter used to saved files with different names : "RigelSave_dd-MM-yyyy_HH-mm-ss.txt"
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern( "dd-MM-yyyy_HH-mm-ss" );
    // searching only for txt files
    private static final FileChooser.ExtensionFilter EXTENSION_FILTER =
            new FileChooser.ExtensionFilter("Text Files", "*.txt" );

    private static final NamedTimeAccelerator DEFAULT_ACCELERATOR = NamedTimeAccelerator.TIMES_300;
    private static final String DEFAULT_ZONE_ID_NAME = ZoneId.systemDefault().toString();

    // init constants
    private static final NamedObserverLocations DEFAULT_OBSERVER_LOCATION = NamedObserverLocations.EPFL;
    private static final double INIT_VIEWING_LON = 180.000000000001;
    private static final double INIT_VIEWING_LAT = 15;
    private static final double INIT_FOV_VALUE = 100;
    private static final int MIN_CANVAS_WIDTH = 800;
    private static final int MIN_CANVAS_HEIGHT = 600;

    // Card Transitions
    private static final Duration TRANSITION_DURATION =  Duration.millis( 250 );
    private static final FadeTransition FADE_IN_TRANSITION = new FadeTransition( TRANSITION_DURATION );
    private static final FadeTransition FADE_OUT_TRANSITION = new FadeTransition( TRANSITION_DURATION );

    // private attributes used inside the whole class
    private Stage primaryStage;
    private Canvas sky;
    private TimeAnimator timeAnimator;
    private DateTimeBean dateTimeBean;
    private ObserverLocationBean observerLocationBean;
    private ViewingParametersBean viewingParametersBean;
    private SkyCanvasManager canvasManager;

    private Font fontAwesome;
    private DatePicker datePicker;
    private TextField timeField;
    private ComboBox<String> timezoneBox;
    private ChoiceBox<NamedTimeAccelerator> acceleratorChoiceBox;
    private ChoiceBox<NamedObserverLocations> observerChoiceBox;
    private Button resetButton, playPauseButton, downloadButton, importButton;
    private FileChooser importInput;
    private TextFormatter<Number> lonTextFormatter, latTextFormatter;
    private Pane skyPane;
    private String objectUnderMouseName;

    private boolean loadedFont = false;
    private boolean loadedResources = true;

    // an input stream to read the files
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
        this.primaryStage = primaryStage;
        // transitions from-to values
        FADE_IN_TRANSITION.setFromValue( 0 );
        FADE_IN_TRANSITION.setToValue( 1 );
        FADE_OUT_TRANSITION.setFromValue( 1 );
        FADE_OUT_TRANSITION.setToValue( 0 );

        // define a date, time, time animator, accelerator, observer location and viewing parameters
        dateTimeBean = new DateTimeBean();
        dateTimeBean.setZonedDateTime( ZonedDateTime.now() );

        timeAnimator = new TimeAnimator( dateTimeBean );
        timeAnimator.setAccelerator( DEFAULT_ACCELERATOR.getAccelerator() );

        observerLocationBean = new ObserverLocationBean();
        observerLocationBean.setCoordinates( DEFAULT_OBSERVER_LOCATION.getObserverLocationBean().getCoordinates() );

        viewingParametersBean = new ViewingParametersBean();
        viewingParametersBean.setCenter( HorizontalCoordinates.ofDeg( INIT_VIEWING_LON, INIT_VIEWING_LAT ) );
        viewingParametersBean.setFieldOfViewDeg( INIT_FOV_VALUE );

        // get the stars and asterisms from the files in the resource folder and load the font
        initLoadFiles();

        // get screen size
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        double canvasWidth = screenBounds.getWidth();
        double canvasHeight = screenBounds.getHeight();

        // create the main wrapper that takes the entire window
        BorderPane wrapper = new BorderPane();
        wrapper.setMinWidth( MIN_CANVAS_WIDTH );
        wrapper.setMinHeight( MIN_CANVAS_HEIGHT );
        wrapper.setMaxWidth( canvasWidth );
        wrapper.setMaxHeight( canvasHeight );

        // create and set the different parts of the program
        wrapper.setTop( buildTopTab() );
        wrapper.setCenter( buildSky() );
        wrapper.setBottom( buildBottomTab() );

        // bind the size of the sky to the wrapper
        sky.widthProperty().bind( wrapper.widthProperty() );
        sky.heightProperty().bind( wrapper.heightProperty() );

        // set pref size
        primaryStage.setMinWidth( MIN_CANVAS_WIDTH );
        primaryStage.setMinHeight( MIN_CANVAS_HEIGHT );
        primaryStage.setMaxWidth( canvasWidth );
        primaryStage.setMaxHeight( canvasHeight );

        primaryStage.setScene( new Scene( wrapper ) );
        primaryStage.setMaximized( true );
        primaryStage.show();

        sky.requestFocus();
    }


    /**
     * Retrieve the stars and asterisms from the /ressource folder and add them into the star catalogue
     *  and build the SkyCanvasManager
     */
    private void initLoadFiles()
    {
        StarCatalogue.Builder builder = new StarCatalogue.Builder();
        StarCatalogue catalogue;

        // Stars
        try ( InputStream hygStream = resourceStream( HYG_CATALOGUE_NAME ) )
        {
            builder.loadFrom( hygStream, HygDatabaseLoader.INSTANCE);
        } catch ( IOException e )
        {
            loadedResources = false;
            return;
        }

        // Asterisms
        try ( InputStream asterismStream = resourceStream( ASTERISM_CATALOGUE_NAME ) )
        {
            builder.loadFrom( asterismStream, AsterismLoader.INSTANCE );
        }
        catch ( IOException e )
        {
            loadedResources = false;
            return;
        }

        catalogue = builder.build();

        canvasManager = new SkyCanvasManager(
                catalogue, dateTimeBean,
                observerLocationBean, viewingParametersBean );

        // load the font with the fontLoader class
        fontAwesome = new FontLoader().loadFontAwesome();
        if ( fontAwesome != null ) { loadedFont = true; }
    }



    /**
     * The top tab control bar consisting of three sub-sections separated from each other by separators.
     * From left to right : the observation position, the instant of observation, and the simulated time elapsed.
     */
    private HBox buildTopTab()
    {
        HBox topTab = new HBox(); // wrapper
        topTab.setStyle( "-fx-spacing: 4; -fx-padding: 4;" );

        // FIRST PART : observer locations
        HBox observerLocationsBox = initObserverLocationsInput();
        // SECOND PART : longitude, latitude
        HBox posBox = initPositionBox();
        // THIRD PART : date, hour, zone id
        HBox timeDateZoneBox = initDateTimeZoneBox();
        // FOURTH PART : accelerator and buttons
        HBox buttonsBox = initButtonsBox();
        // FIFTH PART : search Bar
        HBox searchBar = initSearchBar();

        // initialize mouse clicked event on the play/pause and reset buttons
        initMouseClickedEvents();

        // enable/disable all inputs depending on the animation running or not
        timeAnimator.isRunning().addListener( ( o, oV, nV ) -> {
            datePicker.setDisable( nV );
            timeField.setDisable( nV );
            timezoneBox.setDisable( nV );
            acceleratorChoiceBox.setDisable( nV );
            resetButton.setDisable( nV );
        } );

        // add the three parts together as well as the separators between them
        topTab.getChildren().addAll(
                observerLocationsBox,
                new Separator( Orientation.VERTICAL ),
                posBox,
                new Separator( Orientation.VERTICAL ),
                timeDateZoneBox,
                new Separator( Orientation.VERTICAL ),
                buttonsBox,
                new Separator( Orientation.VERTICAL ),
                searchBar);
        return topTab;
    }

    private HBox initObserverLocationsInput()
    {
        HBox NamedObserverBox = new HBox(); // container
        NamedObserverBox.setStyle( "-fx-spacing: inherit; -fx-alignment: baseline-right;" );

        // creates an observable list of Time Accelerators names
        List<NamedObserverLocations> observerLocations = new ArrayList<>();
        Collections.addAll( observerLocations, NamedObserverLocations.values() );
        ObservableList<NamedObserverLocations> observerLocationName = FXCollections.observableList( observerLocations );
        observerChoiceBox = new ChoiceBox<>( observerLocationName );
        observerChoiceBox.setValue( DEFAULT_OBSERVER_LOCATION );
        observerChoiceBox.getSelectionModel().selectedItemProperty().addListener( ( observable, oldValue, newValue ) -> {
            lonTextFormatter.setValue( newValue.getLon() );
            latTextFormatter.setValue( newValue.getLat() );
            timezoneBox.setValue( newValue.getZoneId().getId() );
        } );

        NamedObserverBox.getChildren().addAll( observerChoiceBox );

        return NamedObserverBox;
    }


    /**
     * Observation position box
     * @return HBox
     */
    private HBox initPositionBox()
    {
        HBox posBox = new HBox(); // container
        posBox.setStyle( "-fx-spacing: inherit; -fx-alignment: baseline-left;" );

        // Longitude label and field
        Label posLongitudeLabel = new Label( "Longitude (°) :" );
        TextField posLongitudeField = new TextField();
        posLongitudeField.setStyle( "-fx-pref-width: 60; -fx-alignment: baseline-right;" );
        // Longitude Text Formatter - with two decimals
        UnaryOperator<TextFormatter.Change> lonFilter = ( change -> {
            try
            {
                String newText = change.getControlNewText();
                double newLonDeg = TWO_DECIMALS_CONVERTER.fromString( newText ).doubleValue();
                return GeographicCoordinates.isValidLonDeg( newLonDeg ) ? change : null;
            }
            catch ( Exception e ) { return null; }
        } );
        lonTextFormatter = new TextFormatter<>(
                TWO_DECIMALS_CONVERTER, DEFAULT_OBSERVER_LOCATION.getLon(), lonFilter );
        posLongitudeField.setTextFormatter( lonTextFormatter );
        // bind the longitude with the observer longitude latitude
        observerLocationBean.lonDegProperty().bind( lonTextFormatter.valueProperty() );


        // Latitude label and field
        Label posLatitudeLabel = new Label( "Latitude (°) :" );
        TextField posLatitudeField = new TextField();
        posLatitudeField.setStyle( "-fx-pref-width: 60; -fx-alignment: baseline-right;" );
        // Latitude Text Formatter - with two decimals
        UnaryOperator<TextFormatter.Change> latFilter = ( change -> {
            try
            {
                String newText = change.getControlNewText();
                double newLatDeg = TWO_DECIMALS_CONVERTER.fromString( newText ).doubleValue();
                return GeographicCoordinates.isValidLatDeg( newLatDeg ) ? change : null;
            }
            catch ( Exception e ) { return null; }
        } );
        latTextFormatter = new TextFormatter<>(
                TWO_DECIMALS_CONVERTER, DEFAULT_OBSERVER_LOCATION.getLat(), latFilter );
        posLatitudeField.setTextFormatter( latTextFormatter );
        // bind the latitude with the observer location latitude
        observerLocationBean.latDegProperty().bind( latTextFormatter.valueProperty() );

        posBox.getChildren().addAll( posLongitudeLabel, posLongitudeField, posLatitudeLabel, posLatitudeField );

        return posBox;
    }

    /**
     * Initialize the date, time and zone inputs inside a HBox
     * @return HBox - the box containing these inputs
     */
    private HBox initDateTimeZoneBox()
    {
        HBox timeDateZoneBox = new HBox(); // container
        timeDateZoneBox.setStyle( "-fx-spacing: inherit; -fx-alignment: baseline-left;" );

        // Date label
        Label dateLabel = new Label( "Date :" );
        // Date Input
        datePicker = new DatePicker();
        datePicker.setStyle( "-fx-pref-width: 120;" );
        datePicker.setValue( dateTimeBean.getDate() );
        // Bidirectional bind the date input with the dateTimeBean's date
        dateTimeBean.dateProperty().bindBidirectional( datePicker.valueProperty() );

        // Time Label
        Label timeLabel = new Label( "Heure :" );
        // Time Input
        timeField = new TextField();
        timeField.setStyle( "-fx-pref-width: 75; -fx-alignment: baseline-right;" );
        // Date/Time Formatter
        DateTimeFormatter hmsFormatter = DateTimeFormatter.ofPattern( "HH:mm:ss" );
        LocalTimeStringConverter stringConverter = new LocalTimeStringConverter( hmsFormatter, hmsFormatter );
        TextFormatter<LocalTime> timeFormatter = new TextFormatter<>( stringConverter, dateTimeBean.getTime() );
        timeField.setTextFormatter( timeFormatter );
        timeField.setText( dateTimeBean.getTime().toString() );
        // Bidirectional bind the time input text with the dateTimeBean's time
        dateTimeBean.timeProperty().bindBidirectional( timeFormatter.valueProperty() );

        // Zone Ids
        // directly sort the zone ids by alphabetical order
        SortedSet<String> zoneIdsSet = new TreeSet<>( ZoneId.getAvailableZoneIds() );
        // and turn the sorted set into an observable list
        ObservableList<String> zoneIds = FXCollections.observableList( new ArrayList<>( zoneIdsSet ) );
        timezoneBox = new ComboBox<>( zoneIds );
        timezoneBox.setStyle( "-fx-pref-width: 180;" );
        timezoneBox.setValue( DEFAULT_ZONE_ID_NAME );
        // add a listener to the selected item so that when the value changes, it updates the zoneId of the dateTimeBean.
        // We don't use a bind here because the selectedItemProperty is a ReadOnly property and
        // the types are different, timezoneBox uses String whereas dateTimeBean uses ZoneId
        timezoneBox.getSelectionModel().selectedItemProperty().addListener( ( observable, oldValue, newValue ) ->
                dateTimeBean.setZone( ZoneId.of( newValue ) )
        );

        // add all the components to the box
        timeDateZoneBox.getChildren().addAll( dateLabel, datePicker, timeLabel, timeField, timezoneBox );

        return timeDateZoneBox;
    }

    /**
     * Creates the accelerator choice box and the buttons
     * @return HBox - the box containing the accelerator choice box and the buttons
     */
    private HBox initButtonsBox()
    {
        HBox acceleratorButtonsBox = new HBox(); // container
        acceleratorButtonsBox.setStyle( "-fx-spacing: inherit; -fx-alignment: baseline-right;" );

        // creates an observable list of Time Accelerators names
        List<NamedTimeAccelerator> accNames = new ArrayList<>();
        Collections.addAll( accNames, NamedTimeAccelerator.values() );
        ObservableList<NamedTimeAccelerator> acceleratorsName = FXCollections.observableList( accNames );

        acceleratorChoiceBox = new ChoiceBox<>( acceleratorsName );
        acceleratorChoiceBox.setValue( DEFAULT_ACCELERATOR );
        // add a listener to the selected item so that when the choice box value changes, it updates the accelerator
        // of the TimeAnimator. We don't use a bind here because the selectedItemProperty is a ReadOnly property and
        // the types are different : acceleratorChoiceBox uses NamedTimeAccelerator whereas setAccelerator()
        // requires a TimeAccelerator
        // A listener here does exactly the same thing as a bind though
        acceleratorChoiceBox.getSelectionModel().selectedItemProperty().addListener( ( observable, oldValue, newValue ) ->
            timeAnimator.setAccelerator( newValue.getAccelerator() )
        );

        // refresh and play/pause buttons
        resetButton = new Button();
        playPauseButton = new Button();
        downloadButton = new Button();
        importButton = new Button();
        importInput = new FileChooser();
        String programPath = Paths.get( "." ).toAbsolutePath().normalize().toString();
        importInput.setInitialDirectory( new File( programPath ) );
        importInput.setSelectedExtensionFilter( EXTENSION_FILTER );

        if ( loadedFont )
        {
            resetButton.setFont( fontAwesome );
            resetButton.setText( RESET_BTN_TEXT );
            playPauseButton.setFont( fontAwesome );
            playPauseButton.setText( PLAY_BTN_TEXT );
            downloadButton.setFont( fontAwesome );
            downloadButton.setText( DOWNLOAD_BTN_TEXT );
            importButton.setFont( fontAwesome );
            importButton.setText( IMPORT_BTN_TEXT );
        }
        else
        {
            resetButton.setText( BACKUP_RESET_BTN_TEXT );
            playPauseButton.setText( BACKUP_PLAY_BTN_TEXT );
            downloadButton.setText( BACKUP_DOWNLOAD_BTN_TEXT );
            importInput.setTitle( BACKUP_IMPORT_BTN_TEXT );
        }

        // add all the components to the box
        acceleratorButtonsBox.getChildren().addAll(
                acceleratorChoiceBox,
                resetButton,
                playPauseButton,
                new Separator( Orientation.VERTICAL ),
                downloadButton,
                importButton );

        return acceleratorButtonsBox;
    }

    private HBox initSearchBar()
    {
        HBox searchBox = new HBox();
        TextField searchText = new TextField();
        searchText.setStyle( "-fx-pref-width: 100; -fx-alignment: baseline-left; -fx-padding-left: 5pt; " );

        searchText.setOnKeyReleased( keyEvent -> {
            KeyCode key = keyEvent.getCode(); // get the key
            String inputValue = searchText.getText();
            if( key.equals( KeyCode.ENTER ) && inputValue.length() > 0 )
            {
                EquatorialToHorizontalConversion conversion = new EquatorialToHorizontalConversion(
                        dateTimeBean.getZonedDateTime(), observerLocationBean.getCoordinates() );
                EquatorialCoordinates equatorialCoordinates = canvasManager.getCoordinatesWithName( inputValue );
                if ( equatorialCoordinates != null )
                {
                    viewingParametersBean.setCenter( conversion.apply( equatorialCoordinates ) );
                }
            }
        } );

        searchText.setPromptText( "Search.." );
        searchBox.getChildren().add( searchText );
        return searchBox;
    }


    /**
     * Adds mouse clicked events to the buttons
     */
    private void initMouseClickedEvents()
    {
        // stop the time animator if it is playing or start it if it is stopped
        playPauseButton.setOnMouseClicked( mouseEvent -> {
            if ( timeAnimator.isRunning().get() )
            {
                timeAnimator.stop();
                // update the text
                playPauseButton.setText( loadedFont ?  PLAY_BTN_TEXT : BACKUP_PLAY_BTN_TEXT );
            }
            else
            {
                timeAnimator.start();
                // update the text
                playPauseButton.setText( loadedFont ?  PAUSE_BTN_TEXT : BACKUP_PAUSE_BTN_TEXT );
            }
        } );

        // resets the date, time, and zone to the ones at start
        resetButton.setOnMouseClicked( mouseEvent -> {
            dateTimeBean.setZonedDateTime( ZonedDateTime.now() );
            timezoneBox.setValue( DEFAULT_ZONE_ID_NAME );
            observerChoiceBox.setValue( DEFAULT_OBSERVER_LOCATION );
        } );

        downloadButton.setOnMouseClicked( mouseEvent ->  {
            LocalDateTime now = LocalDateTime.now();
            String fileName = "RigelSave_" + DATE_TIME_FORMATTER.format( now ) + ".txt";

            StringJoiner data = new StringJoiner("," )
                    .add( lonTextFormatter.getValue().toString() )
                    .add( latTextFormatter.getValue().toString() )
                    .add( datePicker.getValue().toString() )
                    .add( timeField.getText() )
                    .add( timezoneBox.getValue() )
                    .add( String.valueOf( viewingParametersBean.getCenter().azDeg() ) )
                    .add( String.valueOf( viewingParametersBean.getCenter().altDeg() ) )
                    .add( viewingParametersBean.getFieldOfViewDeg().toString() );

            try ( FileOutputStream fos = new FileOutputStream( fileName ) )
            {
                fos.write( data.toString().getBytes() );
                fos.flush();
                new NotificationBox()
                        .setSuccessLogo()
                        .setTitle( "File successfully saved !" )
                        .setParentElement( skyPane )
                        .fire();
            } catch ( IOException e )
            {
                new NotificationBox()
                        .setErrorLogo()
                        .setTitle( "File couldn't be saved" )
                        .setParentElement( skyPane )
                        .fire();
                e.printStackTrace();
            }
        } );

        importButton.setOnMouseClicked( mouseEvent ->  {
            File importedFiles = importInput.showOpenDialog( primaryStage );

            if ( importedFiles != null && importedFiles.getName().contains( "RigelSave" ) )
            {
                File importedFile = Collections.singletonList( importedFiles ).get( 0 );
                try ( BufferedReader reader = new BufferedReader( new FileReader( importedFile ) ) )
                {
                    String line;
                    while ( ( line = reader.readLine() ) != null )
                    {
                        String[] data = line.split( "," );
                        lonTextFormatter.setValue( Double.parseDouble( data[ 0 ] ) );
                        latTextFormatter.setValue( Double.parseDouble( data[ 1 ] ) );
                        int[] date = Arrays.stream( data[ 2 ].split( "-" ) ).mapToInt( Integer::parseInt ).toArray();
                        datePicker.setValue( LocalDate.of( date[ 0 ], date[ 1 ], date[ 2 ] ) );
                        timeField.setText( data[ 3 ] );
                        timezoneBox.setValue( data[ 4 ] );
                        HorizontalCoordinates newCenter =  HorizontalCoordinates.ofDeg(
                                Double.parseDouble( data[ 5 ] ), Double.parseDouble( data[ 6 ] ) );
                        viewingParametersBean.setCenter( newCenter );
                        viewingParametersBean.setFieldOfViewDeg( Double.parseDouble( data[ 7 ] ) );
                    }
                }
                catch ( IOException e )
                {
                    e.printStackTrace();
                }
            }
        } );
    }








    /**
     * Creates the center Pane, which is the sky
     */
    private Pane buildSky()
    {
        // return an empty pane if we did not achieve to load the resources
        if ( !loadedResources ) { return new Pane(); }

        // if we achieve to build the star catalogue, then we can draw the sky
        sky = canvasManager.canvas();
        skyPane = new Pane( sky );
        Map<String, Card> cardMap = SolarSystemData.getCardsMap();

        canvasManager.objectUnderMouseProperty().addListener( ( observable, oldValue, newValue ) -> {
            if ( skyPane.getChildren().size() > 1 && !newValue.equals( objectUnderMouseName ) )
            {
                FADE_OUT_TRANSITION.play();
                // remove the card from the pane when the transition is finished
                FADE_OUT_TRANSITION.setOnFinished( event -> skyPane.getChildren().remove( 1 ) );
            }
            if ( cardMap.containsKey( newValue ) )
            {
                Card card = cardMap.get( newValue );
                if ( !skyPane.getChildren().contains( card ) )
                {
                    skyPane.getChildren().add( card );
                    FADE_IN_TRANSITION.setNode( card );
                    FADE_OUT_TRANSITION.setNode( card );
                    FADE_IN_TRANSITION.play();
                }
            }
            objectUnderMouseName = newValue;
        } );
        return skyPane;
    }


    /**
     * Creates the bottom tab that contains the FOV, the name of the star/planet under the mouse, and the mouse position
     * @return BorderPane
     */
    private BorderPane buildBottomTab()
    {
        BorderPane bottomTab = new BorderPane(); // container
        bottomTab.setStyle( "-fx-padding: 4; -fx-background-color: white;" );

        // Fov text, bound to the viewing parameters fov
        Text fovText = new Text();
        StringExpression fov = Bindings.format( Locale.ROOT,"Champ de vue : %.1f°", viewingParametersBean.fieldOfViewDegProperty() );
        fovText.textProperty().bind( fov );

        // Name of the object under the mouse, bound to the objectUnderMouse property of the canvas manager
        Text objectUnderMouseText = new Text();
        objectUnderMouseText.textProperty().bind( canvasManager.objectUnderMouse );

        // Mouse coordinates, bound to the mouse position of the canvas manager
        Text mouseHorizontalText = new Text();
        StringExpression mouseHorizontal = Bindings.format( Locale.ROOT, "Azimut : %.2f°, hauteur : %.2f°", canvasManager.mouseAzDeg, canvasManager.mouseAltDeg );
        mouseHorizontalText.textProperty().bind( mouseHorizontal );

        // add children
        bottomTab.setLeft( fovText );
        bottomTab.setCenter( objectUnderMouseText );
        bottomTab.setRight( mouseHorizontalText );

        return bottomTab;
    }
}
