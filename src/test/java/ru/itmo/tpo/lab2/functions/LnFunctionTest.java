package ru.itmo.tpo.lab2.functions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LnFunctionTest {
    private static final double EPS = 1e-12;
    private static final double DELTA = 1e-6;

    private static final double E = 2.71828182845904523536;
    private static final double LN_2 = 0.6931471805599453;
    private static final double LN_3 = 1.0986122886681098;
    private static final double LN_10 = 2.302585092994046;
    private static final double LN_25 = 3.2188758248682006;

    private final LnFunction ln = new LnFunction();

    @Test
    @DisplayName("ln: отрицательная область определения")
    void negativeArgumentClass() {
        assertAll(
                () -> assertTrue(Double.isNaN(ln.calculate(-1.0, EPS))),
                () -> assertTrue(Double.isNaN(ln.calculate(-100.0, EPS))),
                () -> assertTrue(Double.isNaN(ln.calculate(Double.NEGATIVE_INFINITY, EPS)))
        );
    }

    @Test
    @DisplayName("ln: граница области определения x = 0")
    void zeroArgumentClass() {
        double result = ln.calculate(0.0, EPS);

        assertAll(
                () -> assertTrue(Double.isInfinite(result)),
                () -> assertEquals(Double.NEGATIVE_INFINITY, result)
        );
    }

    @Test
    @DisplayName("ln: область 0 < x < 1")
    void betweenZeroAndOneClass() {
        assertAll(
                () -> assertTrue(ln.calculate(0.5, EPS) < 0.0),
                () -> assertTrue(ln.calculate(0.1, EPS) < 0.0),
                () -> assertEquals(-LN_2, ln.calculate(0.5, EPS), DELTA),
                () -> assertEquals(-LN_10, ln.calculate(0.1, EPS), DELTA)
        );
    }

    @Test
    @DisplayName("ln: особая точка x = 1")
    void oneArgumentClass() {
        assertAll(
                () -> assertEquals(0.0, ln.calculate(1.0, EPS), DELTA),
                () -> assertFalse(Double.isNaN(ln.calculate(1.0, EPS))),
                () -> assertFalse(Double.isInfinite(ln.calculate(1.0, EPS)))
        );
    }

    @Test
    @DisplayName("ln: область x > 1")
    void greaterThanOneClass() {
        assertAll(
                () -> assertTrue(ln.calculate(2.0, EPS) > 0.0),
                () -> assertTrue(ln.calculate(10.0, EPS) > 0.0),
                () -> assertEquals(LN_2, ln.calculate(2.0, EPS), DELTA),
                () -> assertEquals(LN_3, ln.calculate(3.0, EPS), DELTA),
                () -> assertEquals(LN_10, ln.calculate(10.0, EPS), DELTA)
        );
    }

    @Test
    @DisplayName("ln: ln(e) = 1")
    void eArgumentClass() {
        assertAll(
                () -> assertEquals(1.0, ln.calculate(E, EPS), DELTA),
                () -> assertTrue(ln.calculate(E, EPS) > 0.0)
        );
    }

    @Test
    @DisplayName("ln: монотонность на положительной области")
    void monotonicityClass() {
        double y1 = ln.calculate(0.5, EPS);
        double y2 = ln.calculate(1.0, EPS);
        double y3 = ln.calculate(2.0, EPS);
        double y4 = ln.calculate(10.0, EPS);

        assertAll(
                () -> assertTrue(y1 < y2),
                () -> assertTrue(y2 < y3),
                () -> assertTrue(y3 < y4)
        );
    }

    @Test
    @DisplayName("ln: взаимно обратные аргументы")
    void reciprocalArgumentsClass() {
        double ln2 = ln.calculate(2.0, EPS);
        double lnHalf = ln.calculate(0.5, EPS);

        assertAll(
                () -> assertEquals(0.0, ln2 + lnHalf, DELTA),
                () -> assertEquals(-ln2, lnHalf, DELTA)
        );
    }

    @Test
    @DisplayName("ln: аргументы, требующие приведения к окрестности 1")
    void reducedArgumentClass() {
        assertAll(
                () -> assertEquals(LN_25, ln.calculate(25.0, EPS), DELTA),
                () -> assertEquals(-LN_10, ln.calculate(0.1, EPS), DELTA)
        );
    }

    @Test
    @DisplayName("ln: NaN и +Infinity")
    void specialArgumentClass() {
        assertAll(
                () -> assertTrue(Double.isNaN(ln.calculate(Double.NaN, EPS))),
                () -> assertEquals(Double.POSITIVE_INFINITY, ln.calculate(Double.POSITIVE_INFINITY, EPS))
        );
    }

    @Test
    @DisplayName("ln: разные значения точности eps")
    void differentEpsilonClass() {
        double rough = ln.calculate(10.0, 1e-4);
        double precise = ln.calculate(10.0, 1e-12);

        assertAll(
                () -> assertEquals(LN_10, rough, 1e-3),
                () -> assertEquals(LN_10, precise, DELTA),
                () -> assertEquals(rough, precise, 1e-3)
        );
    }

    @Test
    @DisplayName("ln: некорректный eps")
    void invalidEpsilonClass() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> ln.calculate(2.0, 0.0)),
                () -> assertThrows(IllegalArgumentException.class, () -> ln.calculate(2.0, -1e-6)),
                () -> assertThrows(IllegalArgumentException.class, () -> ln.calculate(2.0, Double.NaN)),
                () -> assertThrows(IllegalArgumentException.class, () -> ln.calculate(2.0, Double.POSITIVE_INFINITY))
        );
    }
}