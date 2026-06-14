package ru.itmo.tpo.lab2.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.itmo.tpo.lab2.functions.MathFunction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

public class CsvWriterTest {
    @TempDir
    Path tempDir;

    @Test
    void writeSimpleFunction() throws IOException {
        MathFunction function = (x, eps) -> x * x;
        Path output = tempDir.resolve("square.csv");

        CsvWriter.write(function, 0.0, 0.2, 0.1, 1e-7, output);

        List<String> lines = Files.readAllLines(output);

        assertAll(
                () -> assertEquals(4, lines.size()),
                () -> assertEquals("X;Result_result(X)", lines.get(0)),
                () -> assertEquals("0.0000000000;0.0000000000", lines.get(1)),
                () -> assertEquals("0.1000000000;0.0100000000", lines.get(2)),
                () -> assertEquals("0.2000000000;0.0400000000", lines.get(3))
        );
    }

    @Test
    void writeSimpleFunctionWithModuleName() throws IOException {
        MathFunction function = (x, eps) -> x + 1.0;
        Path output = tempDir.resolve("custom.csv");

        CsvWriter.write(function, 1.0, 1.2, 0.1, 1e-7, output, "custom");

        List<String> lines = Files.readAllLines(output);

        assertAll(
                () -> assertEquals(4, lines.size()),
                () -> assertEquals("X;Result_custom(X)", lines.get(0)),
                () -> assertEquals("1.0000000000;2.0000000000", lines.get(1)),
                () -> assertEquals("1.1000000000;2.1000000000", lines.get(2)),
                () -> assertEquals("1.2000000000;2.2000000000", lines.get(3))
        );
    }

    @Test
    void writeWithMock() throws IOException {
        MathFunction function = mock(MathFunction.class);
        when(function.calculate(anyDouble(), anyDouble())).thenReturn(42.0);

        Path output = tempDir.resolve("mock.csv");

        CsvWriter.write(function, 0.0, 0.2, 0.1, 1e-7, output, "mock");

        List<String> lines = Files.readAllLines(output);

        assertAll(
                () -> assertEquals(4, lines.size()),
                () -> assertEquals("X;Result_mock(X)", lines.get(0)),
                () -> assertEquals("0.0000000000;42.0000000000", lines.get(1)),
                () -> assertEquals("0.1000000000;42.0000000000", lines.get(2)),
                () -> assertEquals("0.2000000000;42.0000000000", lines.get(3)),
                () -> verify(function, times(3)).calculate(anyDouble(), anyDouble())
        );
    }

    @Test
    void invalidRange() {
        MathFunction function = (x, eps) -> x;
        Path output = tempDir.resolve("invalid.csv");

        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> CsvWriter.write(function, 0.0, 1.0, 0.0, 1e-7, output)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> CsvWriter.write(function, 0.0, 1.0, -0.1, 1e-7, output)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> CsvWriter.write(function, 1.0, 0.0, 0.1, 1e-7, output)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> CsvWriter.write(function, Double.NaN, 1.0, 0.1, 1e-7, output))
        );
    }
}