package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.gui.Card;

import java.util.HashMap;
import java.util.Map;

public class SolarSystemData
{
    private static final SolarSystemInfo[] SOLAR_SYSTEM_INFOS = SolarSystemInfo.values();
    private static final Map<String, Card> solarSystemCardsMap = new HashMap<>();

    public static Map<String, Card> getCardsMap()
    {
        for ( SolarSystemInfo info: SOLAR_SYSTEM_INFOS )
        {
            solarSystemCardsMap.put( info.getName(), info.getCard() );
        }
        return solarSystemCardsMap;
    }

    public Map<String, Card> getCardMap() { return solarSystemCardsMap; }
}

