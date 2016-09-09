package ru.sbt.orlov.net;

import java.util.Date;

public class CalculatorImpl implements Calculator {
    public double calculate(int a, int b) {
        return a + b - 410 / 12;
    }

    public double calculate(int a, int b, Date date) {
        throw new IllegalArgumentException("WOWOWOWO");
    }
}
