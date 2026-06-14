package ru.itmo.tpo.lab2.functions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class CosFunctionTest {
    private static final double EPS = 1e-12;
    private static final double DELTA = 1e-6;
    private static final double H = 1e-7;

    private static final double PI = 3.14159265358979323846;
    private static final double TWO_PI = 2.0 * PI;
    private static final double HALF_PI = PI / 2.0;
    private static final double SQRT2_HALF = 0.7071067811865475;
    private static final double SQRT3_HALF = 0.8660254037844386;

    private final CosFunction cos = new CosFunction(new SinFunction());

    @Test
    @DisplayName("cos: максимум в 0")
    void maximumClass() {
        double left = cos.calculate(-H, EPS);
        double center = cos.calculate(0.0, EPS);
        double right = cos.calculate(H, EPS);

        assertAll(
                () -> assertTrue(center >= left - DELTA),
                () -> assertTrue(center >= right - DELTA),
                () -> assertEquals(1.0, center, DELTA),
                () -> assertTrue(left > 0.0),
                () -> assertTrue(right > 0.0)
        );
    }

    @Test
    @DisplayName("cos: минимум в pi")
    void minimumClass() {
        double left = cos.calculate(PI - H, EPS);
        double center = cos.calculate(PI, EPS);
        double right = cos.calculate(PI + H, EPS);

        assertAll(
                () -> assertTrue(center <= left + DELTA),
                () -> assertTrue(center <= right + DELTA),
                () -> assertEquals(-1.0, center, DELTA),
                () -> assertTrue(left < 0.0),
                () -> assertTrue(right < 0.0)
        );
    }

    @Test
    @DisplayName("cos: ноль в pi/2 со сменой знака")
    void zeroClassAroundPiOverTwo() {
        double left = cos.calculate(HALF_PI - H, EPS);
        double center = cos.calculate(HALF_PI, EPS);
        double right = cos.calculate(HALF_PI + H, EPS);

        assertAll(
                () -> assertTrue(left > 0.0),
                () -> assertEquals(0.0, center, DELTA),
                () -> assertTrue(right < 0.0)
        );
    }

    @Test
    @DisplayName("cos: ноль в -pi/2 со сменой знака")
    void zeroClassAroundMinusPiOverTwo() {
        double left = cos.calculate(-HALF_PI - H, EPS);
        double center = cos.calculate(-HALF_PI, EPS);
        double right = cos.calculate(-HALF_PI + H, EPS);

        assertAll(
                () -> assertTrue(left < 0.0),
                () -> assertEquals(0.0, center, DELTA),
                () -> assertTrue(right > 0.0)
        );
    }

    @Test
    @DisplayName("cos: известные значения внутри валидных интервалов")
    void knownValuesClass() {
        assertAll(
                () -> assertEquals(SQRT3_HALF, cos.calculate(PI / 6.0, EPS), DELTA),
                () -> assertEquals(SQRT2_HALF, cos.calculate(PI / 4.0, EPS), DELTA),
                () -> assertEquals(0.5, cos.calculate(PI / 3.0, EPS), DELTA),
                () -> assertEquals(SQRT2_HALF, cos.calculate(-PI / 4.0, EPS), DELTA)
        );
    }

    @Test
    @DisplayName("cos: положительный и отрицательный интервалы")
    void signIntervalsClass() {
        assertAll(
                () -> assertTrue(cos.calculate(PI / 3.0, EPS) > 0.0),
                () -> assertTrue(cos.calculate(-PI / 3.0, EPS) > 0.0),
                () -> assertTrue(cos.calculate(2.0 * PI / 3.0, EPS) < 0.0),
                () -> assertTrue(cos.calculate(-2.0 * PI / 3.0, EPS) < 0.0)
        );
    }

    @Test
    @DisplayName("cos: возрастание на [-pi; 0]")
    void increasingIntervalClass() {
        double y1 = cos.calculate(-3.0 * PI / 4.0, EPS);
        double y2 = cos.calculate(-HALF_PI, EPS);
        double y3 = cos.calculate(-PI / 4.0, EPS);

        assertAll(
                () -> assertTrue(y1 < y2),
                () -> assertTrue(y2 < y3)
        );
    }

    @Test
    @DisplayName("cos: убывание на [0; pi]")
    void decreasingIntervalClass() {
        double y1 = cos.calculate(PI / 4.0, EPS);
        double y2 = cos.calculate(HALF_PI, EPS);
        double y3 = cos.calculate(3.0 * PI / 4.0, EPS);

        assertAll(
                () -> assertTrue(y1 > y2),
                () -> assertTrue(y2 > y3)
        );
    }

    @Test
    @DisplayName("cos: чётность")
    void evenFunctionClass() {
        double x = 1.2345;

        assertAll(
                () -> assertEquals(cos.calculate(x, EPS), cos.calculate(-x, EPS), DELTA),
                () -> assertEquals(cos.calculate(-x, EPS), cos.calculate(x, EPS), DELTA)
        );
    }

    @Test
    @DisplayName("cos: периодичность")
    void periodicityClass() {
        double x = PI / 5.0;

        assertAll(
                () -> assertEquals(cos.calculate(x, EPS), cos.calculate(x + TWO_PI, EPS), DELTA),
                () -> assertEquals(cos.calculate(x, EPS), cos.calculate(x - TWO_PI, EPS), DELTA),
                () -> assertEquals(cos.calculate(x, EPS), cos.calculate(x + 5.0 * TWO_PI, EPS), DELTA)
        );
    }

    @Test
    @DisplayName("cos: разные значения точности eps")
    void differentEpsilonClass() {
        double x = PI / 3.0;

        double rough = cos.calculate(x, 1e-4);
        double precise = cos.calculate(x, 1e-12);

        assertAll(
                () -> assertEquals(0.5, rough, 1e-3),
                () -> assertEquals(0.5, precise, DELTA),
                () -> assertEquals(rough, precise, 1e-3)
        );
    }

    @Test
    @DisplayName("cos: некорректный eps")
    void invalidEpsilonClass() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> cos.calculate(1.0, 0.0)),
                () -> assertThrows(IllegalArgumentException.class, () -> cos.calculate(1.0, -1e-6)),
                () -> assertThrows(IllegalArgumentException.class, () -> cos.calculate(1.0, Double.NaN)),
                () -> assertThrows(IllegalArgumentException.class, () -> cos.calculate(1.0, Double.POSITIVE_INFINITY))
        );
    }

    @Test
    @DisplayName("cos: NaN и бесконечности")
    void invalidArgumentClass() {
        assertAll(
                () -> assertTrue(Double.isNaN(cos.calculate(Double.NaN, EPS))),
                () -> assertTrue(Double.isNaN(cos.calculate(Double.POSITIVE_INFINITY, EPS))),
                () -> assertTrue(Double.isNaN(cos.calculate(Double.NEGATIVE_INFINITY, EPS)))
        );
    }

    @Test
    @DisplayName("cos: property fuzzing по области значений, чётности и периодичности")
    void propertyFuzzingClass() {
        Random random = new Random(43);

        for (int i = 0; i < 10_000; i++) {
            double x = (random.nextDouble() * 200.0 - 100.0) * PI;
            double y = cos.calculate(x, EPS);

            assertTrue(y >= -1.0 - DELTA, "cos(x) меньше -1 при x=" + x);
            assertTrue(y <= 1.0 + DELTA, "cos(x) больше 1 при x=" + x);
            assertEquals(y, cos.calculate(-x, EPS), DELTA, "нарушена чётность при x=" + x);
            assertEquals(y, cos.calculate(x + TWO_PI, EPS), DELTA, "нарушена периодичность при x=" + x);
        }
    }
}