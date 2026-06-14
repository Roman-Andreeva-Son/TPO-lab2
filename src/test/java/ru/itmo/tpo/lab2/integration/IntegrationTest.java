package ru.itmo.tpo.lab2.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.itmo.tpo.lab2.functions.*;
import ru.itmo.tpo.lab2.stubs.TableMathFunctionStub;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {
    private static final double EPS = 1e-12;
    private static final double DELTA = 1e-6;

    private static final double PI = 3.14159265358979323846;
    private static final double SQRT2_HALF = 0.7071067811865475;

    private static final double X_NEGATIVE = -PI / 4.0;
    private static final double X_POSITIVE = 25.0;

    @Test
    @DisplayName("integration step 1: real sin, cot/cos — табличные заглушки")
    void step1RealSin() {
        MathFunction realSin = new SinFunction();

        SystemFunction system = new SystemFunction(
                cotStub(),
                realSin,
                cosStub(),
                unused(),
                unused(),
                unused()
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
                () -> assertFalse(Double.isNaN(actual)),
                () -> assertFalse(Double.isInfinite(actual))
        );
    }

    @Test
    @DisplayName("integration step 2: real sin + real cos, cot — табличная заглушка")
    void step2RealSinAndCos() {
        MathFunction realSin = new SinFunction();
        MathFunction realCos = new CosFunction(realSin);

        SystemFunction system = new SystemFunction(
                cotStub(),
                realSin,
                realCos,
                unused(),
                unused(),
                unused()
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
                () -> assertFalse(Double.isNaN(actual)),
                () -> assertFalse(Double.isInfinite(actual))
        );
    }

    @Test
    @DisplayName("integration step 3: полностью реальная тригонометрическая ветка")
    void step3FullTrigBranch() {
        MathFunction realSin = new SinFunction();
        MathFunction realCos = new CosFunction(realSin);
        MathFunction realCot = new CotFunction(realSin, realCos);

        SystemFunction system = new SystemFunction(
                realCot,
                realSin,
                realCos,
                unused(),
                unused(),
                unused()
        );

        double expected = negativeFormula(
                realCot.calculate(X_NEGATIVE, EPS),
                realSin.calculate(X_NEGATIVE, EPS),
                realCos.calculate(X_NEGATIVE, EPS)
        );

        double actual = system.calculate(X_NEGATIVE, EPS);

        assertAll(
                () -> assertEquals(expected, actual, DELTA),
                () -> assertTrue(actual > 0.0),
                () -> assertFalse(Double.isNaN(actual)),
                () -> assertFalse(Double.isInfinite(actual))
        );
    }

    @Test
    @DisplayName("integration step 4: полностью реальная логарифмическая ветка")
    void step4FullLogBranch() {
        MathFunction ln = new LnFunction();
        MathFunction realLog3 = new LogBaseFunction(3.0, ln);
        MathFunction realLog5 = new LogBaseFunction(5.0, ln);
        MathFunction realLog10 = new LogBaseFunction(10.0, ln);

        SystemFunction system = new SystemFunction(
                unused(),
                unused(),
                unused(),
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
                () -> assertTrue(actual > 0.0),
                () -> assertFalse(Double.isNaN(actual)),
                () -> assertFalse(Double.isInfinite(actual))
        );
    }

    @Test
    @DisplayName("integration step 5: полностью реальная система")
    void step5FullSystem() {
        SystemFunction system = FunctionFactory.createSystemFunction();

        double negativeResult = system.calculate(X_NEGATIVE, EPS);
        double positiveResult = system.calculate(X_POSITIVE, EPS);
        double oneResult = system.calculate(1.0, EPS);

        assertAll(
                () -> assertEquals(6.82842712474619, negativeResult, DELTA),
                () -> assertTrue(negativeResult > 0.0),
                () -> assertFalse(Double.isNaN(negativeResult)),
                () -> assertFalse(Double.isInfinite(negativeResult)),

                () -> assertEquals(19.145353530459897, positiveResult, DELTA),
                () -> assertTrue(positiveResult > 0.0),
                () -> assertFalse(Double.isNaN(positiveResult)),
                () -> assertFalse(Double.isInfinite(positiveResult)),

                () -> assertTrue(Double.isNaN(oneResult))
        );
    }

    private static MathFunction cosStub() {
        return TableMathFunctionStub.fromResource("stubs/cos.csv");
    }

    private static MathFunction cotStub() {
        return TableMathFunctionStub.fromResource("stubs/cot.csv");
    }

    private static MathFunction unused() {
        return (x, eps) -> {
            throw new AssertionError("This dependency must not be called");
        };
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
}