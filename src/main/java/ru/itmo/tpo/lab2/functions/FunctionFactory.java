package ru.itmo.tpo.lab2.functions;

public final class FunctionFactory {
    private FunctionFactory() {
    }

    public static SystemFunction createSystemFunction() {
        MathFunction sin = new SinFunction();
        MathFunction cos = new CosFunction(sin);
        MathFunction cot = new CotFunction(sin, cos);

        MathFunction ln = new LnFunction();
        MathFunction log3 = new LogBaseFunction(3.0, ln);
        MathFunction log5 = new LogBaseFunction(5.0, ln);
        MathFunction log10 = new LogBaseFunction(10.0, ln);

        return new SystemFunction(cot, sin, cos, log3, log5, log10);
    }
}
