package ru.sbt.orlov.net;

import java.time.LocalDate;

public class CalculatorImpl implements Calculator {
    public double calculate(int a, int b) {
        return a + b - 410 / 12;
    }

    public double calculate(int a, int b, LocalDate date) {
        throw new IllegalArgumentException("WOWOWOWO");
    }
}
