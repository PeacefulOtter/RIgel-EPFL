package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;

import java.time.ZoneId;

// Collection of locations around the world consisting of a Name, a longitude and latitude, and a zone id
public enum NamedObserverLocations
{
    CUSTOM( "Custom", 0, 0, "Etc/GMT" ),
    EPFL( "EPFL",  6.57, 46.52, "Europe/Zurich" ),
    NEW_YORK( "New York",  -73.96, 40.78, "America/New_York" ),
    LONDON( "London", -0.13, 51.51, "Europe/London" ),
    REYKJAVIK( "Reykjavik", -22, 64.1, "Atlantic/Reykjavik" ),
    DELHI( "Delhi", 77.1, 28.7, "Asia/Calcutta" ),
    SYDNEY( "Sydney", 151.2, -33.86 ,"Australia/Sydney" );

    private final ObservableObjectValue<String> name;
    private final ObservableObjectValue<ObserverLocationBean> observerLocationBean;
    private final ObservableObjectValue<ZoneId> zoneId;

    NamedObserverLocations( String name, double lon, double lat, String zoneIdName )
    {
        this.name = new SimpleObjectProperty<>( name );
        ObserverLocationBean olb = new ObserverLocationBean();
        olb.setLonDeg( lon ); olb.setLatDeg( lat );
        this.observerLocationBean = new SimpleObjectProperty<>( olb );
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
