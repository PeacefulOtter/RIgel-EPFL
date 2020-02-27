package ch.epfl.sigcheck;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

final class SignatureChecks_2 {
    @Test
    void checkGeographicCoordinates() {
        double d;
        GeographicCoordinates g;

        assertTrue( GeographicCoordinates.isValidLonDeg( 0 ) );
        assertFalse( GeographicCoordinates.isValidLonDeg( 200 ) );

        assertThrows( IllegalArgumentException.class, () -> {GeographicCoordinates.ofDeg(0, 90); } );
        assertThrows( IllegalArgumentException.class, () -> {GeographicCoordinates.ofDeg(180, 70); } );

        g = GeographicCoordinates.ofDeg( 90, 45 );
        d = g.lon();
        assertEquals( Math.PI / 2, d );
        d = g.lonDeg();
        assertEquals(90, d );
        d = g.lat();
        assertEquals( Math.PI / 4, d );
        d = g.latDeg();
        assertEquals( 45, d );
        System.out.println(g.toString());

        g = GeographicCoordinates.ofDeg( 45, 22.5 );
        d = g.lon();
        assertEquals( Math.PI / 4, d );
        d = g.lonDeg();
        assertEquals(45, d );
        d = g.lat();
        assertEquals( Math.PI / 8, d );
        d = g.latDeg();
        assertEquals( 22.5, d );

        System.out.println(g.toString());
    }
    @Test
    void checkHorizontalCoordinates() {
        double d = 0;
        String s = "";
        HorizontalCoordinates h;

        assertThrows( IllegalArgumentException.class, () -> {HorizontalCoordinates.ofDeg(400, 90); } );
        assertThrows( IllegalArgumentException.class, () -> {HorizontalCoordinates.ofDeg(100, -150); } );
        assertThrows( IllegalArgumentException.class, () -> {HorizontalCoordinates.of(Math.PI * 3, 2); } );
        assertThrows( IllegalArgumentException.class, () -> {HorizontalCoordinates.ofDeg(0, 500 ); } );

        h = HorizontalCoordinates.ofDeg( 225, 45 );
        d = h.lon();
        assertEquals( Math.PI + Math.PI / 4, d, Math.exp(-6) );
        d = h.lonDeg();
        assertEquals(225, d, Math.exp(-6) );
        d = h.lat();
        assertEquals( Math.PI / 4, d, Math.exp(-6) );
        d = h.latDeg();
        assertEquals( 45, d, Math.exp(-6) );
        System.out.println(h);
        System.out.println( h.azOctantName("N", "E", "S", "O") );

        HorizontalCoordinates m = HorizontalCoordinates.of( Math.PI / 3, Math.PI / 4 );
        d = m.lon();
        assertEquals( Math.PI / 3, d, Math.exp(-6) );
        d = m.lonDeg();
        assertEquals(60, d, Math.exp(-6) );
        d = m.lat();
        assertEquals( Math.PI / 4, d, Math.exp(-6) );
        d = m.latDeg();
        assertEquals( 45, d, Math.exp(-6) );
        System.out.println(m);
        System.out.println( m.azOctantName("N", "E", "S", "O") );


        System.out.println( m.angularDistanceTo(h) );
    }
    @Test
    void checkEquatorialCoordinates() {
        double d = 0;
        EquatorialCoordinates e;
        e = EquatorialCoordinates.of(d, d);
        d = e.ra();
        d = e.raDeg();
        d = e.raHr();
        d = e.dec();
        d = e.decDeg();
    }
    @Test
    void checkEclipticCoordinates() {
        double d = 0;
        EclipticCoordinates e;
        e = EclipticCoordinates.of(d, d);
        d = e.lon();
        d = e.lonDeg();
        d = e.lat();
        d = e.latDeg();
    }
}
