package ru.itmo.tpo.lab2.stubs;

import ru.itmo.tpo.lab2.functions.MathFunction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public final class TableMathFunctionStub implements MathFunction {
    private static final double DEFAULT_KEY_EPS = 1e-9;
    private final Map<Double, Double> values;
    private final double keyEps;

    public TableMathFunctionStub(Map<Double, Double> values) {
        this(values, DEFAULT_KEY_EPS);
    }

    public TableMathFunctionStub(Map<Double, Double> values, double keyEps) {
        this.values = new LinkedHashMap<>(values);
        this.keyEps = keyEps;
    }

    public static TableMathFunctionStub fromResource(String resourceName) {
        InputStream inputStream = TableMathFunctionStub.class.getClassLoader().getResourceAsStream(resourceName);
        if (inputStream == null) {
            throw new IllegalArgumentException("Resource not found: " + resourceName);
        }

        Map<Double, Double> map = new LinkedHashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                if (firstLine) {
                    firstLine = false;
                    if (line.startsWith("x")) {
                        continue;
                    }
                }
                String[] parts = line.split(";");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Bad stub line: " + line);
                }
                map.put(parse(parts[0]), parse(parts[1]));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read resource: " + resourceName, e);
        }
        return new TableMathFunctionStub(map);
    }

    @Override
    public double calculate(double x, double eps) {
        for (Map.Entry<Double, Double> entry : values.entrySet()) {
            if (abs(entry.getKey() - x) <= keyEps) {
                return entry.getValue();
            }
        }
        throw new IllegalArgumentException("No stub value for x=" + x);
    }

    private static double parse(String value) {
        return switch (value.trim()) {
            case "NaN" -> Double.NaN;
            case "Infinity", "+Infinity" -> Double.POSITIVE_INFINITY;
            case "-Infinity" -> Double.NEGATIVE_INFINITY;
            default -> Double.parseDouble(value.trim());
        };
    }

    private static double abs(double value) {
        return value < 0 ? -value : value;
    }
}
