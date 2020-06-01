package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;

import java.time.ZoneId;

// sort of bean but with immutable properties (no setters)
public enum NamedObserverLocations
{
    EPFL( "EPFL",  6.57, 46.52, "Europe/Zurich" ),
    NEW_YORK( "New York",  -73.96, 40.78, "America/New_York" ),
    LONDON( "Londres", -0.13, 51.51, "Europe/London" );

    private final ObservableObjectValue<String> name;
    private final ObservableObjectValue<ObserverLocationBean> observerLocationBean;
    private final ObservableObjectValue<ZoneId> zoneId;

    NamedObserverLocations( String name, double lon, double lat, String zoneIdName )
    {
        this.name = new SimpleObjectProperty<>( name );
        this.observerLocationBean = new SimpleObjectProperty<>( new ObserverLocationBean() );
        this.observerLocationBean.get().setLonDeg( lon );
        this.observerLocationBean.get().setLatDeg( lat );
        this.zoneId = new SimpleObjectProperty<>( ZoneId.of( zoneIdName ) );
    }

    public ObservableObjectValue<ObserverLocationBean> observerLocationBeanProperty() { return observerLocationBean; }
    public ObserverLocationBean getObserverLocationBean() { return observerLocationBean.get(); }

    public ObservableObjectValue<String> nameProperty() { return name; }
    public String getName() { return name.get(); }

    public ObservableObjectValue<ZoneId> zoneIdProperty() { return zoneId; }
    public ZoneId getZoneId() { return zoneId.get(); }

    public double getLon() { return observerLocationBean.get().getLonDeg(); }
    public double getLat() { return observerLocationBean.get().getLatDeg(); }

    public GeographicCoordinates getCoordinates() { return observerLocationBean.get().getCoordinates(); }

    @Override
    public String toString() { return getName(); }
}
