package ru.itmo.tpo.lab2.functions;

public final class LogBaseFunction implements MathFunction {
    private final double base;
    private final MathFunction ln;

    public LogBaseFunction(double base, MathFunction ln) {
        this.base = base;
        this.ln = ln;
    }

    @Override
    public double calculate(double x, double eps) {
        DoubleUtils.requireValidEps(eps);

        if (Double.isNaN(base) || Double.isInfinite(base) || base <= 0.0 || base == 1.0) {
            return Double.NaN;
        }

        double lnX = ln.calculate(x, eps);
        double lnBase = ln.calculate(base, eps);
        return lnX / lnBase;
    }
}