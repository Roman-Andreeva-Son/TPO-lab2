package ru.itmo.tpo.lab2.functions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

public class DependencyOrderTest {
    private static final double EPS = 1e-12;
    private static final double PI = 3.14159265358979323846;
    private static final double HALF_PI = PI / 2.0;

    @Test
    @DisplayName("dependency: cos вызывает sin")
    void cosCallsSin() {
        MathFunction sin = mock(MathFunction.class);
        when(sin.calculate(anyDouble(), anyDouble())).thenReturn(0.5);

        CosFunction cos = new CosFunction(sin);
        cos.calculate(0.7, EPS);

        assertAll(
                () -> verify(sin, times(1)).calculate(HALF_PI - 0.7, EPS),
                () -> verifyNoMoreInteractions(sin)
        );
    }

    @Test
    @DisplayName("dependency: cot вызывает sin и cos")
    void cotCallsSinAndCos() {
        MathFunction sin = mock(MathFunction.class);
        MathFunction cos = mock(MathFunction.class);

        when(sin.calculate(anyDouble(), anyDouble())).thenReturn(0.5);
        when(cos.calculate(anyDouble(), anyDouble())).thenReturn(1.0);

        CotFunction cot = new CotFunction(sin, cos);
        cot.calculate(0.7, EPS);

        assertAll(
                () -> verify(cos, times(1)).calculate(0.7, EPS),
                () -> verify(sin, times(1)).calculate(0.7, EPS),
                () -> verifyNoMoreInteractions(sin, cos)
        );
    }

    @Test
    @DisplayName("dependency: log_b вызывает ln для x и base")
    void logBaseCallsLnForArgumentAndBase() {
        MathFunction ln = mock(MathFunction.class);

        when(ln.calculate(25.0, EPS)).thenReturn(3.0);
        when(ln.calculate(5.0, EPS)).thenReturn(1.5);

        LogBaseFunction log5 = new LogBaseFunction(5.0, ln);
        log5.calculate(25.0, EPS);

        assertAll(
                () -> verify(ln, times(1)).calculate(25.0, EPS),
                () -> verify(ln, times(1)).calculate(5.0, EPS),
                () -> verifyNoMoreInteractions(ln)
        );
    }

    @Test
    @DisplayName("dependency: system при x <= 0 вызывает только trig-модули")
    void systemNegativeBranchCallsOnlyTrigModules() {
        MathFunction cot = mock(MathFunction.class);
        MathFunction sin = mock(MathFunction.class);
        MathFunction cos = mock(MathFunction.class);
        MathFunction log3 = mock(MathFunction.class);
        MathFunction log5 = mock(MathFunction.class);
        MathFunction log10 = mock(MathFunction.class);

        when(cot.calculate(anyDouble(), anyDouble())).thenReturn(-1.0);
        when(sin.calculate(anyDouble(), anyDouble())).thenReturn(-0.7);
        when(cos.calculate(anyDouble(), anyDouble())).thenReturn(0.7);

        SystemFunction system = new SystemFunction(cot, sin, cos, log3, log5, log10);
        system.calculate(-0.7, EPS);

        assertAll(
                () -> verify(cot, times(1)).calculate(-0.7, EPS),
                () -> verify(sin, times(1)).calculate(-0.7, EPS),
                () -> verify(cos, times(1)).calculate(-0.7, EPS),
                () -> verifyNoInteractions(log3, log5, log10)
        );
    }

    @Test
    @DisplayName("dependency: system при x > 0 вызывает только log-модули")
    void systemPositiveBranchCallsOnlyLogModules() {
        MathFunction cot = mock(MathFunction.class);
        MathFunction sin = mock(MathFunction.class);
        MathFunction cos = mock(MathFunction.class);
        MathFunction log3 = mock(MathFunction.class);
        MathFunction log5 = mock(MathFunction.class);
        MathFunction log10 = mock(MathFunction.class);

        when(log3.calculate(anyDouble(), anyDouble())).thenReturn(2.0);
        when(log5.calculate(anyDouble(), anyDouble())).thenReturn(3.0);
        when(log10.calculate(anyDouble(), anyDouble())).thenReturn(4.0);

        SystemFunction system = new SystemFunction(cot, sin, cos, log3, log5, log10);
        system.calculate(25.0, EPS);

        assertAll(
                () -> verify(log3, times(1)).calculate(25.0, EPS),
                () -> verify(log5, times(1)).calculate(25.0, EPS),
                () -> verify(log10, times(1)).calculate(25.0, EPS),
                () -> verifyNoInteractions(cot, sin, cos)
        );
    }
}