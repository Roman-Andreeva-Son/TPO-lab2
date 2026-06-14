package ru.itmo.tpo.lab2.functions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SystemFunctionTest {
    private static final double EPS = 1e-12;
    private static final double DELTA = 1e-6;

    private static final double PI = 3.14159265358979323846;
    private static final double HALF_PI = PI / 2.0;
    private static final double SQRT2_HALF = 0.7071067811865475;

    private static final double LOG3_25 = 2.9299470414358537;
    private static final double LOG5_25 = 2.0;
    private static final double LOG10_25 = 1.3979400086720377;

    private static final double LOG3_HALF = -0.6309297535714574;
    private static final double LOG5_HALF = -0.43067655807339306;
    private static final double LOG10_HALF = -0.3010299956639812;

    @Test
    @DisplayName("system: x < 0, обычная точка тригонометрической ветки")
    void negativeBranchRegularClass() {
        double x = -PI / 4.0;

        SystemFunction system = new SystemFunction(
                stub(Map.of(x, -1.0)),
                stub(Map.of(x, -SQRT2_HALF)),
                stub(Map.of(x, SQRT2_HALF)),
                unused(),
                unused(),
                unused()
        );

        double expected = negativeFormula(-1.0, -SQRT2_HALF, SQRT2_HALF);
        double actual = system.calculate(x, EPS);

        assertAll(
                () -> assertEquals(expected, actual, DELTA),
                () -> assertTrue(actual > 0.0),
                () -> assertFalse(Double.isNaN(actual)),
                () -> assertFalse(Double.isInfinite(actual))
        );
    }

    @Test
    @DisplayName("system: x = 0, граница ветвления и полюс cot/sin")
    void zeroBoundaryClass() {
        double x = 0.0;

        SystemFunction system = new SystemFunction(
                stub(Map.of(x, Double.POSITIVE_INFINITY)),
                stub(Map.of(x, 0.0)),
                stub(Map.of(x, 1.0)),
                unused(),
                unused(),
                unused()
        );

        double actual = system.calculate(x, EPS);

        assertAll(
                () -> assertTrue(Double.isInfinite(actual)),
                () -> assertEquals(Double.POSITIVE_INFINITY, actual)
        );
    }

    @Test
    @DisplayName("system: x < 0, cos(x) = 0 в знаменателе")
    void negativeBranchCosZeroClass() {
        double x = -HALF_PI;

        SystemFunction system = new SystemFunction(
                stub(Map.of(x, 0.0)),
                stub(Map.of(x, -1.0)),
                stub(Map.of(x, 0.0)),
                unused(),
                unused(),
                unused()
        );

        double actual = system.calculate(x, EPS);

        assertAll(
                () -> assertTrue(Double.isInfinite(actual)),
                () -> assertEquals(Double.POSITIVE_INFINITY, actual)
        );
    }

    @Test
    @DisplayName("system: x > 0, обычная точка логарифмической ветки")
    void positiveBranchRegularGreaterThanOneClass() {
        double x = 25.0;

        SystemFunction system = new SystemFunction(
                unused(),
                unused(),
                unused(),
                stub(Map.of(x, LOG3_25)),
                stub(Map.of(x, LOG5_25)),
                stub(Map.of(x, LOG10_25))
        );

        double expected = positiveFormula(LOG3_25, LOG5_25, LOG10_25);
        double actual = system.calculate(x, EPS);

        assertAll(
                () -> assertEquals(expected, actual, DELTA),
                () -> assertTrue(actual > 0.0),
                () -> assertFalse(Double.isNaN(actual)),
                () -> assertFalse(Double.isInfinite(actual))
        );
    }

    @Test
    @DisplayName("system: 0 < x < 1, все логарифмы отрицательные")
    void positiveBranchBetweenZeroAndOneClass() {
        double x = 0.5;

        SystemFunction system = new SystemFunction(
                unused(),
                unused(),
                unused(),
                stub(Map.of(x, LOG3_HALF)),
                stub(Map.of(x, LOG5_HALF)),
                stub(Map.of(x, LOG10_HALF))
        );

        double expected = positiveFormula(LOG3_HALF, LOG5_HALF, LOG10_HALF);
        double actual = system.calculate(x, EPS);

        assertAll(
                () -> assertEquals(expected, actual, DELTA),
                () -> assertTrue(actual > 0.0),
                () -> assertFalse(Double.isNaN(actual)),
                () -> assertFalse(Double.isInfinite(actual))
        );
    }

    @Test
    @DisplayName("system: x = 1, log3(1) = log5(1) = log10(1) = 0")
    void positiveBranchOneClass() {
        double x = 1.0;

        SystemFunction system = new SystemFunction(
                unused(),
                unused(),
                unused(),
                stub(Map.of(x, 0.0)),
                stub(Map.of(x, 0.0)),
                stub(Map.of(x, 0.0))
        );

        double actual = system.calculate(x, EPS);

        assertAll(
                () -> assertTrue(Double.isNaN(actual)),
                () -> assertFalse(Double.isInfinite(actual))
        );
    }

    @Test
    @DisplayName("system: NaN")
    void nanArgumentClass() {
        SystemFunction system = new SystemFunction(
                unused(),
                unused(),
                unused(),
                unused(),
                unused(),
                unused()
        );

        assertAll(
                () -> assertTrue(Double.isNaN(system.calculate(Double.NaN, EPS))),
                () -> assertThrows(IllegalArgumentException.class, () -> system.calculate(1.0, 0.0)),
                () -> assertThrows(IllegalArgumentException.class, () -> system.calculate(1.0, -1e-6)),
                () -> assertThrows(IllegalArgumentException.class, () -> system.calculate(1.0, Double.NaN))
        );
    }

    private static double negativeFormula(double cotX, double sinX, double cosX) {
        return ((((cotX + sinX) + cotX) - cosX) / cosX) / sinX;
    }

    private static double positiveFormula(double log3X, double log5X, double log10X) {
        double ratio = log10X / log3X;
        double ratioSquared = ratio * ratio;
        double ratioFourth = ratioSquared * ratioSquared;
        return (ratioFourth + ((log5X + log10X) * log5X)) * (log5X * log10X);
    }

    private static MathFunction stub(Map<Double, Double> values) {
        return (x, eps) -> {
            for (Map.Entry<Double, Double> entry : values.entrySet()) {
                if (abs(entry.getKey() - x) <= 1e-9) {
                    return entry.getValue();
                }
            }
            throw new IllegalArgumentException("No stub value for x=" + x);
        };
    }

    private static MathFunction unused() {
        return (x, eps) -> {
            throw new AssertionError("This dependency must not be called");
        };
    }

    private static double abs(double value) {
        return value < 0.0 ? -value : value;
    }
}