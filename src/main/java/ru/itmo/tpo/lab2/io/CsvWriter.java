package ru.itmo.tpo.lab2.io;

import ru.itmo.tpo.lab2.functions.MathFunction;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public final class CsvWriter {
    private static final String SEPARATOR = ";";

    private CsvWriter() {
    }

    public static void write(
            MathFunction function,
            double from,
            double to,
            double step,
            double eps,
            Path outputPath
    ) throws IOException {
        write(function, from, to, step, eps, outputPath, "result");
    }

    public static void write(
            MathFunction function,
            double from,
            double to,
            double step,
            double eps,
            Path outputPath,
            String moduleName
    ) throws IOException {
        validateRange(from, to, step);

        Path parent = outputPath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
            writer.write("X" + SEPARATOR + "Result_" + moduleName + "(X)");
            writer.newLine();

            if (step > 0.0) {
                for (double x = from; x <= to + step / 2.0; x += step) {
                    writeRow(writer, function, x, eps);
                }
            } else {
                for (double x = from; x >= to + step / 2.0; x += step) {
                    writeRow(writer, function, x, eps);
                }
            }
        }
    }

    private static void writeRow(
            BufferedWriter writer,
            MathFunction function,
            double x,
            double eps
    ) throws IOException {
        double result = function.calculate(x, eps);

        writer.write(format(x));
        writer.write(SEPARATOR);
        writer.write(format(result));
        writer.newLine();
    }

    private static void validateRange(double from, double to, double step) {
        if (Double.isNaN(from) || Double.isNaN(to) || Double.isNaN(step)
                || Double.isInfinite(from) || Double.isInfinite(to) || Double.isInfinite(step)) {
            throw new IllegalArgumentException("from, to and step must be finite");
        }

        if (step == 0.0) {
            throw new IllegalArgumentException("step must not be zero");
        }

        if (from < to && step < 0.0) {
            throw new IllegalArgumentException("step must be positive when from < to");
        }

        if (from > to && step > 0.0) {
            throw new IllegalArgumentException("step must be negative when from > to");
        }
    }

    private static String format(double value) {
        if (Double.isNaN(value)) {
            return "NaN";
        }

        if (value == Double.POSITIVE_INFINITY) {
            return "Infinity";
        }

        if (value == Double.NEGATIVE_INFINITY) {
            return "-Infinity";
        }

        return String.format(Locale.US, "%.10f", value);
    }
}