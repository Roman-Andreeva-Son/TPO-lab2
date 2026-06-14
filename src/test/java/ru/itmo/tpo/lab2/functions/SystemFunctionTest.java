package ru.itmo.tpo.lab2.functions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

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
    @DisplayName("system: x < 0, regular trigonometric branch point")
    void negativeBranchRegularClass() {
        double x = -PI / 4.0;
        MathFunction cot = mock(MathFunction.class);
        MathFunction sin = mock(MathFunction.class);
        MathFunction cos = mock(MathFunction.class);
        MathFunction log3 = mock(MathFunction.class);
        MathFunction log5 = mock(MathFunction.class);
        MathFunction log10 = mock(MathFunction.class);

        when(cot.calculate(x, EPS)).thenReturn(-1.0);
        when(sin.calculate(x, EPS)).thenReturn(-SQRT2_HALF);
        when(cos.calculate(x, EPS)).thenReturn(SQRT2_HALF);

        SystemFunction system = new SystemFunction(cot, sin, cos, log3, log5, log10);

        double expected = negativeFormula(-1.0, -SQRT2_HALF, SQRT2_HALF);
        double actual = system.calculate(x, EPS);

        assertAll(
                () -> assertEquals(expected, actual, DELTA),
                () -> assertTrue(actual > 0.0),
                () -> assertFinite(actual)
        );
    }

    @Test
    @DisplayName("system: x = 0, branch boundary and cot/sin pole")
    void zeroBoundaryClass() {
        double x = 0.0;
        MathFunction cot = mock(MathFunction.class);
        MathFunction sin = mock(MathFunction.class);
        MathFunction cos = mock(MathFunction.class);
        MathFunction log3 = mock(MathFunction.class);
        MathFunction log5 = mock(MathFunction.class);
        MathFunction log10 = mock(MathFunction.class);

        when(cot.calculate(x, EPS)).thenReturn(Double.POSITIVE_INFINITY);
        when(sin.calculate(x, EPS)).thenReturn(0.0);
        when(cos.calculate(x, EPS)).thenReturn(1.0);

        double actual = new SystemFunction(cot, sin, cos, log3, log5, log10).calculate(x, EPS);

        assertAll(
                () -> assertTrue(Double.isInfinite(actual)),
                () -> assertEquals(Double.POSITIVE_INFINITY, actual)
        );
    }

    @Test
    @DisplayName("system: x < 0, cos(x) = 0 in denominator")
    void negativeBranchCosZeroClass() {
        double x = -HALF_PI;
        MathFunction cot = mock(MathFunction.class);
        MathFunction sin = mock(MathFunction.class);
        MathFunction cos = mock(MathFunction.class);
        MathFunction log3 = mock(MathFunction.class);
        MathFunction log5 = mock(MathFunction.class);
        MathFunction log10 = mock(MathFunction.class);

        when(cot.calculate(x, EPS)).thenReturn(0.0);
        when(sin.calculate(x, EPS)).thenReturn(-1.0);
        when(cos.calculate(x, EPS)).thenReturn(0.0);

        double actual = new SystemFunction(cot, sin, cos, log3, log5, log10).calculate(x, EPS);

        assertAll(
                () -> assertTrue(Double.isInfinite(actual)),
                () -> assertEquals(Double.POSITIVE_INFINITY, actual)
        );
    }

    @Test
    @DisplayName("system: x > 0, regular logarithmic branch point")
    void positiveBranchRegularGreaterThanOneClass() {
        double x = 25.0;
        MathFunction cot = mock(MathFunction.class);
        MathFunction sin = mock(MathFunction.class);
        MathFunction cos = mock(MathFunction.class);
        MathFunction log3 = mock(MathFunction.class);
        MathFunction log5 = mock(MathFunction.class);
        MathFunction log10 = mock(MathFunction.class);

        when(log3.calculate(x, EPS)).thenReturn(LOG3_25);
        when(log5.calculate(x, EPS)).thenReturn(LOG5_25);
        when(log10.calculate(x, EPS)).thenReturn(LOG10_25);

        double expected = positiveFormula(LOG3_25, LOG5_25, LOG10_25);
        double actual = new SystemFunction(cot, sin, cos, log3, log5, log10).calculate(x, EPS);

        assertAll(
                () -> assertEquals(expected, actual, DELTA),
                () -> assertTrue(actual > 0.0),
                () -> assertFinite(actual)
        );
    }

    @Test
    @DisplayName("system: 0 < x < 1, all logarithms are negative")
    void positiveBranchBetweenZeroAndOneClass() {
        double x = 0.5;
        MathFunction cot = mock(MathFunction.class);
        MathFunction sin = mock(MathFunction.class);
        MathFunction cos = mock(MathFunction.class);
        MathFunction log3 = mock(MathFunction.class);
        MathFunction log5 = mock(MathFunction.class);
        MathFunction log10 = mock(MathFunction.class);

        when(log3.calculate(x, EPS)).thenReturn(LOG3_HALF);
        when(log5.calculate(x, EPS)).thenReturn(LOG5_HALF);
        when(log10.calculate(x, EPS)).thenReturn(LOG10_HALF);

        double expected = positiveFormula(LOG3_HALF, LOG5_HALF, LOG10_HALF);
        double actual = new SystemFunction(cot, sin, cos, log3, log5, log10).calculate(x, EPS);

        assertAll(
                () -> assertEquals(expected, actual, DELTA),
                () -> assertTrue(actual > 0.0),
                () -> assertFinite(actual)
        );
    }

    @Test
    @DisplayName("system: x = 1, log values are zero and result is NaN")
    void positiveBranchOneClass() {
        double x = 1.0;
        MathFunction cot = mock(MathFunction.class);
        MathFunction sin = mock(MathFunction.class);
        MathFunction cos = mock(MathFunction.class);
        MathFunction log3 = mock(MathFunction.class);
        MathFunction log5 = mock(MathFunction.class);
        MathFunction log10 = mock(MathFunction.class);

        when(log3.calculate(x, EPS)).thenReturn(0.0);
        when(log5.calculate(x, EPS)).thenReturn(0.0);
        when(log10.calculate(x, EPS)).thenReturn(0.0);

        double actual = new SystemFunction(cot, sin, cos, log3, log5, log10).calculate(x, EPS);

        assertAll(
                () -> assertTrue(Double.isNaN(actual)),
                () -> assertFalse(Double.isInfinite(actual))
        );
        verifyNoInteractions(cot, sin, cos);
    }

    @Test
    @DisplayName("system: NaN argument and invalid eps")
    void nanArgumentClass() {
        MathFunction cot = mock(MathFunction.class);
        MathFunction sin = mock(MathFunction.class);
        MathFunction cos = mock(MathFunction.class);
        MathFunction log3 = mock(MathFunction.class);
        MathFunction log5 = mock(MathFunction.class);
        MathFunction log10 = mock(MathFunction.class);

        SystemFunction system = new SystemFunction(cot, sin, cos, log3, log5, log10);

        assertAll(
                () -> assertTrue(Double.isNaN(system.calculate(Double.NaN, EPS))),
                () -> assertThrows(IllegalArgumentException.class, () -> system.calculate(1.0, 0.0)),
                () -> assertThrows(IllegalArgumentException.class, () -> system.calculate(1.0, -1e-6)),
                () -> assertThrows(IllegalArgumentException.class, () -> system.calculate(1.0, Double.NaN))
        );
        verifyNoInteractions(cot, sin, cos, log3, log5, log10);
    }

    @Test
    @DisplayName("system: x < 0 calls only trigonometric dependencies")
    void negativeBranchUsesOnlyTrigonometricDependencies() {
        double x = -PI / 4.0;
        MathFunction cot = mock(MathFunction.class);
        MathFunction sin = mock(MathFunction.class);
        MathFunction cos = mock(MathFunction.class);
        MathFunction log3 = mock(MathFunction.class);
        MathFunction log5 = mock(MathFunction.class);
        MathFunction log10 = mock(MathFunction.class);

        when(cot.calculate(x, EPS)).thenReturn(-1.0);
        when(sin.calculate(x, EPS)).thenReturn(-SQRT2_HALF);
        when(cos.calculate(x, EPS)).thenReturn(SQRT2_HALF);

        double actual = new SystemFunction(cot, sin, cos, log3, log5, log10).calculate(x, EPS);

        assertEquals(negativeFormula(-1.0, -SQRT2_HALF, SQRT2_HALF), actual, DELTA);
        verify(cot).calculate(x, EPS);
        verify(sin).calculate(x, EPS);
        verify(cos).calculate(x, EPS);
        verifyNoInteractions(log3, log5, log10);
    }

    @Test
    @DisplayName("system: x > 0 calls only logarithmic dependencies")
    void positiveBranchUsesOnlyLogarithmicDependencies() {
        double x = 25.0;
        MathFunction cot = mock(MathFunction.class);
        MathFunction sin = mock(MathFunction.class);
        MathFunction cos = mock(MathFunction.class);
        MathFunction log3 = mock(MathFunction.class);
        MathFunction log5 = mock(MathFunction.class);
        MathFunction log10 = mock(MathFunction.class);

        when(log3.calculate(x, EPS)).thenReturn(LOG3_25);
        when(log5.calculate(x, EPS)).thenReturn(LOG5_25);
        when(log10.calculate(x, EPS)).thenReturn(LOG10_25);

        double actual = new SystemFunction(cot, sin, cos, log3, log5, log10).calculate(x, EPS);

        assertEquals(positiveFormula(LOG3_25, LOG5_25, LOG10_25), actual, DELTA);
        verify(log3).calculate(x, EPS);
        verify(log5).calculate(x, EPS);
        verify(log10).calculate(x, EPS);
        verifyNoInteractions(cot, sin, cos);
    }

    @Test
    @DisplayName("system: values near -pi/2 have different signs")
    void nearCosDiscontinuityValuesHaveDifferentSigns() {
        double left = -HALF_PI - 1e-3;
        double right = -HALF_PI + 1e-3;
        MathFunction cot = mock(MathFunction.class);
        MathFunction sin = mock(MathFunction.class);
        MathFunction cos = mock(MathFunction.class);
        MathFunction log3 = mock(MathFunction.class);
        MathFunction log5 = mock(MathFunction.class);
        MathFunction log10 = mock(MathFunction.class);

        stubTrig(cot, sin, cos, left);
        stubTrig(cot, sin, cos, right);

        SystemFunction system = new SystemFunction(cot, sin, cos, log3, log5, log10);
        double leftResult = system.calculate(left, EPS);
        double rightResult = system.calculate(right, EPS);

        assertAll(
                () -> assertFinite(leftResult),
                () -> assertFinite(rightResult),
                () -> assertTrue(abs(leftResult) > 100.0),
                () -> assertTrue(abs(rightResult) > 100.0),
                () -> assertTrue(leftResult * rightResult < 0.0)
        );
    }

    @Test
    @DisplayName("system: values near -pi are large by absolute value")
    void nearSinDiscontinuityValuesAreLarge() {
        double left = -PI - 1e-3;
        double right = -PI + 1e-3;
        MathFunction cot = mock(MathFunction.class);
        MathFunction sin = mock(MathFunction.class);
        MathFunction cos = mock(MathFunction.class);
        MathFunction log3 = mock(MathFunction.class);
        MathFunction log5 = mock(MathFunction.class);
        MathFunction log10 = mock(MathFunction.class);

        stubTrig(cot, sin, cos, left);
        stubTrig(cot, sin, cos, right);

        SystemFunction system = new SystemFunction(cot, sin, cos, log3, log5, log10);
        double leftResult = system.calculate(left, EPS);
        double rightResult = system.calculate(right, EPS);

        assertAll(
                () -> assertFinite(leftResult),
                () -> assertFinite(rightResult),
                () -> assertTrue(abs(leftResult) > 100_000.0),
                () -> assertTrue(abs(rightResult) > 100_000.0)
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

    private static void stubTrig(MathFunction cot, MathFunction sin, MathFunction cos, double x) {
        double sinX = Math.sin(x);
        double cosX = Math.cos(x);

        when(cot.calculate(x, EPS)).thenReturn(cosX / sinX);
        when(sin.calculate(x, EPS)).thenReturn(sinX);
        when(cos.calculate(x, EPS)).thenReturn(cosX);
    }

    private static void assertFinite(double value) {
        assertAll(
                () -> assertFalse(Double.isNaN(value)),
                () -> assertFalse(Double.isInfinite(value))
        );
    }

    private static double abs(double value) {
        return value < 0.0 ? -value : value;
    }
}
