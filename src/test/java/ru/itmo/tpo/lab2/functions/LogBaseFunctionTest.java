package ru.itmo.tpo.lab2.functions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LogBaseFunctionTest {
    private static final double EPS = 1e-12;
    private static final double DELTA = 1e-6;

    private final LnFunction ln = new LnFunction();

    @Test
    @DisplayName("log_b: log_b(1) = 0")
    void oneArgumentClass() {
        MathFunction log3 = new LogBaseFunction(3.0, ln);
        MathFunction log5 = new LogBaseFunction(5.0, ln);
        MathFunction log10 = new LogBaseFunction(10.0, ln);

        assertAll(
                () -> assertEquals(0.0, log3.calculate(1.0, EPS), DELTA),
                () -> assertEquals(0.0, log5.calculate(1.0, EPS), DELTA),
                () -> assertEquals(0.0, log10.calculate(1.0, EPS), DELTA)
        );
    }

    @Test
    @DisplayName("log_b: log_b(b) = 1")
    void baseArgumentClass() {
        MathFunction log3 = new LogBaseFunction(3.0, ln);
        MathFunction log5 = new LogBaseFunction(5.0, ln);
        MathFunction log10 = new LogBaseFunction(10.0, ln);

        assertAll(
                () -> assertEquals(1.0, log3.calculate(3.0, EPS), DELTA),
                () -> assertEquals(1.0, log5.calculate(5.0, EPS), DELTA),
                () -> assertEquals(1.0, log10.calculate(10.0, EPS), DELTA)
        );
    }

    @Test
    @DisplayName("log_b: аргумент x > base")
    void greaterThanBaseClass() {
        MathFunction log5 = new LogBaseFunction(5.0, ln);
        MathFunction log10 = new LogBaseFunction(10.0, ln);

        assertAll(
                () -> assertEquals(2.0, log5.calculate(25.0, EPS), DELTA),
                () -> assertEquals(2.0, log10.calculate(100.0, EPS), DELTA),
                () -> assertTrue(log5.calculate(25.0, EPS) > 1.0)
        );
    }

    @Test
    @DisplayName("log_b: область 0 < x < 1 при base > 1")
    void betweenZeroAndOneForBaseGreaterThanOneClass() {
        MathFunction log10 = new LogBaseFunction(10.0, ln);

        assertAll(
                () -> assertEquals(-1.0, log10.calculate(0.1, EPS), DELTA),
                () -> assertTrue(log10.calculate(0.1, EPS) < 0.0)
        );
    }

    @Test
    @DisplayName("log_b: основание 0 < base < 1")
    void baseBetweenZeroAndOneClass() {
        MathFunction logHalf = new LogBaseFunction(0.5, ln);

        assertAll(
                () -> assertEquals(1.0, logHalf.calculate(0.5, EPS), DELTA),
                () -> assertEquals(2.0, logHalf.calculate(0.25, EPS), DELTA),
                () -> assertEquals(-1.0, logHalf.calculate(2.0, EPS), DELTA)
        );
    }

    @Test
    @DisplayName("log_b: монотонность при base > 1")
    void increasingForBaseGreaterThanOneClass() {
        MathFunction log10 = new LogBaseFunction(10.0, ln);

        double y1 = log10.calculate(0.1, EPS);
        double y2 = log10.calculate(1.0, EPS);
        double y3 = log10.calculate(10.0, EPS);
        double y4 = log10.calculate(100.0, EPS);

        assertAll(
                () -> assertTrue(y1 < y2),
                () -> assertTrue(y2 < y3),
                () -> assertTrue(y3 < y4)
        );
    }

    @Test
    @DisplayName("log_b: монотонность при 0 < base < 1")
    void decreasingForBaseBetweenZeroAndOneClass() {
        MathFunction logHalf = new LogBaseFunction(0.5, ln);

        double y1 = logHalf.calculate(0.25, EPS);
        double y2 = logHalf.calculate(0.5, EPS);
        double y3 = logHalf.calculate(2.0, EPS);

        assertAll(
                () -> assertTrue(y1 > y2),
                () -> assertTrue(y2 > y3)
        );
    }

    @Test
    @DisplayName("log_b: некорректное основание")
    void invalidBaseClass() {
        MathFunction negativeBase = new LogBaseFunction(-3.0, ln);
        MathFunction zeroBase = new LogBaseFunction(0.0, ln);
        MathFunction oneBase = new LogBaseFunction(1.0, ln);
        MathFunction nanBase = new LogBaseFunction(Double.NaN, ln);
        MathFunction infinityBase = new LogBaseFunction(Double.POSITIVE_INFINITY, ln);

        assertAll(
                () -> assertTrue(Double.isNaN(negativeBase.calculate(10.0, EPS))),
                () -> assertTrue(Double.isNaN(zeroBase.calculate(10.0, EPS))),
                () -> assertTrue(Double.isNaN(oneBase.calculate(10.0, EPS))),
                () -> assertTrue(Double.isNaN(nanBase.calculate(10.0, EPS))),
                () -> assertTrue(Double.isNaN(infinityBase.calculate(10.0, EPS)))
        );
    }

    @Test
    @DisplayName("log_b: некорректный аргумент x")
    void invalidArgumentClass() {
        MathFunction log10 = new LogBaseFunction(10.0, ln);
        MathFunction logHalf = new LogBaseFunction(0.5, ln);

        assertAll(
                () -> assertTrue(Double.isNaN(log10.calculate(-1.0, EPS))),
                () -> assertTrue(Double.isNaN(log10.calculate(Double.NaN, EPS))),
                () -> assertEquals(Double.NEGATIVE_INFINITY, log10.calculate(0.0, EPS)),
                () -> assertEquals(Double.POSITIVE_INFINITY, logHalf.calculate(0.0, EPS)),
                () -> assertEquals(Double.POSITIVE_INFINITY, log10.calculate(Double.POSITIVE_INFINITY, EPS))
        );
    }

    @Test
    @DisplayName("log_b: разные значения точности eps")
    void differentEpsilonClass() {
        MathFunction log10 = new LogBaseFunction(10.0, ln);

        double rough = log10.calculate(100.0, 1e-4);
        double precise = log10.calculate(100.0, 1e-12);

        assertAll(
                () -> assertEquals(2.0, rough, 1e-3),
                () -> assertEquals(2.0, precise, DELTA),
                () -> assertEquals(rough, precise, 1e-3)
        );
    }

    @Test
    @DisplayName("log_b: некорректный eps")
    void invalidEpsilonClass() {
        MathFunction log10 = new LogBaseFunction(10.0, ln);

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> log10.calculate(10.0, 0.0)),
                () -> assertThrows(IllegalArgumentException.class, () -> log10.calculate(10.0, -1e-6)),
                () -> assertThrows(IllegalArgumentException.class, () -> log10.calculate(10.0, Double.NaN)),
                () -> assertThrows(IllegalArgumentException.class, () -> log10.calculate(10.0, Double.POSITIVE_INFINITY))
        );
    }
}