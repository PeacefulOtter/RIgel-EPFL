
package ch.epfl.sigcheck_part3;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.coordinates.*;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.function.Function;

final class SignatureChecks_3 {
    @Test
    void checkEpoch() {
        double d;
        ZonedDateTime z = ZonedDateTime.of(
                LocalDate.of(2000, Month.JANUARY, 3),
                LocalTime.of(18, 0),
                ZoneOffset.UTC);
        System.out.println(z);

        Epoch e = Epoch.J2000;
        System.out.println(Epoch.J2000.daysUntil(z));
        z = ZonedDateTime.of( LocalDate.of(2002, Month.JANUARY, 3),
                LocalTime.of(18, 0),
                ZoneOffset.UTC);
        System.out.println(Epoch.J2000.julianCenturiesUntil(z));

    }

    @Test
    void checkSiderealTime() {
        double d;
        //ZoneId utc = ZoneId.of( ZoneOffset.UTC.getId() );
        ZonedDateTime z = ZonedDateTime.of( 1980, 4, 22, 14, 36, 51, 670000000, ZoneId.of( "UTC") );
        System.out.println(z);
        //GeographicCoordinates g = null;
        d = SiderealTime.greenwich(z);
        System.out.println(d);
        //assertEquals( );
        //d = SiderealTime.local(z, g);
    }

    void checkEclipticToEquatorialConversion() {
        ZonedDateTime z = null;
        EclipticToEquatorialConversion e = new EclipticToEquatorialConversion(z);
        Function<EclipticCoordinates, EquatorialCoordinates> f = e;
    }

    void checkEquatorialToHorizontalConversion() {
        ZonedDateTime z = null;
        GeographicCoordinates g = null;
        EquatorialToHorizontalConversion e = new EquatorialToHorizontalConversion(z, g);
        Function<EquatorialCoordinates, HorizontalCoordinates> f = e;
    }
}
