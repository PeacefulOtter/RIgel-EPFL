package ch.epfl.sigcheck_part4;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.*;

import java.time.ZonedDateTime;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


final class SignatureChecks_4 {
    void checkCartesianCoordinates() {
        double d = 0;
        CartesianCoordinates c = CartesianCoordinates.of(d, d);
        d = c.x();
        d = c.y();
    }

    void checkStereographicProjection() {
        HorizontalCoordinates h = null;
        StereographicProjection s;
        CartesianCoordinates c;
        double d;
        s = new StereographicProjection(h);
        c = s.circleCenterForParallel(h);
        d = s.circleRadiusForParallel(h);
        d = s.applyToAngle(d);
        c = s.apply(h);
        h = s.inverseApply(c);
    }

    void checkCelestialObject() {
        CelestialObject c = null;
        String s;
        double d;
        EquatorialCoordinates e;
        s = c.name();
        d = c.angularSize();
        d = c.magnitude();
        e = c.equatorialPos();
        s = c.info();
    }

    void checkSun() {
        CelestialObject c;
        Sun s;
        EclipticCoordinates e = null;
        EquatorialCoordinates q = null;
        float f = 0f;
        double d;
        s = new Sun(e, q, f, f);
        e = s.eclipticPos();
        d = s.meanAnomaly();
        c = s;
    }

    void checkMoon() {
        CelestialObject c;
        Moon m;
        EquatorialCoordinates e = null;
        float f = 0f;
        m = new Moon(e, f, f, f);
        c = m;
    }

    void checkPlanet() {
        CelestialObject c;
        Planet m;
        EquatorialCoordinates e = null;
        String s = null;
        float f = 0f;
        m = new Planet(s, e, f, f);
        c = m;
    }

    @Test
    void applyWorks(){
        HorizontalCoordinates h1 = HorizontalCoordinates.of(Math.PI/4, Math.PI/6);
        HorizontalCoordinates center1 = HorizontalCoordinates.of(0,0);
        StereographicProjection e = new StereographicProjection(center1);
        double p = Math.sqrt(6);
        CartesianCoordinates a1 = CartesianCoordinates.of(p/(4+p), 2/(4+p));
        CartesianCoordinates c1 = e.apply(h1);
        assertEquals(a1.x(), c1.x(), 1e-8);
        assertEquals(a1.y(), c1.y(), 1e-8);

        HorizontalCoordinates h2 = HorizontalCoordinates.of(Math.PI/2, Math.PI/2);
        HorizontalCoordinates center2 = HorizontalCoordinates.of(Math.PI/4, Math.PI/4);
        StereographicProjection e2 = new StereographicProjection(center2);
        double p2 = Math.sqrt(2);
        CartesianCoordinates a2 = CartesianCoordinates.of(0, p2/(2+p2));
        CartesianCoordinates c2 = e2.apply(h2);
        assertEquals(a2.x(), c2.x(), 1e-8);
        assertEquals(a2.y(), c2.y(), 1e-8);
    }

    @Test
    void circleCenterForParallelWorks(){
        HorizontalCoordinates h1 = HorizontalCoordinates.of(Math.PI/4, Math.PI/6);
        HorizontalCoordinates center1 = HorizontalCoordinates.of(0,0);
        StereographicProjection s = new StereographicProjection(center1);
        CartesianCoordinates a1 = s.circleCenterForParallel(h1);
        assertEquals(0, a1.x(), 1e-10);
        assertEquals(2, a1.y(), 1e-10);
    }

    @Test
    void circleRadiusForParallelWorks(){
        HorizontalCoordinates h2 = HorizontalCoordinates.of(Math.PI/2, Math.PI/2);
        HorizontalCoordinates center2 = HorizontalCoordinates.of(Math.PI/4, Math.PI/4);
        StereographicProjection e2 = new StereographicProjection(center2);
        double rho1 = e2.circleRadiusForParallel(h2);
        assertEquals(0, rho1, 1e-10);
    }

    @Test
    void applyToAngle(){
        HorizontalCoordinates center2 = HorizontalCoordinates.of(Math.PI/4, Math.PI/4);
        StereographicProjection e2 = new StereographicProjection(center2);
        double z = e2.applyToAngle(Math.PI/2);
        System.out.println(z);
    }

}
