package ru.itmo.tpo.lab2.functions;

final class DoubleUtils {
    static final double PI = 3.14159265358979323846264338327950288419716939937510;
    static final double HALF_PI = PI / 2.0;
    static final double TWO_PI = 2.0 * PI;
    static final double E = 2.71828182845904523536028747135266249775724709369995;

    private DoubleUtils() {
    }

    static void requireValidEps(double eps) {
        if (Double.isNaN(eps) || Double.isInfinite(eps) || eps <= 0.0) {
            throw new IllegalArgumentException("eps must be finite and > 0");
        }
    }

    static double abs(double value) {
        return value < 0.0 ? -value : value;
    }

    static double normalizeRadians(double x) { //Без нормализации ряд считал бы очень долго и неточно
        double r = x % TWO_PI;
        if (r > PI) {
            r -= TWO_PI;
        }
        if (r < -PI) {
            r += TWO_PI;
        }
        return r;
    }
}
