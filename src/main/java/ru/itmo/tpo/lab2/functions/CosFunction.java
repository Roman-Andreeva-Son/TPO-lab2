package ru.itmo.tpo.lab2.functions;

public final class CosFunction implements MathFunction {
    private final MathFunction sin;

    public CosFunction(MathFunction sin) {
        this.sin = sin;
    }

    @Override
    public double calculate(double x, double eps) {
        DoubleUtils.requireValidEps(eps);

        if (Double.isNaN(x) || Double.isInfinite(x)) {
            return Double.NaN;
        }

        return sin.calculate(DoubleUtils.HALF_PI - x, eps);
    }
}
