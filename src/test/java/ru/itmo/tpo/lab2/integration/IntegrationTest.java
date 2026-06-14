package ru.itmo.tpo.lab2.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.itmo.tpo.lab2.functions.CosFunction;
import ru.itmo.tpo.lab2.functions.CotFunction;
import ru.itmo.tpo.lab2.functions.FunctionFactory;
import ru.itmo.tpo.lab2.functions.LnFunction;
import ru.itmo.tpo.lab2.functions.LogBaseFunction;
import ru.itmo.tpo.lab2.functions.MathFunction;
import ru.itmo.tpo.lab2.functions.SinFunction;
import ru.itmo.tpo.lab2.functions.SystemFunction;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class IntegrationTest {
    private static final double EPS = 1e-12;
    private static final double DELTA = 1e-6;

    private static final double PI = 3.14159265358979323846;
    private static final double SQRT2_HALF = 0.7071067811865475;

    private static final double X_NEGATIVE = -PI / 4.0;
    private static final double X_POSITIVE = 25.0;

    @Test
    @DisplayName("integration step 1: real sin, cot/cos via Mockito")
    void step1RealSin() {
        MathFunction realSin = new SinFunction();
        MathFunction cotMock = mock(MathFunction.class);
        MathFunction cosMock = mock(MathFunction.class);
        MathFunction log3Mock = mock(MathFunction.class);
        MathFunction log5Mock = mock(MathFunction.class);
        MathFunction log10Mock = mock(MathFunction.class);

        when(cotMock.calculate(X_NEGATIVE, EPS)).thenReturn(-1.0);
        when(cosMock.calculate(X_NEGATIVE, EPS)).thenReturn(SQRT2_HALF);

        SystemFunction system = new SystemFunction(
                cotMock,
                realSin,
                cosMock,
                log3Mock,
                log5Mock,
                log10Mock
        );

        double expected = negativeFormula(
                -1.0,
                realSin.calculate(X_NEGATIVE, EPS),
                SQRT2_HALF
        );

        double actual = system.calculate(X_NEGATIVE, EPS);

        assertAll(
                () -> assertEquals(expected, actual, DELTA),
                () -> assertTrue(actual > 0.0),
                () -> assertFinite(actual)
        );
        verifyNoInteractions(log3Mock, log5Mock, log10Mock);
    }

    @Test
    @DisplayName("integration step 2: real sin + real cos, cot via Mockito")
    void step2RealSinAndCos() {
        MathFunction realSin = new SinFunction();
        MathFunction realCos = new CosFunction(realSin);
        MathFunction cotMock = mock(MathFunction.class);
        MathFunction log3Mock = mock(MathFunction.class);
        MathFunction log5Mock = mock(MathFunction.class);
        MathFunction log10Mock = mock(MathFunction.class);

        when(cotMock.calculate(X_NEGATIVE, EPS)).thenReturn(-1.0);

        SystemFunction system = new SystemFunction(
                cotMock,
                realSin,
                realCos,
                log3Mock,
                log5Mock,
                log10Mock
        );

        double expected = negativeFormula(
                -1.0,
                realSin.calculate(X_NEGATIVE, EPS),
                realCos.calculate(X_NEGATIVE, EPS)
        );

        double actual = system.calculate(X_NEGATIVE, EPS);

        assertAll(
                () -> assertEquals(expected, actual, DELTA),
                () -> assertTrue(actual > 0.0),
                () -> assertFinite(actual)
        );
        verifyNoInteractions(log3Mock, log5Mock, log10Mock);
    }

    @Test
    @DisplayName("integration step 3: full real trigonometric branch")
    void step3FullTrigBranch() {
        MathFunction realSin = new SinFunction();
        MathFunction realCos = new CosFunction(realSin);
        MathFunction realCot = new CotFunction(realSin, realCos);
        MathFunction log3Mock = mock(MathFunction.class);
        MathFunction log5Mock = mock(MathFunction.class);
        MathFunction log10Mock = mock(MathFunction.class);

        SystemFunction system = new SystemFunction(
                realCot,
                realSin,
                realCos,
                log3Mock,
                log5Mock,
                log10Mock
        );

        double expected = negativeFormula(
                realCot.calculate(X_NEGATIVE, EPS),
                realSin.calculate(X_NEGATIVE, EPS),
                realCos.calculate(X_NEGATIVE, EPS)
        );

        double actual = system.calculate(X_NEGATIVE, EPS);

        assertAll(
                () -> assertEquals(expected, actual, DELTA),
                () -> assertEquals(6.82842712474619, actual, DELTA),
                () -> assertTrue(actual > 0.0),
                () -> assertFinite(actual)
        );
        verifyNoInteractions(log3Mock, log5Mock, log10Mock);
    }

    @Test
    @DisplayName("integration step 4: full real logarithmic branch")
    void step4FullLogBranch() {
        MathFunction ln = new LnFunction();
        MathFunction realLog3 = new LogBaseFunction(3.0, ln);
        MathFunction realLog5 = new LogBaseFunction(5.0, ln);
        MathFunction realLog10 = new LogBaseFunction(10.0, ln);
        MathFunction cotMock = mock(MathFunction.class);
        MathFunction sinMock = mock(MathFunction.class);
        MathFunction cosMock = mock(MathFunction.class);

        SystemFunction system = new SystemFunction(
                cotMock,
                sinMock,
                cosMock,
                realLog3,
                realLog5,
                realLog10
        );

        double expected = positiveFormula(
                realLog3.calculate(X_POSITIVE, EPS),
                realLog5.calculate(X_POSITIVE, EPS),
                realLog10.calculate(X_POSITIVE, EPS)
        );

        double actual = system.calculate(X_POSITIVE, EPS);

        assertAll(
                () -> assertEquals(expected, actual, DELTA),
                () -> assertEquals(19.145353530459897, actual, DELTA),
                () -> assertTrue(actual > 0.0),
                () -> assertFinite(actual)
        );
        verifyNoInteractions(cotMock, sinMock, cosMock);
    }

    @Test
    @DisplayName("integration step 5: full real system and extra equivalence classes")
    void step5FullSystem() {
        SystemFunction system = FunctionFactory.createSystemFunction();

        double[] negativePoints = {
                -PI / 4.0,
                -3.0 * PI / 4.0,
                -5.0 * PI / 4.0,
                -7.0 * PI / 4.0
        };
        for (double x : negativePoints) {
            assertFinite(system.calculate(x, EPS));
        }

        assertEquals(6.82842712474619, system.calculate(-PI / 4.0, EPS), DELTA);
        assertEquals(
                system.calculate(-PI / 4.0, EPS),
                system.calculate(-PI / 4.0 - 2.0 * PI, EPS),
                DELTA
        );

        assertTrigBreakHasDifferentSigns(system, -PI / 2.0);
        assertTrigBreakHasDifferentSigns(system, -3.0 * PI / 2.0);
        assertTrigSinBreakIsLarge(system, -PI);

        double[] positivePoints = {0.5, 2.0, 25.0};
        for (double x : positivePoints) {
            assertPositiveFinite(system.calculate(x, EPS));
        }

        assertEquals(19.145353530459897, system.calculate(X_POSITIVE, EPS), DELTA);

        assertTrue(Double.isNaN(system.calculate(1.0, EPS)));
        assertPositiveFiniteAndLessThan(system.calculate(0.999, EPS), 1e-6);
        assertPositiveFiniteAndLessThan(system.calculate(1.001, EPS), 1e-6);
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

    private static void assertTrigBreakHasDifferentSigns(SystemFunction system, double breakPoint) {
        double left = system.calculate(breakPoint - 1e-3, EPS);
        double right = system.calculate(breakPoint + 1e-3, EPS);

        assertAll(
                () -> assertFinite(left),
                () -> assertFinite(right),
                () -> assertTrue(abs(left) > 100.0),
                () -> assertTrue(abs(right) > 100.0),
                () -> assertTrue(left * right < 0.0)
        );
    }

    private static void assertTrigSinBreakIsLarge(SystemFunction system, double breakPoint) {
        double left = system.calculate(breakPoint - 1e-3, EPS);
        double right = system.calculate(breakPoint + 1e-3, EPS);

        assertAll(
                () -> assertFinite(left),
                () -> assertFinite(right),
                () -> assertTrue(abs(left) > 100_000.0),
                () -> assertTrue(abs(right) > 100_000.0)
        );
    }

    private static void assertPositiveFinite(double value) {
        assertAll(
                () -> assertTrue(value > 0.0),
                () -> assertFinite(value)
        );
    }

    private static void assertPositiveFiniteAndLessThan(double value, double upperBound) {
        assertAll(
                () -> assertPositiveFinite(value),
                () -> assertTrue(value < upperBound)
        );
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
