
package ch.epfl.sigcheck_part3;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.coordinates.*;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class SignatureChecks_3 {
    @Test
    void checkEpoch() {
        //double d;
        /*
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
        */
        ZonedDateTime a = ZonedDateTime.of(
                LocalDate.of(2003, Month.JULY, 30),
                LocalTime.of(15, 0),
                ZoneOffset.UTC);
        ZonedDateTime b = ZonedDateTime.of(
                LocalDate.of(2020, Month.MARCH, 20),
                LocalTime.of(0, 0),
                ZoneOffset.UTC);
        ZonedDateTime c = ZonedDateTime.of(
                LocalDate.of(2006, Month.JUNE, 16),
                LocalTime.of(18, 13),
                ZoneOffset.UTC);
        ZonedDateTime d = ZonedDateTime.of(
                LocalDate.of(2000, Month.JANUARY, 3),
                LocalTime.of(18, 0),
                ZoneOffset.UTC);
        ZonedDateTime e = ZonedDateTime.of(
                LocalDate.of(1999, Month.DECEMBER, 6),
                LocalTime.of(23, 3),
                ZoneOffset.UTC);

        assertEquals(1306.125, Epoch.J2000.daysUntil(a), 1e-6);
        assertEquals(7383.5, Epoch.J2000.daysUntil(b), 1e-6);
        assertEquals(2358.259028, Epoch.J2000.daysUntil(c), 1e-6);
        assertEquals(2.25, Epoch.J2000.daysUntil(d), 1e-6);
        assertEquals(-25.539583, Epoch.J2000.daysUntil(e), 1e-6);

        assertEquals(-2345.375, Epoch.J2010.daysUntil(a), 1e-6);
        assertEquals(3732, Epoch.J2010.daysUntil(b), 1e-6);
        assertEquals(-1293.240972, Epoch.J2010.daysUntil(c), 1e-6);
        assertEquals(-3649.25, Epoch.J2010.daysUntil(d), 1e-6);
        assertEquals(-3677.039583, Epoch.J2010.daysUntil(e), 1e-6);


    }

    @Test
    void checkSiderealTime() {
        assertEquals(Angle.ofHr(4.668119327), SiderealTime.greenwich(ZonedDateTime.of(LocalDate.of(1980, Month.APRIL, 22), LocalTime.of(14, 36, 51, (int) 6.7e8), ZoneOffset.UTC)), 1e-6);
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
    @Test
    void worksWithValidCoordinates(){
        EclipticCoordinates start = EclipticCoordinates.of(Angle.ofDMS(139,41,10),Angle.ofDMS(4,52,31));
        var conversion = new EclipticToEquatorialConversion(ZonedDateTime.of(2009,7,6,9,34,53,0, ZoneOffset.UTC));
        assertEquals("(ra=9.5815h, dec=19.5350°)", conversion.apply(start).toString());
    }

    @Test
    void ConversionWorksOnBaseCase(){
        var start = EquatorialCoordinates.of(Angle.ofHr(5.862222222),Angle.ofDeg(23.21944444));
        var conversion= new EquatorialToHorizontalConversion(ZonedDateTime.now(),GeographicCoordinates.ofDeg(0,52));
        System.out.println(conversion.apply(start));
    }
}
