package ru.itmo.tpo.lab2.functions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CotFunctionTest {
    private static final double EPS = 1e-12;
    private static final double DELTA = 1e-6;
    private static final double H = 1e-7;

    private static final double PI = 3.14159265358979323846;
    private static final double HALF_PI = PI / 2.0;

    private final SinFunction sin = new SinFunction();
    private final CosFunction cos = new CosFunction(sin);
    private final CotFunction cot = new CotFunction(sin, cos);

    @Test
    @DisplayName("cot: известные значения")
    void knownValuesClass() {
        assertAll(
                () -> assertEquals(1.0, cot.calculate(PI / 4.0, EPS), DELTA),
                () -> assertEquals(-1.0, cot.calculate(-PI / 4.0, EPS), DELTA),
                () -> assertEquals(-1.0, cot.calculate(3.0 * PI / 4.0, EPS), DELTA)
        );
    }

    @Test
    @DisplayName("cot: ноль в pi/2")
    void zeroClassAroundPiOverTwo() {
        double left = cot.calculate(HALF_PI - H, EPS);
        double center = cot.calculate(HALF_PI, EPS);
        double right = cot.calculate(HALF_PI + H, EPS);

        assertAll(
                () -> assertTrue(left > 0.0),
                () -> assertEquals(0.0, center, DELTA),
                () -> assertTrue(right < 0.0)
        );
    }

    @Test
    @DisplayName("cot: ноль в -pi/2")
    void zeroClassAroundMinusPiOverTwo() {
        double left = cot.calculate(-HALF_PI - H, EPS);
        double center = cot.calculate(-HALF_PI, EPS);
        double right = cot.calculate(-HALF_PI + H, EPS);

        assertAll(
                () -> assertTrue(left > 0.0),
                () -> assertEquals(0.0, center, DELTA),
                () -> assertTrue(right < 0.0)
        );
    }

    @Test
    @DisplayName("cot: полюс в 0")
    void poleClassAroundZero() {
        double left = cot.calculate(-H, EPS);
        double center = cot.calculate(0.0, EPS);
        double right = cot.calculate(H, EPS);

        assertAll(
                () -> assertTrue(left < -1_000_000.0),
                () -> assertTrue(Double.isInfinite(center)),
                () -> assertTrue(right > 1_000_000.0)
        );
    }

    @Test
    @DisplayName("cot: полюс в pi")
    void poleClassAroundPi() {
        double left = cot.calculate(PI - H, EPS);
        double center = cot.calculate(PI, EPS);
        double right = cot.calculate(PI + H, EPS);

        assertAll(
                () -> assertTrue(left < -1_000_000.0),
                () -> assertTrue(Double.isInfinite(center) || Double.isNaN(center) || center > 1_000_000.0 || center < -1_000_000.0),
                () -> assertTrue(right > 1_000_000.0)
        );
    }

    @Test
    @DisplayName("cot: положительный и отрицательный интервалы")
    void signIntervalsClass() {
        assertAll(
                () -> assertTrue(cot.calculate(PI / 4.0, EPS) > 0.0),
                () -> assertTrue(cot.calculate(3.0 * PI / 4.0, EPS) < 0.0),
                () -> assertTrue(cot.calculate(-PI / 4.0, EPS) < 0.0),
                () -> assertTrue(cot.calculate(-3.0 * PI / 4.0, EPS) > 0.0)
        );
    }

    @Test
    @DisplayName("cot: убывание на интервале между полюсами")
    void decreasingIntervalClass() {
        double y1 = cot.calculate(PI / 4.0, EPS);
        double y2 = cot.calculate(HALF_PI, EPS);
        double y3 = cot.calculate(3.0 * PI / 4.0, EPS);

        assertAll(
                () -> assertTrue(y1 > y2),
                () -> assertTrue(y2 > y3)
        );
    }

    @Test
    @DisplayName("cot: нечётность")
    void oddFunctionClass() {
        double x = 0.8;

        assertAll(
                () -> assertEquals(-cot.calculate(x, EPS), cot.calculate(-x, EPS), DELTA),
                () -> assertEquals(cot.calculate(x, EPS), -cot.calculate(-x, EPS), DELTA)
        );
    }

    @Test
    @DisplayName("cot: период pi")
    void periodicityClass() {
        double x = 0.8;

        assertAll(
                () -> assertEquals(cot.calculate(x, EPS), cot.calculate(x + PI, EPS), DELTA),
                () -> assertEquals(cot.calculate(x, EPS), cot.calculate(x - PI, EPS), DELTA),
                () -> assertEquals(cot.calculate(x, EPS), cot.calculate(x + 4.0 * PI, EPS), DELTA)
        );
    }

    @Test
    @DisplayName("cot: некорректный eps")
    void invalidEpsilonClass() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> cot.calculate(1.0, 0.0)),
                () -> assertThrows(IllegalArgumentException.class, () -> cot.calculate(1.0, -1e-6)),
                () -> assertThrows(IllegalArgumentException.class, () -> cot.calculate(1.0, Double.NaN)),
                () -> assertThrows(IllegalArgumentException.class, () -> cot.calculate(1.0, Double.POSITIVE_INFINITY))
        );
    }

    @Test
    @DisplayName("cot: NaN и бесконечности")
    void invalidArgumentClass() {
        assertAll(
                () -> assertTrue(Double.isNaN(cot.calculate(Double.NaN, EPS))),
                () -> assertTrue(Double.isNaN(cot.calculate(Double.POSITIVE_INFINITY, EPS))),
                () -> assertTrue(Double.isNaN(cot.calculate(Double.NEGATIVE_INFINITY, EPS)))
        );
    }
}