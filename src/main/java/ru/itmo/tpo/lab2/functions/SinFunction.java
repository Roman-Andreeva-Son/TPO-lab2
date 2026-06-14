package ru.itmo.tpo.lab2.functions;

public final class SinFunction implements MathFunction {
    @Override
    public double calculate(double x, double eps) {
        DoubleUtils.requireValidEps(eps);

        if (Double.isNaN(x) || Double.isInfinite(x)) {
            return Double.NaN;
        }

        double normalizedX = DoubleUtils.normalizeRadians(x);
        double term = normalizedX;
        double sum = term;
        int n = 1;

        while (DoubleUtils.abs(term) > eps) {
            term *= -normalizedX * normalizedX / ((2.0 * n + 1.0) * (2.0 * n));
            sum += term;
            n++;
        }

        return sum;
    }
}
