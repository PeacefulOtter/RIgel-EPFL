package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.gui.Card;

import java.util.HashMap;
import java.util.Map;

public class SolarSystemData
{
    private static final SolarSystemInfo[] SOLAR_SYSTEM_INFO = SolarSystemInfo.values();
    private static final Map<String, Card> SOLAR_SYSTEM_CARD_MAP = new HashMap<>();

    public static Map<String, Card> getCardsMap()
    {
        for ( SolarSystemInfo info: SOLAR_SYSTEM_INFO )
        {
            SOLAR_SYSTEM_CARD_MAP.put( info.getName(), info.getCard() );
        }
        return SOLAR_SYSTEM_CARD_MAP;
    }
}

