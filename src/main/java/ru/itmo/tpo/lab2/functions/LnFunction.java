package ru.itmo.tpo.lab2.functions;

public final class LnFunction implements MathFunction {
    @Override
    public double calculate(double x, double eps) {
        DoubleUtils.requireValidEps(eps);

        if (Double.isNaN(x)) {
            return Double.NaN;
        }
        if (x < 0.0) {
            return Double.NaN;
        }
        if (x == 0.0) {
            return Double.NEGATIVE_INFINITY;
        }
        if (Double.isInfinite(x)) {
            return Double.POSITIVE_INFINITY;
        }

        int power = 0;
        double reduced = x;

        while (reduced > DoubleUtils.E) {
            reduced /= DoubleUtils.E;
            power++;
        }
        while (reduced < 1.0 / DoubleUtils.E) {
            reduced *= DoubleUtils.E;
            power--;
        }

        return power + lnNearOne(reduced, eps);
    }

    private double lnNearOne(double x, double eps) {
        double z = (x - 1.0) / (x + 1.0);
        double z2 = z * z;
        double term = z;
        double sum = 0.0;
        int denominator = 1;

        while (DoubleUtils.abs(2.0 * term / denominator) > eps) {
            sum += term / denominator;
            term *= z2;
            denominator += 2;
        }

        return 2.0 * sum;
    }
}
