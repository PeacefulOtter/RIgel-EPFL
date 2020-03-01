package ch.epfl.sigcheck;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.math.Angle;
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
        assertThrows( IllegalArgumentException.class, () -> {EquatorialCoordinates.of(Angle.TAU, Angle.TAU/5); } );
        assertThrows( IllegalArgumentException.class, () -> {EquatorialCoordinates.of(0, Angle.TAU/2); } );
        assertThrows( IllegalArgumentException.class, () -> {EquatorialCoordinates.of(Math.PI * 3, 2); } );
        assertThrows( IllegalArgumentException.class, () -> {EquatorialCoordinates.of(0, 500 ); } );

        EquatorialCoordinates e;
        e = EquatorialCoordinates.of(Angle.TAU/2, Angle.TAU/6);
        d = e.ra();
        assertEquals( Math.PI , d, Math.exp(-6) );
        d = e.raDeg();
        assertEquals( 180, d, Math.exp(-6) );
        d = e.raHr();
        assertEquals( 12, d, Math.exp(-6) );
        d = e.dec();
        assertEquals( Math.PI / 3, d, Math.exp(-6) );
        d = e.decDeg();
        assertEquals( 60, d, Math.exp(-6) );
        System.out.print(e);

    }
    @Test
    void checkEclipticCoordinates() {
        double d = 0;

        assertThrows( IllegalArgumentException.class, () -> {EclipticCoordinates.of(Angle.TAU, Angle.TAU/5); } );
        assertThrows( IllegalArgumentException.class, () -> {EclipticCoordinates.of(0, Angle.TAU/2); } );
        assertThrows( IllegalArgumentException.class, () -> {EclipticCoordinates.of(Math.PI * 3, 2); } );
        assertThrows( IllegalArgumentException.class, () -> {EclipticCoordinates.of(0, 500 ); } );

        EclipticCoordinates e;
        e = EclipticCoordinates.of(Angle.TAU/2, Angle.TAU/6);
        d = e.lon();
        assertEquals( Math.PI , d, Math.exp(-6) );
        d = e.lonDeg();
        assertEquals( 180, d, Math.exp(-6) );
        d = e.lat();
        assertEquals( Math.PI / 3, d, Math.exp(-6) );
        d = e.latDeg();
        assertEquals( 60, d, Math.exp(-6) );
        System.out.print(e);
    }
}
