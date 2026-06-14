package ru.itmo.tpo.lab2.functions;

public final class SystemFunction implements MathFunction {
    private final MathFunction cot;
    private final MathFunction sin;
    private final MathFunction cos;
    private final MathFunction log3;
    private final MathFunction log5;
    private final MathFunction log10;

    public SystemFunction( //сделано черрез конструктор, чтобы было легко подставлять реальные модули или заглушки
            MathFunction cot,
            MathFunction sin,
            MathFunction cos,
            MathFunction log3,
            MathFunction log5,
            MathFunction log10
    ) {
        this.cot = cot;
        this.sin = sin;
        this.cos = cos;
        this.log3 = log3;
        this.log5 = log5;
        this.log10 = log10;
    }

    @Override
    public double calculate(double x, double eps) {
        DoubleUtils.requireValidEps(eps);

        if (Double.isNaN(x)) {
            return Double.NaN;
        }

        if (x <= 0.0) {
            double cotX = cot.calculate(x, eps);
            double sinX = sin.calculate(x, eps);
            double cosX = cos.calculate(x, eps);
            return ((((cotX + sinX) + cotX) - cosX) / cosX) / sinX;
        }

        double log3X = log3.calculate(x, eps);
        double log5X = log5.calculate(x, eps);
        double log10X = log10.calculate(x, eps);

        double ratio = log10X / log3X;
        double ratioFourth = ratio * ratio * ratio * ratio;
        return (ratioFourth + ((log5X + log10X) * log5X)) * (log5X * log10X);
    }
}
