package ru.itmo.tpo.lab2.functions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class SinFunctionTest {
    private static final double EPS = 1e-12;
    private static final double DELTA = 1e-6;
    private static final double H = 1e-7;

    private static final double PI = 3.14159265358979323846;
    private static final double TWO_PI = 2.0 * PI;
    private static final double HALF_PI = PI / 2.0;
    private static final double SQRT2_HALF = 0.7071067811865475;
    private static final double SQRT3_HALF = 0.8660254037844386;

    private final SinFunction sin = new SinFunction();

    @Test
    @DisplayName("sin: нули функции и смена знака в окрестности 0")
    void zeroClassAroundZero() {
        double left = sin.calculate(-H, EPS);
        double center = sin.calculate(0.0, EPS);
        double right = sin.calculate(H, EPS);

        assertAll(
                () -> assertTrue(left < 0.0),
                () -> assertEquals(0.0, center, DELTA),
                () -> assertTrue(right > 0.0)
        );
    }

    @Test
    @DisplayName("sin: максимум в pi/2")
    void maximumClass() {
        double left = sin.calculate(HALF_PI - H, EPS);
        double center = sin.calculate(HALF_PI, EPS);
        double right = sin.calculate(HALF_PI + H, EPS);

        assertAll(
                () -> assertTrue(center >= left - DELTA),
                () -> assertTrue(center >= right - DELTA),
                () -> assertEquals(1.0, center, DELTA),
                () -> assertTrue(left > 0.0),
                () -> assertTrue(right > 0.0)
        );
    }

    @Test
    @DisplayName("sin: минимум в -pi/2")
    void minimumClass() {
        double left = sin.calculate(-HALF_PI - H, EPS);
        double center = sin.calculate(-HALF_PI, EPS);
        double right = sin.calculate(-HALF_PI + H, EPS);

        assertAll(
                () -> assertTrue(center <= left + DELTA),
                () -> assertTrue(center <= right + DELTA),
                () -> assertEquals(-1.0, center, DELTA),
                () -> assertTrue(left < 0.0),
                () -> assertTrue(right < 0.0)
        );
    }

    @Test
    @DisplayName("sin: ноль в pi со сменой знака")
    void zeroClassAroundPi() {
        double left = sin.calculate(PI - H, EPS);
        double center = sin.calculate(PI, EPS);
        double right = sin.calculate(PI + H, EPS);

        assertAll(
                () -> assertTrue(left > 0.0),
                () -> assertEquals(0.0, center, DELTA),
                () -> assertTrue(right < 0.0)
        );
    }

    @Test
    @DisplayName("sin: известные значения внутри валидных интервалов")
    void knownValuesClass() {
        assertAll(
                () -> assertEquals(0.5, sin.calculate(PI / 6.0, EPS), DELTA),
                () -> assertEquals(SQRT2_HALF, sin.calculate(PI / 4.0, EPS), DELTA),
                () -> assertEquals(SQRT3_HALF, sin.calculate(PI / 3.0, EPS), DELTA),
                () -> assertEquals(-SQRT2_HALF, sin.calculate(-PI / 4.0, EPS), DELTA)
        );
    }

    @Test
    @DisplayName("sin: положительный и отрицательный интервалы")
    void signIntervalsClass() {
        assertAll(
                () -> assertTrue(sin.calculate(PI / 3.0, EPS) > 0.0),
                () -> assertTrue(sin.calculate(2.0 * PI / 3.0, EPS) > 0.0),
                () -> assertTrue(sin.calculate(-PI / 3.0, EPS) < 0.0),
                () -> assertTrue(sin.calculate(4.0 * PI / 3.0, EPS) < 0.0)
        );
    }

    @Test
    @DisplayName("sin: возрастание на [-pi/2; pi/2]")
    void increasingIntervalClass() {
        double y1 = sin.calculate(-PI / 3.0, EPS);
        double y2 = sin.calculate(0.0, EPS);
        double y3 = sin.calculate(PI / 3.0, EPS);

        assertAll(
                () -> assertTrue(y1 < y2),
                () -> assertTrue(y2 < y3)
        );
    }

    @Test
    @DisplayName("sin: убывание на [pi/2; 3pi/2]")
    void decreasingIntervalClass() {
        double y1 = sin.calculate(2.0 * PI / 3.0, EPS);
        double y2 = sin.calculate(PI, EPS);
        double y3 = sin.calculate(4.0 * PI / 3.0, EPS);

        assertAll(
                () -> assertTrue(y1 > y2),
                () -> assertTrue(y2 > y3)
        );
    }

    @Test
    @DisplayName("sin: нечётность")
    void oddFunctionClass() {
        double x = 1.2345;

        assertAll(
                () -> assertEquals(-sin.calculate(x, EPS), sin.calculate(-x, EPS), DELTA),
                () -> assertEquals(sin.calculate(x, EPS), -sin.calculate(-x, EPS), DELTA)
        );
    }

    @Test
    @DisplayName("sin: периодичность")
    void periodicityClass() {
        double x = PI / 5.0;

        assertAll(
                () -> assertEquals(sin.calculate(x, EPS), sin.calculate(x + TWO_PI, EPS), DELTA),
                () -> assertEquals(sin.calculate(x, EPS), sin.calculate(x - TWO_PI, EPS), DELTA),
                () -> assertEquals(sin.calculate(x, EPS), sin.calculate(x + 5.0 * TWO_PI, EPS), DELTA)
        );
    }

    @Test
    @DisplayName("sin: разные значения точности eps")
    void differentEpsilonClass() {
        double x = PI / 3.0;

        double rough = sin.calculate(x, 1e-4);
        double precise = sin.calculate(x, 1e-12);

        assertAll(
                () -> assertEquals(SQRT3_HALF, rough, 1e-3),
                () -> assertEquals(SQRT3_HALF, precise, DELTA),
                () -> assertEquals(rough, precise, 1e-3)
        );
    }

    @Test
    @DisplayName("sin: некорректный eps")
    void invalidEpsilonClass() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> sin.calculate(1.0, 0.0)),
                () -> assertThrows(IllegalArgumentException.class, () -> sin.calculate(1.0, -1e-6)),
                () -> assertThrows(IllegalArgumentException.class, () -> sin.calculate(1.0, Double.NaN)),
                () -> assertThrows(IllegalArgumentException.class, () -> sin.calculate(1.0, Double.POSITIVE_INFINITY))
        );
    }

    @Test
    @DisplayName("sin: NaN и бесконечности")
    void invalidArgumentClass() {
        assertAll(
                () -> assertTrue(Double.isNaN(sin.calculate(Double.NaN, EPS))),
                () -> assertTrue(Double.isNaN(sin.calculate(Double.POSITIVE_INFINITY, EPS))),
                () -> assertTrue(Double.isNaN(sin.calculate(Double.NEGATIVE_INFINITY, EPS)))
        );
    }

    @Test
    @DisplayName("sin: property fuzzing по области значений, нечётности и периодичности")
    void propertyFuzzingClass() {
        Random random = new Random(42);

        for (int i = 0; i < 10_000; i++) {
            double x = (random.nextDouble() * 200.0 - 100.0) * PI;
            double y = sin.calculate(x, EPS);

            assertTrue(y >= -1.0 - DELTA, "sin(x) меньше -1 при x=" + x);
            assertTrue(y <= 1.0 + DELTA, "sin(x) больше 1 при x=" + x);
            assertEquals(-y, sin.calculate(-x, EPS), DELTA, "нарушена нечётность при x=" + x);
            assertEquals(y, sin.calculate(x + TWO_PI, EPS), DELTA, "нарушена периодичность при x=" + x);
        }
    }
}