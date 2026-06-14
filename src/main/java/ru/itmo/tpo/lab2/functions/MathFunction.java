package ru.itmo.tpo.lab2.functions;

@FunctionalInterface
public interface MathFunction { //чтобы SystemFunction не знала, кто именно ей передан: настоящая функция, мок, табличная заглушка или лямбда.
    double calculate(double x, double eps);
}
