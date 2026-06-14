# TPO lab 2, variant 9123

Function system:

```text
x <= 0: ((((cot(x) + sin(x)) + cot(x)) - cos(x)) / cos(x)) / sin(x)
x > 0 : (((log10(x) / log3(x))^2)^2 + ((log5(x) + log10(x)) * log5(x))) * (log5(x) * log10(x))
```

Base functions `sin(x)` and `ln(x)` are implemented through series.
The remaining functions are expressed through these base dependencies.

Integration tests use Mockito stubs and mock objects instead of CSV table stubs.
Current test suite: 83 tests.

Run tests:

```bash
gradlew clean test
```
