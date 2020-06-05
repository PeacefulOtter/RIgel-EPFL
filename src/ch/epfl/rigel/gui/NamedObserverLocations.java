package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;

import java.time.ZoneId;

/**
 * Collection of locations around the world consisting of a Name, a longitude, a latitude, and a zone id
 */
public enum NamedObserverLocations
{
    CUSTOM( "Custom", 0, 0, "Etc/GMT" ),
    EPFL( "EPFL",  6.57, 46.52, "Europe/Zurich" ),
    NEW_YORK( "New York",  -73.96, 40.78, "America/New_York" ),
    LONDON( "London", -0.13, 51.51, "Europe/London" ),
    REYKJAVIK( "Reykjavik", -22, 64.1, "Atlantic/Reykjavik" ),
    DELHI( "Delhi", 77.1, 28.7, "Asia/Calcutta" ),
    SYDNEY( "Sydney", 151.2, -33.86 ,"Australia/Sydney" );

    private final String name;
    private final ObserverLocationBean observerLocationBean;
    private final ZoneId zoneId;

    NamedObserverLocations( String name, double lon, double lat, String zoneIdName )
    {
        this.name = name;
        this.observerLocationBean = new ObserverLocationBean();
        this.observerLocationBean.setLonDeg( lon );
        this.observerLocationBean.setLatDeg( lat );
        this.zoneId = ZoneId.of( zoneIdName );
    }

    public ObserverLocationBean getObserverLocationBean() { return observerLocationBean; }
    public String getName() { return name; }
    public ZoneId getZoneId() { return zoneId; }

    public GeographicCoordinates getCoordinates() { return observerLocationBean.getCoordinates(); }
    public double getLon() { return observerLocationBean.getLonDeg(); }
    public double getLat() { return observerLocationBean.getLatDeg(); }

    @Override
    public String toString() { return getName(); }
}
