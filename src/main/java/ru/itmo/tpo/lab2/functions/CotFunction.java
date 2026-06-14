package ru.itmo.tpo.lab2.functions;

public final class CotFunction implements MathFunction {
    private final MathFunction sin;
    private final MathFunction cos;

    public CotFunction(MathFunction sin, MathFunction cos) {
        this.sin = sin;
        this.cos = cos;
    }

    @Override
    public double calculate(double x, double eps) {
        DoubleUtils.requireValidEps(eps);
        return cos.calculate(x, eps) / sin.calculate(x, eps);
    }
}
