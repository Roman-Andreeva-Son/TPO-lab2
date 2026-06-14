package ru.itmo.tpo.lab2.app;

import ru.itmo.tpo.lab2.functions.CosFunction;
import ru.itmo.tpo.lab2.functions.CotFunction;
import ru.itmo.tpo.lab2.functions.FunctionFactory;
import ru.itmo.tpo.lab2.functions.LnFunction;
import ru.itmo.tpo.lab2.functions.LogBaseFunction;
import ru.itmo.tpo.lab2.functions.MathFunction;
import ru.itmo.tpo.lab2.functions.SinFunction;
import ru.itmo.tpo.lab2.functions.SystemFunction;
import ru.itmo.tpo.lab2.io.CsvWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Main {
    private static final double DEFAULT_EPS = 1e-7;
    private static final Path DEFAULT_OUTPUT_DIR = Path.of("build", "results");

    private Main() {
    }

    public static void main(String[] args) throws IOException {
        Map<String, MathFunction> functions = createFunctions();

        if (args.length == 0) {
            writeDefaultCsvFiles(functions);
            return;
        }

        if (args.length != 5 && args.length != 6) {
            printUsage();
            return;
        }

        String moduleName = args[0];
        MathFunction function = functions.get(moduleName);

        if (function == null) {
            throw new IllegalArgumentException("Unknown module: " + moduleName);
        }

        double from = Double.parseDouble(args[1]);
        double to = Double.parseDouble(args[2]);
        double step = Double.parseDouble(args[3]);
        double eps = Double.parseDouble(args[4]);

        Path outputPath = args.length == 6
                ? Path.of(args[5])
                : DEFAULT_OUTPUT_DIR.resolve(moduleName + ".csv");

        CsvWriter.write(function, from, to, step, eps, outputPath, moduleName);
    }

    private static Map<String, MathFunction> createFunctions() {
        MathFunction sin = new SinFunction();
        MathFunction cos = new CosFunction(sin);
        MathFunction cot = new CotFunction(sin, cos);

        MathFunction ln = new LnFunction();
        MathFunction log3 = new LogBaseFunction(3.0, ln);
        MathFunction log5 = new LogBaseFunction(5.0, ln);
        MathFunction log10 = new LogBaseFunction(10.0, ln);

        SystemFunction system = FunctionFactory.createSystemFunction();

        Map<String, MathFunction> functions = new LinkedHashMap<>();
        functions.put("sin", sin);
        functions.put("cos", cos);
        functions.put("cot", cot);
        functions.put("ln", ln);
        functions.put("log3", log3);
        functions.put("log5", log5);
        functions.put("log10", log10);
        functions.put("system", system);

        return functions;
    }

    private static void writeDefaultCsvFiles(Map<String, MathFunction> functions) throws IOException {
        CsvWriter.write(functions.get("sin"), -10.0, 10.0, 0.1, DEFAULT_EPS,
                DEFAULT_OUTPUT_DIR.resolve("sin.csv"), "sin");

        CsvWriter.write(functions.get("cos"), -10.0, 10.0, 0.1, DEFAULT_EPS,
                DEFAULT_OUTPUT_DIR.resolve("cos.csv"), "cos");

        CsvWriter.write(functions.get("cot"), -10.0, 10.0, 0.1, DEFAULT_EPS,
                DEFAULT_OUTPUT_DIR.resolve("cot.csv"), "cot");

        CsvWriter.write(functions.get("ln"), 0.1, 10.0, 0.1, DEFAULT_EPS,
                DEFAULT_OUTPUT_DIR.resolve("ln.csv"), "ln");

        CsvWriter.write(functions.get("log3"), 0.1, 10.0, 0.1, DEFAULT_EPS,
                DEFAULT_OUTPUT_DIR.resolve("log3.csv"), "log3");

        CsvWriter.write(functions.get("log5"), 0.1, 10.0, 0.1, DEFAULT_EPS,
                DEFAULT_OUTPUT_DIR.resolve("log5.csv"), "log5");

        CsvWriter.write(functions.get("log10"), 0.1, 10.0, 0.1, DEFAULT_EPS,
                DEFAULT_OUTPUT_DIR.resolve("log10.csv"), "log10");

        CsvWriter.write(functions.get("system"), -10.0, 10.0, 0.1, DEFAULT_EPS,
                DEFAULT_OUTPUT_DIR.resolve("system.csv"), "system");
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  gradlew run");
        System.out.println("  gradlew run --args=\"module from to step eps [outputFile]\"");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  gradlew run --args=\"cos -6.28 6.28 0.01 1e-7\"");
        System.out.println("  gradlew run --args=\"log5 0.1 10 0.05 1e-7\"");
        System.out.println("  gradlew run --args=\"system -10 10 0.1 1e-7 build/results/system.csv\"");
    }
}